package server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import basic.Room;
import basic.RoomInformation;

public class FileSender extends Room {

	public FileSender(Socket socket) {
		super(socket);
		// TODO Auto-generated constructor stub
	}

	public FileSender(Socket socket, int port) {
		super(socket);
		this.portNumber = port;
	}

	public void run() {
		int sender_pro = 101; // �ð��� ��ġ �� ����ڰ� �濡 ���� �����ִ� ��������
		// int send_sign = 666;
		/*
		 * ������ ����Ǿ� �ִ� ���� ���� ������!
		 */
		try {
			
				String group_name = db.GetRoomName(portNumber);

				File file = new File("C:\\RE\\" + group_name); // �� �׷������ ����� ����

				File[] fileList = file.listFiles();
				String filepath = null;

				BufferedOutputStream toClient_Re = null;
				BufferedInputStream Bin = null;
				FileInputStream Fin = null;
				DataOutputStream Dout = null;

				Dout.write(fileList.length); // ������ ���� ���� ����
				int sign=0;
				for (int i = 0; i < fileList.length; i++) {
					Dout.write(sender_pro); // �������� ���� - ���� ���ٴ� ��ȣ
					File files = fileList[i];
					System.out.println(files.getName());
					filepath = files.getPath();
					toClient_Re = new BufferedOutputStream(roomSocket.getOutputStream());
					Dout = new DataOutputStream(roomSocket.getOutputStream());
					//Dout.writeUTF(filepath);

					Fin = new FileInputStream(filepath);
					Bin = new BufferedInputStream(Fin);

					Dout.writeInt(Bin.available());
					int bytesRead = 0;
					while ((bytesRead = Bin.read()) != -1) {
						toClient_Re.write(bytesRead);
					}
					toClient_Re.flush();
					// new Thread().sleep(1000);
					if( i == fileList.length) {
						sign = 8;
						toClient_Re.write(sign);
					}
				}
				Fin.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				roomSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
