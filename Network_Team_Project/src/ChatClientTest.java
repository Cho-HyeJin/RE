import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import basic.RoomInformation;

public class ChatClientTest {

	public static void main(String[] args) {
		try {
			System.out.println("Client Start...");
			InetAddress host = InetAddress.getLocalHost();
			Socket clientSocket = new Socket(host, 1234);

			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

			// ä��&���� ����� =======================================================
			out.writeInt(111);// ä�ù��� ����� ���� �������� 111
			out.flush();
			// �� ���� ������
			RoomInformation roomInfor = new RoomInformation("group1", -1, 3, "Who are you?", "I don't know", null);
			out.writeObject(roomInfor);
			out.flush();
			// ���� �ɹ�ȣ �޾ƿ���
			int roomNum = (Integer) in.readObject();
			System.out.println("Room: " + roomNum);
			// ================================================================

			// ä��&���� �� ���� =======================================================
			out.writeInt(222);// ä�ù濡 ���� ���� �������� 222
			out.flush();
			// �Է��� �ɹ�ȣ Ȥ�� �������Է� ���� �ɹ�ȣ�� �������� ������.
			out.writeObject(roomNum);
			out.flush();
			// =================================================================
			
			// ä���� ������========================================================
			Chatting ch = new Chatting(roomNum);
			ch.start(); // ä�� �����带 ����
			while(ch.isAlive());
			
			
			//==================================================================
			System.out.println("Chatting & FILE is dead");
			
			// �����ϱ� ==========================================================
			out.writeInt(888); // protocol bye-bye-bye
			// ��Ʈ���� ���� �ݱ�
			out.close();
			in.close();
			clientSocket.close();
			// ================================================================
		
		} catch (Exception e) {
			System.out.println("Error");
		}
		System.out.println("Client End");
	}
	
	// ä�ù� ������ =====================================================================
	private static class Chatting extends Thread {
		Socket chatSocket;
		ObjectInputStream cin;
		ObjectOutputStream cout;

		/**
		 * Chatting Constructor
		 * @param rm : room number�� ���Ӹ�
		 */
		public Chatting(int rm) {
			try {
				InetAddress host = InetAddress.getLocalHost(); // ���� �ּ� �޾ƿ���
				this.chatSocket = new Socket(host, rm);
				this.cout = new ObjectOutputStream(chatSocket.getOutputStream());
				this.cin = new ObjectInputStream(chatSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			// ä�ù� test 
			Scanner keyboard = new Scanner(System.in);
			try {
				
				for (int i = 0; i < 1; i++) {
					System.out.print("Enter Message: ");
					// �� �κ��� �ٲ㼭 Ű����� �Է��ϴ� �κп� ������ �ɰͰ��ƿ�! =============
					String temp = keyboard.nextLine();
					cout.writeObject(temp);
					cout.flush();
					// =====================================================
					System.out.println((String) cin.readObject()); // �̰� ä�� �ߴµ��� ������ �ɰͰ��ƿ�
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// ���ϰ� ��Ʈ�� ����
					chatSocket.close();
					cin.close();
					cout.close();
					keyboard.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	// ===============================================================================
	
	
	
}
