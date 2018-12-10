package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.io.DataOutputStream;

import basic.Chat;
import basic.RoomInformation;
import client.JoinView;

public class Client extends JFrame {

	ObjectInputStream in;
	ObjectOutputStream out;
	Socket socket; // waitingRoom socket
	ImageView imageView;
	// String serverAddress = getServerAddress();
	String serverAddress = "192.168.1.136";

	ObjectInputStream inChat;
	ObjectOutputStream outChat;
	Socket chatSocket; // chattingRomm socket

	BufferedOutputStream toServer;
	ObjectInputStream inFile;
	Socket fileSocket;  //fileRoom socket
	FileInputStream fis;
	BufferedInputStream bis;
	DataOutputStream dos;
	
	/*BufferedOutputStream toBuffer;
	BufferedInputStream fromBuffer;
	DataInputStream Dis;
	ObjectOutputStream os;
	Socket filesenderSocket;*/
	
	Socket filesenderSocket;
	BufferedInputStream Bis;
	DataInputStream Dis;
	ObjectOutputStream Oos;
	
	Integer roomNum;
	int valueQNA;
	
	PrintWriter OUT; // ������ ������ �Է��ϴ� �κп� ����
	LoginView loginView;
	JoinView joinView;
	HostView hostView;
	RoomInformation info;
	SecurityQnA securityQnA;
	// UserInfomation data;
	String emailAdd;
	JPasswordField passWord;
	
	
	
	/*ä�ÿ��� �̸�ǥ�ø� ���� �޾ƿ�(�̸����� �������� ���� ��츦 ��� --> ����Ű�� ���)*/
	Chat myChat = new Chat();    //������ �������� ����  �̸���, �̸�, �޼���
	Chat tempChat;               //�������� �޴� ������  �̸���, �̸�, �޼���
	
	EmailCheck mailCheck; //������ ��ȿ�� �˻縦 ���� ����
	
	JPanel contentPane;
	JTextField txtPinNum;
	JTextField textField;
	JTextArea txtrPn;
	JTextArea textArea; // ä�ó��� �������� ��
	JTextArea FileArea;
	DragNDrop egg;
	JButton btnSending;
	JButton btnOpen;

	ImageIcon img = null;
	
	public Client() {
		info = new RoomInformation();
		// data = new UserInfomation();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 940, 665);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		

		this.egg = new DragNDrop();
		this.egg.ta.setIcon(new ImageIcon("R.PNG"));
		this.egg.ta.setBounds(70, 20, 381, 434);
		this.contentPane.add(egg.ta);
		this.egg.setMain(this);

		FileArea = new JTextArea();// ���ϸ� �������� �κ�
		FileArea.setEditable(false);
		FileArea.setBounds(39, 493, 475, 113);
		JScrollPane scroll = new JScrollPane(FileArea);
		scroll.setBounds(39, 493, 475, 113);
		contentPane.add(scroll);
		
		btnOpen = new JButton("Open");
		btnOpen.setBounds(218, 250, 106, 27);
		contentPane.add(btnOpen);
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//�������� ������� �̹��� ���°� ����Ǹ�
				//ImageView ����
		
				
				getImage();
				
				
				try {
					
					Oos.writeInt(31);
					Oos.flush();
					int file_num = Bis.read();//�׷� ������ ����Ǿ� �ִ� ���� ���� 
					System.out.println("file_num: " + file_num);
					//imageView.imageInByte = new byte[imageView.file_num][]; 
					imageView.imageInByte = new byte[file_num][];
					System.out.println("debug1");
					int sign=0;
					int j=0;
					do {
						
						int pro = Bis.read();
						System.out.println("sender_pro ");
						if (pro == 101) {
							Oos.writeInt(13);
							Oos.flush();
							int len = Dis.readInt();
							
							System.out.println(len + " received");

							
								imageView.imageInByte[j] = new byte[len];
								for (int i = 0; i < len; i++) {
									imageView.imageInByte[j][i] = (byte)Bis.read();
								}
								//imageView.imageInByte[j] = buffer[j];
								j++;
							
							sign = Dis.read();
							System.out.println(sign);
							//a++;
							//outFile.flush();
							//outFile.close();
							//Bis.flush();
							//Dis.close();
						}
					} while (sign != 8);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		
	});

		/*
		 * contentPane.add(new JScrollPane(textArea_1)); textArea_1.setEditable(false);
		 */ // ���ϸ� ����_��ũ�� ����� �κ�

		btnSending = new JButton("Sending"); // ���� ���� ��ư
		btnSending.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 15));
		btnSending.setBounds(218, 446, 106, 27);
		contentPane.add(btnSending);
		btnSending.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
				
				run();

			}

		});
	
		JButton btnEntrance = new JButton("ENTRANCE");// �ɹ�ȣ�� ������(TODO**�´��� Ȯ�� : ������������??) -> ä�ù� ����
		btnEntrance.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 13));
		btnEntrance.setBounds(558, 67, 106, 38);
		btnEntrance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					out.writeInt(222); // ä�ù濡 ���ڴٴ� ��ȣ
					out.flush();
					roomNum = Integer.parseInt(txtPinNum.getText()); // user�� �Է��� ���ȣ (String-->Integer)
					out.writeObject(roomNum); // �������� ���ȣ�� �����ִ� �κ�
					out.flush();
					
					getQnA();
					
					String severQ = (String)in.readObject();
					System.out.println(severQ);
					System.out.println(in.readInt());
		
					
					securityQnA.showingQ.append(severQ);
					
					
					
					
					

					/*
					 * fileSocket = new Socket(serverAddress, roomNum+1); // ���ϻ����� ������ IP�ޱ� inFile =
					 * new ObjectInputStream(fileSocket.getInputStream()); outFile = new
					 * BufferedOutputStream(fileSocket.getOutputStream());
					 */
				} catch (IOException | ClassNotFoundException e) {

					e.printStackTrace();
				}

			}

		});
		contentPane.add(btnEntrance);

		txtPinNum = new JTextField();
		txtPinNum.setBounds(676, 68, 232, 37);
		txtPinNum.setText("Input Pin Number"); // Pin��ȣ �Է��ϴ� �κ�
		contentPane.add(txtPinNum);
		txtPinNum.setColumns(10);
		

		JButton btnRoom = new JButton("ROOM"); // �� ����� ��ư
		btnRoom.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 13));
		btnRoom.setBounds(558, 20, 79, 40);
		btnRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getHostInfo(); // �� �����_�˾�â

			}
		});

		contentPane.add(btnRoom);

		textField = new JTextField();
		textField.setBounds(558, 573, 350, 33);
		contentPane.add(textField);
		textField.setColumns(10);

		textArea = new JTextArea(); // ä���� �������� �κ�
		textArea.setEditable(false);
		textArea.setBounds(558, 117, 350, 444);
		JScrollPane scrollArea = new JScrollPane(textArea);
		
		scrollArea.setBounds(558, 117, 350, 444);
		contentPane.add(scrollArea);

		textField.addActionListener(new ActionListener() {         /* ���� �Է��ϴ� �κ� */
			public void actionPerformed(ActionEvent e) {
				try {
					myChat.message = ((textField.getText()) + '\n');
					outChat.writeObject(myChat);
					outChat.flush();
					outChat.reset();
					
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textField.setText("");
			}
		});
		
		txtrPn = new JTextArea();      //�������� ���� �ɹ�ȣ�� �������� �κ�
		txtrPn.setEditable(false);
		// txtrPn.setText("Showing Pin Number");
		txtrPn.setBounds(645, 20, 263, 38);
		contentPane.add(txtrPn);
	}

	private String getServerAddress() {
		return JOptionPane.showInputDialog(this, "Enter IP Address of the Server:", "prototype of R:E",
				JOptionPane.QUESTION_MESSAGE);
	}

	/**
	 * Prompt for and return the desired screen name.
	 */
	//////////////////////////////////////////////////////////////�̹��� ������ /////////////////////////////////////
	
	private void getImage() 
	{
		this.imageView = new ImageView();
		this.imageView.setVisible(true);
	}
	
//////////////////////////////////////////////////////////////////////// ���� ���� ������ ////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	private void getQnA() {
		this.securityQnA = new SecurityQnA(); // �α���â ���̱�
		//System.out.println("3");
		this.securityQnA.setMain(this);
		//System.out.println("2");
		this.securityQnA.setVisible(true);
		//System.out.println("1");
		
		securityQnA.QNAConf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String enSecurityAsr = securityQnA.textAnswer.getText();
				
				try {
					out.writeObject(roomNum);
					out.flush();
					out.writeObject(enSecurityAsr);
					out.flush();
				
					valueQNA = in.readInt();
					System.out.println(valueQNA);
					
					if(valueQNA == 149)
					{
						disposeQnA();
						
						chatSocket = new Socket(serverAddress, roomNum); // ���ϻ����� ������ IP�ޱ�
						outChat = new ObjectOutputStream(chatSocket.getOutputStream());
						inChat = new ObjectInputStream(chatSocket.getInputStream());

						
						// ���� ���� ����
						
						if(in.readBoolean())
						{
							filesenderSocket = new Socket(serverAddress, roomNum + 1);
							Bis = new BufferedInputStream(filesenderSocket.getInputStream());
							Dis = new DataInputStream(Bis);
							Oos = new ObjectOutputStream(filesenderSocket.getOutputStream());
							//Bos = new BufferedOutputStream;
							
							
							/*filesenderSocket = new Socket(serverAddress, roomNum + 1);
							fromBuffer = new BufferedInputStream(filesenderSocket.getInputStream());
							Dis = new DataInputStream(fromBuffer);
							os = new ObjectOutputStream(filesenderSocket.getOutputStream());*/
						} else {
							fileSocket = new Socket(serverAddress, roomNum + 1);
							toServer = new BufferedOutputStream(fileSocket.getOutputStream());
							dos = new DataOutputStream(fileSocket.getOutputStream());
						}
						//toBuffer = new BufferedOutputStream(File);
						//toBuffer = new BufferedOutputStream(filesenderSocket.getOutputStream());
						//BufferedOutputStream toBuffer;
						//BufferedInputStream inBuffer;
						// TODO ������ ������
						new ChatThread().start(); // ä�þ����� ����
						
					}
								
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
			}
			
		});
	
	}
	
//========================================================================= �α���â ������ ===================================================================================//
	
	private void getUserInfo() {
		this.loginView = new LoginView(); 
		this.loginView.setMain(this);
		// this.loginView.setData(userName, emailAdd);
		loginView.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				

				emailAdd = loginView.emailText.getText();
			
				//passWord = loginView.passText.getPassword(); 
			
				String pw = "";
				char[] pwOrigin = loginView.passText.getPassword();
				
				for(char c : pwOrigin)
				{
					pw += c;
					System.out.println(c);
					System.out.println(pw);
				}

				try {
					out.writeInt(180);
					out.writeObject(emailAdd);
					out.writeObject(pw);
					out.flush();
					
					int value = in.readInt();
					System.out.println(value);
					
					
					if(value == 181)
					{
						myChat.email = (String)in.readObject();
						myChat.name = (String)in.readObject();
						disposeLogin();
					}
					else if(value == 183)
					{
						//��ư ������, �ٽú����ֱ�
						System.out.println("SQL-error : 183");
					}
					else if(value == 185)
					{
						//�߸��� ��й�ȣ
						System.out.println("185 : ��й�ȣ ����ġ!");
					}
					else if(value == 187)
					{
						//�������� �ʴ� �̸���
						System.out.println("187 : ���Ե��� ���� �̸���!");
					}
	
				} catch (IOException e1) {
				
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});

		loginView.btnJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getJoinInfo();
			}
		});

	}
	
//==================================================================== ȸ������ ������ =======================================================================================//
	
	private void getJoinInfo() {                           
		this.joinView = new JoinView();
		this.joinView.setMain(this);
		this.joinView.setVisible(true);

		joinView.btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = joinView.JoinName.getText();
				String email = joinView.JoinEmailAdd.getText();
				/**/
				//if()
				
				/**/
				
				String pw1 = joinView.Joinpass.getText();
				String pw2 = joinView.JoinCheck.getText();

				if (pw1.equals(pw2)) { // ��й�ȣ üũ�ϴ� �κ�(��й�ȣ & ��й�ȣ üũ)
					
					try {
						out.writeInt(170);
						out.writeObject(email);
						out.writeObject(name);
						out.writeObject(pw1);
						out.flush();
						
						int value = in.readInt();
						System.out.println(value);
						
						if(value == 171)
						{
							//ȸ������ ����
							disposeJoin();
						}
						else if(value == 175)
						{
							//�̹� �����ϴ� ���̵�
							System.out.println("175 : �̹� ���Ե� �̸���");
						}
						else if(value == 179)
						{
							//��ư �ٽ� ������
							System.out.println("179 : SQL-error");
						}

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						
					disposeJoin();
					
				} else { // ��� ����ġ
					System.out.println("Do not match!");

				}

			}
		});
		// Ȯ�� ��ư ������ ���Ȯ��--> �������� ���� ���� --> Ȯ�ι����� â�ݱ�/ �� ������ '����'�޽�ġ ��� �� ���Է� �ޱ�
	}
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void disposeLogin() {  // �α���â�ݱ�
		loginView.dispose(); 
	}
	
	public void disposeJoin() {  // ȸ������ â�ݱ�
		joinView.dispose(); 
	}
	
	public void disposeHost() {  // �� ������ ���� ���� �Է�â
		hostView.dispose();
	}
	
	public void disposeQnA() {  // �α���â�ݱ�
		securityQnA.dispose(); 
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// ȣ��Ʈ ��ư ������ �� ����� �޼ҵ�
	private void getHostInfo() {
		this.hostView = new HostView();
		this.hostView.setMain(this);
		this.hostView.setInfo(info);
		hostView.btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// confirm �������� �׼� ���� �κ�, �� ��ư�� �׼Ǹ����ʸ� ���� ��ǲ���� ���۵ǰ� ������ ����ɿ���
				// ������ �ؽ�Ʈ Area�� �Էµ� ���� �޾ƿð�쿣 .getText() ���� ����Ѵ�.(ex userText.getText() )
				info.groupName = hostView.userText.getText();
				info.securityQuestion = hostView.secQText.getText();
				info.securityAnswer = hostView.secAText.getText();
				info.howManyPeople = hostView.joinNum.getSelectedIndex() + 1;

				info.endDate = Calendar.getInstance();
				info.endDate.set(Calendar.YEAR, Integer.parseInt(hostView.yearText.getText()));
				info.endDate.set(Calendar.MONTH, Integer.parseInt(hostView.monthText.getText()));
				info.endDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(hostView.dateText.getText()));

				System.out.println("Year : " + info.endDate.get(Calendar.YEAR));
				System.out.println("Month : " + info.endDate.get(Calendar.MONTH));

				try {
					out.writeInt(111);
					out.flush();
					System.out.println("I send 111");
					out.writeObject(info);
					out.flush();

					Integer PinNumber = (Integer) in.readObject();
					String PIN = String.valueOf(PinNumber); // ���ȣ ����
					System.out.println("���� ��ȣ : " + PIN);
					txtrPn.append(PIN); // ���ȣ�� �����ִ� �κ�

				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				disposeHost();

			}
		});

	}
	/*
	 * protected static Object Client() {
	 * 
	 * return null; }
	 */


	private void ConnectSocket() throws IOException {

		// �����κ��� ��ǲ�ޱ�
		// �������� ��Ŵ��

		socket = new Socket(serverAddress, 1234); // ���ϻ����� ������ IP�ޱ�
		in = new ObjectInputStream(socket.getInputStream());
		out = new ObjectOutputStream(socket.getOutputStream());

		System.out.println("Connected!");

		/*
		 * while (true) { String line = in.readLine(); if
		 * (line.startsWith("SUBMITNAME")) { //SUBMITNAME�̸��� �Է¹޾�����
		 * out.println(getName()); } else if(line.startsWith("Entry")){ //����� �����ϸ� ������
		 * �˸��� �κ� textArea_1.append(line.substring(5) + "\n"); } else if
		 * (line.startsWith("NAMEACCEPTED")) { textField.setEditable(true); } else if
		 * (line.startsWith("MESSAGE")) { //������� MESSAGE�� ��ο��� ����϶�� ����� �޴� �κ�
		 * textArea_1.append(line.substring(8) + "\n"); } else
		 * if(line.startsWith("Exit")){ //����� �����ϸ� ������ �˸��� �κ�
		 * textArea_1.append(line.substring(4) + "\n"); } }
		 */

		getUserInfo();
		System.out.println("after get user infor");

		// �� �����δ� �������� �ڵ� �ʿ�
	}

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client REclient = new Client();
					
					ImageIcon back  = new ImageIcon("wood2.PNG");  //����̹���
				    JLabel imgLabel  = new JLabel(back);
				    REclient.add(imgLabel);
				    imgLabel.setVisible(true);
				    imgLabel.setBounds(-3, -20, 940, 665);
					
					REclient.setVisible(true);
					REclient.ConnectSocket();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}
	
//================================================================= ä�� ������  ============================================================================================//
	
	public class ChatThread extends Thread { 
		public void run() { 
			try {
				while (true) {
					
					tempChat = (Chat)inChat.readObject();
					textArea.append(tempChat.getMessage(myChat.email)); // ä��â�� ��� --> <���� : �޼���>
					textArea.setCaretPosition(textArea.getDocument().getLength()); //�ڵ� ��ũ��	

				}
				
			} catch (ClassNotFoundException | IOException e) {

			}
		}
	}

	
//	public class FileThread extends Thread {
		public void run() {
			int sign = 66;
			try {
				for(int i=0; i<egg.listA.size(); i++)
				 {
				   toServer.write(77);
				   toServer.flush();
				
					//��������
				   System.out.println(egg.listA.get(i));
				   File f = new File(egg.listA.get(i));
				   fis = new FileInputStream(f);
				   bis = new BufferedInputStream(fis);
				   System.out.println(f.length() + "rec");
				   dos.writeInt(bis.available());
				   int ch =0;

				   while ((ch = bis.read()) != -1 ) {
					   toServer.write(ch);
	               }
	               System.out.println("end\n");
	               toServer.flush();
	               
	               if(i==egg.listA.size()-1)
	            	   sign=99;
	               dos.write(sign);       
                }
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	//}
}
