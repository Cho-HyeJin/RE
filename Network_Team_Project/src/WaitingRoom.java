
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;

import basic.Room;

public class WaitingRoom extends Room {
	private String userName;
	private String eMail;
	public static HashMap<Integer, ServerSocket> chatRoomServerSockets = new HashMap<Integer, ServerSocket>();
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

			// ����ڰ� waiting room���� �ϴ� ���� Ȯ��
			// ������ ����Ǿ� ���� ������ �����ȴ�.
			while (roomSocket.isConnected()) {
				
				toClient = new ObjectOutputStream(roomSocket.getOutputStream());
				fromClient = new ObjectInputStream(roomSocket.getInputStream());
				

				// for test
				System.out.println("Test it");
				System.out.println((String)fromClient.readObject());
				toClient.writeObject("Message: return");
//				Communication cm = (Communication)fromClient.readObject();
//				Communication res = new Communication("Okay");
//				toClient.writeObject(res);
//				toClient.flush();
				
				
				
				
				
				protocol = (Integer)fromClient.readObject();
				System.out.println("protocol: " + protocol);
				// protocol
				// 1 : ���� ����� �ʹ�.
				// 2 : �濡 ���� �ʹ�.
				if (111 == protocol) { // Make the room
					// ���� ���� �ɼ��� �޾ƿ���, �ùٸ��� Ȯ���Ѵ�.
					// TODO : �������� üũ�Ұ���, Ŭ���̾�Ʈ���� üũ�Ұ��� ������ ����
					//RoomInformation roomInfor = (RoomInformation) fromClient.readObject();

					// �� ����� --> ���� ������ ����� ���´�.
					// �� ����⸦ ��û�� Ŭ���̾�Ʈ���� �ɹ�ȣ�� �������ش�.
					try {
						System.out.println("Enter protocol 111");
						int roomNumber = makeChatRoom();
						System.out.println("TT");
						toClient.writeObject(roomNumber);
						toClient.flush();
						System.out.println(roomNumber + " room made");
						
					} catch (Exception e) {
						// TODO : ���� �������� ó���ؾ��Ѵ�!!!!!!
						toClient.writeBytes("ERROR: FAILED MAKING ROOM");
					}

				} else if (2 == protocol) { // Enter the room
					int PIN = fromClient.read();
					enterChatRoom(PIN);

				} else {
					/*
					 * ���� ó��
					 */
				}
			}

		} catch (Exception e) {
			/*
			 * Ŭ���� ĳ���� ����
			 */

		} finally {
		}
		/*
		 * ����� �α� �ƿ� ���Ѿ���
		 */
		try {
			roomSocket.close();
		} catch (IOException e) {

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
			do {
				PIN = makePIN();
			} while (!chatRoomServerSockets.containsKey(PIN));
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
	 * @param PIN
	 *            - ���� ���� ä�ù��� �ɹ�ȣ
	 * @return ���� ���� �濡 ������ true�� �������ش�.
	 */
	private static boolean enterChatRoom(int PIN) {
		if (chatRoomServerSockets.containsKey(PIN)) {
			try {
				new ChatRoom(chatRoomServerSockets.get(PIN).accept()).start();
				return true;
			} catch (Exception e) {
				
			}
		}
		return false;
	}

	/**
	 * makePIN
	 * 
	 * �ùٸ� �ɹ�ȣ�� �������� �Ҵ��� �ش�.
	 * 
	 * @return 10�̻󿡼� 100000 �̸��� ���� �� �Ҵ���� ���� ��ȣ�� �����Ѵ�.
	 */
	private static int makePIN() {
		Random random = new Random();
		int PIN = random.nextInt(100000);

		if (PIN % 2 == 0)
			PIN = PIN + 1;
		while (PIN > PROTOCOL_NUMBER && !chatRoomServerSockets.containsKey(PIN)) {
			PIN = random.nextInt();
			if (PIN % 2 == 0)
				PIN = PIN + 1;
		}
		System.out.println("End makePin");
		return PIN;
	}

}
