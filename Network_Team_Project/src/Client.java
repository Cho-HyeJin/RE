import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import basic.RoomInformation;

public class Client {

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
			Transmission tr = new Transmission(roomNum);
			tr.start();
			while(ch.isAlive() || tr.isAlive()) {
				// ä�ù��� ������� ������ �ƹ��͵� ����
				// TODO: ������ �ʿ���
			}
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
	
	
	private static class Transmission extends Thread {
		  // Socket fileSocket = new Socket(host, roomNum);
	       //ObjectOutputStream fout = new ObjectOutputStream(fileSocket.getOutputStream());
	       //ObjectInputStream fin = new ObjectInputStream(fileSocket.getInputStream());
	       
		   Socket fileSocket;
		   ObjectInputStream fin;
		   ObjectOutputStream fout;
		   
	       public Transmission(int rm) {
	    	   try {
					InetAddress host = InetAddress.getLocalHost(); // ���� �ּ� �޾ƿ���
					this.fileSocket = new Socket(host, rm+1);
					this.fout = new ObjectOutputStream(fileSocket.getOutputStream());
					this.fin = new ObjectInputStream(fileSocket.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
	       }
	       public void run() {
	    	   try {
	    	// ���Ϸ� test
	           int i=0;
	           fout.writeObject((Integer)i);
	           fout.flush();
	           
	           //String groupN = (String)fin.readObject(); // �׷��
	           //String str = (String)in.readObject();
	           String imageName = "�ͽ�������01";
	           fout.writeObject((String)imageName);
	           System.out.println("file name: " + imageName);
	           File f = new File("C:\\Users\\������\\Downloads\\2018-2�б�\\����", imageName + ".jpg");
	           //FileInputStream fileIn = new FileInputStream(f);
	          FileOutputStream fos = new FileOutputStream(f);
	          // ObjectInputStream ois = new ObjectInputStream(fileSocket.getInputStream());
	           byte[] buf = new byte[1024];
	           int n =0;
	    	   int cnt = 0;
	    	   long fileSize = 0;
	          // while(fin.read(buf, 0, 1024) ) {
	           while ((n = fin.read(buf)) != -1) {
	        	  fileSize += n;
	              fout.write(buf);
	              fout.flush();
	              cnt++;
	           }
	              this.fout.close();               
	    	   } catch (Exception e) {
	    		   e.printStackTrace();
	    	   } finally {
	    		   try {
	    			   fileSocket.close();
	    			   this.fin.close();
	    			   this.fout.close();
	    		   } catch (Exception e) {
	    			   e.printStackTrace();
	    		   }
	    	   }
	       }
	   }
}
