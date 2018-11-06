import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.io.File;
import java.io.FileInputStream;
public class fileClient {
	
	public static void main(String[] args) {
		try {
			System.out.println("fileClient Start");
			InetAddress host = InetAddress.getLocalHost();
			Socket fileClientSocket = new Socket(host, 1234);
			
			ObjectInputStream in = new ObjectInputStream(fileClientSocket.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(fileClientSocket.getOutputStream());
			
			// ���Ϸ� ����
			Integer pro = new Integer(222);
			out.writeObject(pro);
			out.flush();
			int roomNum = (Integer)in.readObject();
			out.writeObject(roomNum);
			System.out.println("File Room; "+ roomNum);
			
			// ���Ϸ��� ����, in, out �����ϱ�
			Socket fileSocket = new Socket(host, roomNum);
			ObjectOutputStream fout = new ObjectOutputStream(fileSocket.getOutputStream());
			ObjectInputStream fin = new ObjectInputStream(fileSocket.getInputStream());
			
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
			FileInputStream fileIn = new FileInputStream(f);
			
			byte[] buf = new byte[1024];
			while(fin.read(buf)>0) {
				fout.write(buf);
				fout.flush();
			}
				fout.close();					
			
			out.close();
			in.close();
			fileSocket.close();
			fileClientSocket.close();
			
		} catch (Exception e) {
			System.out.println("File Exception error");
		}
		System.out.println("fileClient end");
	}

}
