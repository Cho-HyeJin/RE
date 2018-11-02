import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		try {
			System.out.println("Client Start...");
			InetAddress host = InetAddress.getLocalHost();
			Socket clientSocket = new Socket(host, 1234);

			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			
				
			// ä�ù游���
			Integer pro = new Integer(111);
			out.writeObject(pro);
			out.flush();
			int roomNum = (Integer)in.readObject();
			System.out.println("Room: "+ roomNum);
			
			// ä�ù����
			pro = new Integer(222);
			out.writeObject(pro);
			out.flush();
			out.writeObject(roomNum);
			
			
			// ä�ù��� ����, in, out �����ϱ�
			Socket chatSocket = new Socket(host, roomNum);
			ObjectOutputStream cout = new ObjectOutputStream(chatSocket.getOutputStream());
			ObjectInputStream cin = new ObjectInputStream(chatSocket.getInputStream());

			// ä�ù� test
			for(int i=0;i<10;i++) {
				cout.writeObject((Integer)i);
				cout.flush();
				System.out.println((String)cin.readObject());
			}
			
			
			
			out.close();
			in.close();
			chatSocket.close();
			clientSocket.close();
			
		} catch (Exception e) {
			System.out.println("Error");
		}
		
		System.out.println("Client End");
	}

}
