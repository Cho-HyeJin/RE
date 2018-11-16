package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import basic.RoomInformation;

public class Client extends JFrame {

	ObjectInputStream in;
	ObjectOutputStream out;
	Socket socket; // waitingRoom socket

	String serverAddress = getServerAddress();
	ObjectInputStream inChat;
	ObjectOutputStream outChat;
	Socket chatSocket; //chattingRomm socket

	BufferedOutputStream outFile;
	ObjectInputStream inFile;
	Socket fileSocket;  //fileRoom socket

	PrintWriter OUT; // ������ ������ �Է��ϴ� �κп� ����
	LoginView loginView;
	HostView hostView;
	RoomInformation info;
	// UserInfomation data;
	String userName;
	String emailAdd;

	JPanel contentPane;
	JTextField txtPinNum;
	JTextField textField;
	JTextArea txtrPn;
	JTextArea textArea;  //ä�ó��� �������� ��

	public Client() {
		info = new RoomInformation();
		// data = new UserInfomation();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 940, 665);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		DragNDrop egg = new DragNDrop();
		egg.ta.setIcon(new ImageIcon("R.PNG"));
		egg.ta.setBounds(70, 20, 381, 434);
		contentPane.add(egg.ta);

		JTextArea textArea_1 = new JTextArea();// ���ϸ� �������� �κ�
		textArea_1.setEditable(false);
		textArea_1.setBounds(39, 493, 475, 113);
		JScrollPane scroll = new JScrollPane(textArea_1);
		scroll.setBounds(39, 493, 475, 113);
		// contentPane.add(textArea_1);
		contentPane.add(scroll);

		/*
		 * contentPane.add(new JScrollPane(textArea_1)); textArea_1.setEditable(false);
		 */ // ���ϸ� ����_��ũ�� ����� �κ�

		JButton btnSending = new JButton("Sending"); // ���� ���� ��ư
		btnSending.setBounds(218, 446, 106, 27);
		contentPane.add(btnSending);

		JButton btnEntrance = new JButton("ENTRANCE");// �ɹ�ȣ�� ������(TODO**�´��� Ȯ�� : ������������??) -> ä�ù� ����
		btnEntrance.setBounds(558, 67, 106, 38);
		btnEntrance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					out.writeInt(222);  //ä�ù濡 ���ڴٴ� ��ȣ
					out.flush();
					Integer roomNum = Integer.parseInt(txtPinNum.getText());  //user�� �Է��� ���ȣ (String-->Integer)
					out.writeObject(roomNum);  //�������� ���ȣ�� �����ִ� �κ�
					out.flush();

					chatSocket = new Socket(serverAddress, roomNum); // ���ϻ����� ������ IP�ޱ�
					outChat = new ObjectOutputStream(chatSocket.getOutputStream());
					inChat = new ObjectInputStream(chatSocket.getInputStream());
					
					//TODO ������ ������
					new ChatThread().start();  //ä�þ����� ����

					/*
					 * fileSocket = new Socket(serverAddress, roomNum+1); // ���ϻ����� ������ IP�ޱ� inFile =
					 * new ObjectInputStream(fileSocket.getInputStream()); outFile = new
					 * BufferedOutputStream(fileSocket.getOutputStream());
					 */
				} catch (IOException e) {

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
		textArea.setCaretPosition(textArea.getDocument().getLength());
		scrollArea.setBounds(558, 117, 350, 444);
		contentPane.add(scrollArea);

		textField.addActionListener(new ActionListener() { /* ���� �Է��ϴ� �κ� */
			public void actionPerformed(ActionEvent e) {
				try {
					outChat.writeObject((textField.getText() + '\n'));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				textField.setText("");
			}
		});
		txtrPn = new JTextArea();
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

	private void getUserInfo() {
		this.loginView = new LoginView(); // �α���â ���̱�
		this.loginView.setMain(this);
		// this.loginView.setData(userName, emailAdd);
		loginView.btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("name email");
				userName = loginView.userText.getText();
				emailAdd = loginView.emailText.getText();

				System.out.println(userName); 
				System.out.println(emailAdd);
			}
		});

	}

	public void disposeLogin() {
		loginView.dispose(); // �α���â�ݱ�
	}

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
					String PIN = String.valueOf(PinNumber); //���ȣ ����
					System.out.println(PIN); 
					txtrPn.append(PIN);  //���ȣ�� �����ִ� �κ�

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

	public void disposeHost() {
		hostView.dispose();
	}

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

		// �� �����δ� �������� �ڵ� �ʿ�
	}

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client REclient = new Client();
					REclient.setVisible(true);
					REclient.ConnectSocket();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public class ChatThread extends Thread {  //ä�� ������ 
		public void run() {
			String line;
			try {
				while (true) {
					line = (String) inChat.readObject();  //�������� ���� �޾Ƽ� ����
					textArea.append(line);  //ä��â�� ���

				}
			} catch (ClassNotFoundException | IOException e) {

			}
		}
	}
}