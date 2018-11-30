package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import basic.Room;
import basic.RoomInformation;

public class FileRoom extends Room {

	int cnt=0;
	public FileRoom(Socket socket) {
		super(socket);
	}

	public FileRoom(Socket socket, RoomInformation rf) {
		super(socket);
		this.roomInfor = rf;
	}

	public void run() {
		System.out.println("Enter the FileRoom!");
		try {

			ObjectOutputStream toCleint = new ObjectOutputStream(roomSocket.getOutputStream());
			String imageName = "�ͽ�������01";
			// toClient.writeObject(imageName);
			System.out.println("file name: " + imageName);

			InputStream in = roomSocket.getInputStream();
			
			int fPro = in.read(); // �������� 
			

			String group_name = roomInfor.groupName;
			
			if (fPro == 77) {
				//������ ���� �ޱ�
				int count = in.read();
				for(int i=0;i<count;i++)
				{
				System.out.println("roof\n");
				int size = in.read();
				File f = new File("C:\\RE\\" + group_name); // folderNN - ����, groupNB - group_id
				if (f.exists() == false)
					f.mkdirs();
				FileOutputStream out = new FileOutputStream(f + "\\us" +cnt +".png");

				byte[] buffer = new byte[8192];
				int bytesRead = 0;
				while ((bytesRead = in.read(buffer)) > 0 && (bytesRead <=  size)) {
					out.write(buffer, 0, bytesRead);

				}
				cnt++;
				out.flush();
				//out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				roomSocket.close();
			} catch (IOException e) {

			}
		}

	}
}