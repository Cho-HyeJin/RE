package server;
import java.net.ServerSocket;

public class ReServer {
	
	
	private static final int PORT = 1234; // TODO: RE���� ��Ʈ��ȣ ���ؾ���
	
	
	public static void main(String[] args) throws Exception {
		System.out.println("The RE server is running.");
		ServerSocket listener = new ServerSocket(PORT);
		try {
			while(true) {
				new WaitingRoom(listener.accept()).start();
			}
		} finally {
			listener.close();
		}
	}
}