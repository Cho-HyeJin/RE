
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import basic.Room;

public class WaitingRoom extends Room {
	private String userName;
	private String eMail;
	public static HashMap<Integer, ServerSocket> chatRoomServerSockets = new HashMap<Integer, ServerSocket>();
	public static HashMap<Integer, ServerSocket> fileRoomServerSockets = new HashMap<Integer, ServerSocket>();
	private static Integer ROOMPIN = new Integer(10000);
	private static final int PROTOCOL_NUMBER = 10;

	WaitingRoom(Socket socket) {
		super(socket);
	}

	public void run() {
		try {
			System.out.println("Enter Waiting Room");
			// TODO : �α��� ������ �ް� �ùٸ��� Ż��
			while (true) {
				break; // ������ �ڵ�
			}

			
			toClient = new ObjectOutputStream(roomSocket.getOutputStream());
			fromClient = new ObjectInputStream(roomSocket.getInputStream());

			
			// ����ڰ� waiting room���� �ϴ� ���� Ȯ��
			// ������ ����Ǿ� ���� ������ �����ȴ�.
			while (roomSocket.isConnected()) {

				protocol = (Integer) fromClient.readObject();
				System.out.println("protocol: " + protocol);
				// protocol
				// 111 : ���� ����� �ʹ�.
				// 222 : �濡 ���� �ʹ�.
				if (111 == protocol) { // Make the room
					// ���� ���� �ɼ��� �޾ƿ���, �ùٸ��� Ȯ���Ѵ�.
					// TODO : �������� üũ�Ұ���, Ŭ���̾�Ʈ���� üũ�Ұ��� ������ ����
					// RoomInformation roomInfor = (RoomInformation) fromClient.readObject();

					// �� ����� --> ���� ������ ����� ���´�.
					// �� ����⸦ ��û�� Ŭ���̾�Ʈ���� �ɹ�ȣ�� �������ش�.
					try {
						System.out.println("Enter protocol 111");
						int roomNumber = makeChatRoom();
						toClient.writeObject(roomNumber);
						toClient.flush();
						System.out.println(roomNumber + " room made");

					} catch (Exception e) {
						// TODO : ���� �������� ó���ؾ��Ѵ�!!!!!!
						toClient.writeBytes("ERROR: FAILED MAKING ROOM");
					}
					System.out.println("End Protocol 111");

				} else if (222 == protocol) { // Enter the room
					System.out.println("Enter protocol 222");
					int PIN = (Integer)fromClient.readObject();
					System.out.println("Enter room Pin in " + PIN);
					enterChatRoom(PIN);

				} else {
					// TODO : �������� ���� ó���ؾ���.
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/*
			 * ����� �α� �ƿ� ���Ѿ���
			 */
			try {
				roomSocket.close();
			} catch (IOException e) {

			}
		}
		
		System.out.println("End Watiing Room");
	}

	/**
	 * logIn
	 * 
	 * �� �Լ��� ù ȭ�鿡�� ������� �̸��ϰ� �̸��� �޴� �Լ��̴�. �̸����� ����ũ�ؾ��ϸ�, ���� �̸����� 2���� ���ÿ� ��� �� �� ����
	 * �ؾ��Ѵ�.
	 * 
	 * @return : �α����� ���������� �Ǿ����� true�� ����, �ٸ� ������ ���� ��� false �� �����Ѵ�.
	 */
	private boolean logIn() {

		return true;

	}

	/**
	 * makeRoom
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
				System.out.println("eeeeeeaaaaak");
			}
			ServerSocket tempSS = new ServerSocket(PIN);
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
	 * @param PIN: ���� ���� ä�ù��� �ɹ�ȣ
	 * @return ���� ���� �濡 ������ true�� �������ش�.
	 */
	private static boolean enterChatRoom(int PIN) {
		if (chatRoomServerSockets.containsKey(PIN)) {
			try {
				new ChatRoom(chatRoomServerSockets.get(PIN).accept(),PIN,"user1","user@naver.com").start();
				System.out.println("enterChatroom very good");
				return true;
			} catch (Exception e) {
				System.out.println("Error in enterChatroom");
			}
		}
		return false;
	}


	   // ���Ϸ�
	   /**
	    * makeFileRoom
	    * 
	    * �� �Լ��� ����ڰ� �� ����� ��ư�� ������, �ùٸ� �ɼ��� �Է��� �� ä�ù��� ���������� �Ҵ��ϴ� �Լ� �̴�. ����ũ�� �ɹ�ȣ�� ������
	    * �ɶ� ���� �� ��ȣ�� �Ҵ��� �õ��Ѵ�.
	    * 
	    * @return : ���Ϸ��� PIN ��ȣ�� �������ش�. (ä�ù��� PIN ��ȣ�� ¦���̴�.)
	    */
	   private int makeFileRoom() throws Exception {
	      int PIN;
	      System.out.println("Enter makeFileRoom");
	      synchronized (fileRoomServerSockets) {

	         PIN = makePIN()+1;
	         if (fileRoomServerSockets.containsKey((Integer) PIN)) {
	            System.out.println("eeeeeeaaaaak - file");
	         }
	         ServerSocket tempSV = new ServerSocket(PIN);

	         fileRoomServerSockets.put(PIN, tempSV);
	      }
	      System.out.println("end makeFileRoom");
	      return PIN;
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
	            new ChatRoom(fileRoomServerSockets.get(PIN).accept()).start();
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
	 * @return 10�̻󿡼� 100000 �̸��� ���� �� �Ҵ���� ���� ��ȣ�� �����Ѵ�.
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
