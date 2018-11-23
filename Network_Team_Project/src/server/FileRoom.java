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

public class FileRoom extends Room{

   public FileRoom(Socket socket) {
      super(socket);
   }
   public FileRoom(Socket socket, RoomInformation rf)
   {
      super(socket);
      this.roomInfor = rf;
   }
   
   public void run() {
      System.out.println("Enter the FileRoom!");
      try {
    	
    	  
    	  
    	  ObjectOutputStream toCleint = new ObjectOutputStream(roomSocket.getOutputStream());
          String imageName = "�ͽ�������01";
          //toClient.writeObject(imageName);
          System.out.println("file name: " + imageName);
         
          InputStream in = roomSocket.getInputStream();
         // FileOutputStream out = new FileOutputStream("C:\\work\\apple.jpg"); 
          int fPro = in.read();
          
          /*
           * group_id - DB���� �ҷ����� // ������ �׷������ �ϴ�!!
           * fpro�� client�� ������ ������ 77, ���� ������ ������ 88
           * fpro = 77�̸� ���� �޾Ƽ� ����
           * fpro = 88�̸� socket �ݱ�
           */
        //String groupN = (String)fin.readObject(); // �׷��
          //String str = (String)in.readObject();
          
          if(fPro == 77)
          {
        	  File f = new File("C:\\Users\\������\\Downloads\\folderNN\\groupNB"); // folderNN - ����, groupNB - group_id
        	  if(f.exists() == false)
        		  f.mkdirs();
        	  FileOutputStream out = new FileOutputStream(f+ "\\us1.png"); 

        	  byte[] buffer = new byte[8192];
        	  int bytesRead=0;
        	  while ((bytesRead = in.read(buffer)) > 0) {
        		  out.write(buffer, 0, bytesRead);
                  out.flush();
                  out.close();
           }
          } else if(fPro == 88) {
          }
      } catch (IOException e) {
         e.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      }
      finally {
         try {
            roomSocket.close();
         } catch (IOException e) {
            
         }
      }
      
      
   }
}