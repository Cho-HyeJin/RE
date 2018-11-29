package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import basic.Room;
import basic.RoomInformation;

public class WaitingRoom extends Room {

	public static HashMap<Integer, ServerSocket> chatRoomServerSockets = new HashMap<Integer, ServerSocket>();
	public static HashMap<Integer, RoomInformation> roomInforMap = new HashMap<Integer, RoomInformation>();
	public static HashMap<Integer, ServerSocket> fileRoomServerSockets = new HashMap<Integer, ServerSocket>();

	private static Integer ROOMPIN = new Integer(10000);

	WaitingRoom(Socket socket) {
		super(socket);
	}

	public void run() {
		try {
			System.out.println("Welcome Waiting Room");
			db = new Database();
			toClient = new ObjectOutputStream(roomSocket.getOutputStream());
			fromClient = new ObjectInputStream(roomSocket.getInputStream());

			// loop until login success
			while (!LogIn())
				;
			System.out.println("Success LogIn");

			// ����ڰ� waiting room���� �ϴ� ���� Ȯ��
			// ������ ����Ǿ� ���� ������ �����ȴ�.
			while (roomSocket.isConnected()) {

				protocol = (Integer) fromClient.readInt();
				System.out.println("protocol: " + protocol);
				// protocol
				// 111 : user want to make a room
				// 222 : user want to enter the room
				// 888 : bye
				if (111 == protocol) { // Make a room
					// TODO : �� �ɼ��� �ùٸ��� Ȯ���Ѵ�.
					roomInfor = (RoomInformation) fromClient.readObject();
					roomInfor.print();

					// �� ����� --> ���� ������ ����� ���´�.
					// �� ����⸦ ��û�� Ŭ���̾�Ʈ���� �ɹ�ȣ�� �������ش�.
					try {
						System.out.println("Enter protocol 111");
						int roomNumber = makeChatRoom();
						makeFileRoom(roomNumber + 1);

						toClient.writeObject(roomNumber);
						toClient.flush();
						System.out.println(roomNumber + " room made");

					} catch (Exception e) {
						// TODO : ���� �������� ó���ؾ��Ѵ�!!!!!!
						toClient.writeBytes("ERROR: FAILED MAKING ROOM");
						toClient.writeBytes("ERROR: FAILED MAKING FileROOM");
						e.printStackTrace();
					}
					System.out.println("End Protocol 111");

				} else if (222 == protocol) { // Enter the room
					System.out.println("Enter protocol 222");
					
					// send room's question
					int PIN = (Integer) fromClient.readObject();
					toClient.writeObject(db.GetRoomQuestion(PIN));
					
					// check room's answer is correct
					PIN = (Integer) fromClient.readObject();
					String an = (String)fromClient.readObject();
					toClient.writeInt(db.CheckRoomAnswer(an, PIN));
					
					// enter the room
					System.out.println("Enter room Pin in " + PIN);
					enterChatRoom(PIN);
					enterFileRoom(PIN + 1);

				} else if (888 == protocol) {
					toClient.close();
					fromClient.close();
					break;

				} else {
					// TODO : �������� ���� ó���ؾ���.
				}
			}

		} catch (ClassNotFoundException e) {
			System.out.println("Error 1");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error 2");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Error 3");
			e.printStackTrace();
		} finally {
			try {
				if (!roomSocket.isClosed())
					roomSocket.close();
			} catch (IOException e) {

			}
		}

		System.out.println("End Watiing Room");
	}

	/**
	 * LogIn
	 * 
	 * �� �Լ��� ù ȭ�鿡�� ������� �̸��ϰ� �̸��� �޴� �Լ��̴�. �̸����� ����ũ�ؾ��ϸ�, ���� �̸����� 2���� ���ÿ� ��� �� �� ����
	 * �ؾ��Ѵ�.
	 * 
	 * @return : �α����� ���������� �Ǿ����� true�� ����, �ٸ� ������ ���� ��� false �� �����Ѵ�.
	 */
	private boolean LogIn() throws Exception {

		protocol = fromClient.readInt();
		if (protocol == 170) { // ȸ������
			String email = (String) fromClient.readObject();
			String userName = (String) fromClient.readObject();
			String password = (String) fromClient.readObject();

			System.out.println("from client: " + email + " " + userName + " " + password);

			int result = db.InsertUserInfor(userName, email, password);
			if (result == 1) {
				toClient.writeInt(171); // success
				toClient.flush();
			} else if (result == -1) {
				toClient.writeInt(175); // already exist
				toClient.flush();
			} else if (result == 0) {
				toClient.writeInt(179); // sql error
				toClient.flush();
			}
			System.out.println("join " + result);

		} else if (protocol == 180) { // �α���
			String email = (String) fromClient.readObject();
			String password = (String) fromClient.readObject();

			System.out.println("from client: " + email + " " + password);

			int result = db.CheckPassword(email, password);
			if (result == 1) {
				toClient.writeInt(181); // success
				toClient.flush();
				db.CommitDB();
				return true;
			} else if (result == 0) {
				toClient.writeInt(183); // sql error
				toClient.flush();
			} else if (result == -1) {
				toClient.writeInt(185); // wrong password
				toClient.flush();
			} else if (result == -2) {
				toClient.writeInt(187); // not exist email
				toClient.flush();
			}
			System.out.println("join " + result);

		}

		db.CommitDB();
		return false;
	}

	// For ChatRoom
	// ============================================================================================
	/**
	 * makeChatRoom
	 * 
	 * �� �Լ��� ����ڰ� �� ����� ��ư�� ������, �ùٸ� �ɼ��� �Է��� �� ä�ù��� ���������� �Ҵ��ϴ� �Լ� �̴�. ����ũ�� �ɹ�ȣ�� ������
	 * �ɶ� ���� �� ��ȣ�� �Ҵ��� �õ��Ѵ�.
	 * 
	 * @return : ä�ù��� PIN ��ȣ�� �������ش�. (ä�ù��� PIN ��ȣ�� Ȧ���̴�.)
	 */
	private int makeChatRoom() throws Exception {
		int PIN;
		System.out.println("Enter makeChatRoom");
		synchronized (chatRoomServerSockets) {
			PIN = makePIN();
			if (chatRoomServerSockets.containsKey((Integer) PIN)) {
				System.out.println("Already Exist - ChatRoom:" + PIN);
			}
			synchronized (db) {
				roomInfor.port = PIN;
				db.InsertRoomInfor(roomInfor);
			}
			synchronized (roomInforMap) {
				roomInforMap.put(PIN, roomInfor);
			}
			ServerSocket tempSS = new ServerSocket(PIN);
			roomInfor.port = PIN;
			chatRoomServerSockets.put(PIN, tempSS);
		}
		System.out.println("end makeChatRoom");
		return PIN;
	}

	/**
	 * enterChatRoom
	 * 
	 * �� �Լ��� ����ڰ� PIN ��ȣ�� �Է��ϰ� �濡 ������⸦ ������ �� ���� �Լ��̴�. ����ڰ� �ùٸ� PIN ��ȣ�� �Է����� ���� ������
	 * ������ �ش�.
	 * 
	 * @param PIN:
	 *            ���� ���� ä�ù��� �ɹ�ȣ
	 * @return ���� ���� �濡 ������ true�� �������ش�.
	 */
	private boolean enterChatRoom(int PIN) {
		if (chatRoomServerSockets.containsKey(PIN)) {
			try {
				new ChatRoom(chatRoomServerSockets.get(PIN).accept(), roomInforMap.get(PIN)).start();

				System.out.println("Method enterChatroom successed");
				return true;
			} catch (Exception e) {
				System.out.println("Error in enterChatroom");
			}
		}
		return false;
	}
	// End ChatRoom
	// Method=======================================================================================

	// ���Ϸ�
	/**
	 * makeFileRoom
	 * 
	 * �� �Լ��� ����ڰ� �� ����� ��ư�� ������, �ùٸ� �ɼ��� �Է��� �� ä�ù��� ���������� �Ҵ��ϴ� �Լ� �̴�. ����ũ�� �ɹ�ȣ�� ������
	 * �ɶ� ���� �� ��ȣ�� �Ҵ��� �õ��Ѵ�.
	 * 
	 * @return : ���Ϸ��� PIN ��ȣ�� �������ش�. (ä�ù��� PIN ��ȣ�� ¦���̴�.)
	 */
	private void makeFileRoom(int fileRM) throws Exception {
		System.out.println("Enter makeFileRoom");
		synchronized (fileRoomServerSockets) {

			if (fileRoomServerSockets.containsKey((Integer) fileRM)) {
				System.out.println("eeeeeeaaaaak - file");
			}
			ServerSocket tempSV = new ServerSocket(fileRM);

			fileRoomServerSockets.put(fileRM, tempSV);
		}
		System.out.println("end makeFileRoom");
	}

	/**
	 * enterFileRoom
	 * 
	 * �� �Լ��� ����ڰ� PIN ��ȣ�� �Է��ϰ� �濡 ������⸦ ������ �� ���� �Լ��̴�. ����ڰ� �ùٸ� PIN ��ȣ�� �Է����� ���� ������
	 * ������ �ش�.
	 * 
	 * @param PIN
	 *            - ���� ���� ä�ù��� �ɹ�ȣ
	 * @return ���� ���� �濡 ������ true�� �������ش�.
	 */
	private static boolean enterFileRoom(int PIN) {
		if (fileRoomServerSockets.containsKey(PIN)) {
			try {
				new FileRoom(fileRoomServerSockets.get(PIN).accept(), roomInforMap.get(PIN - 1)).start();
				System.out.println("enterFileroom very good");
				return true;
			} catch (Exception e) {
				System.out.println("Error in enterFileroom");
			}
		}
		System.out.println("FFFFFFFFFF - file");
		return false;
	}

	/**
	 * makePIN
	 * 
	 * �ùٸ� �ɹ�ȣ�� �������� �Ҵ��� �ش�.
	 * 
	 * @return 10000 �̻��� ������
	 */
	private static int makePIN() {
		int PIN;

		synchronized (ROOMPIN) {
			PIN = ROOMPIN;
			ROOMPIN += 2;
		}
		System.out.println("End makePin");
		return PIN;
	}

}
