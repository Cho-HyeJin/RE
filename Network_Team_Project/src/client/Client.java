package client;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import basic.RoomInformation;


public class Client extends JFrame {

	// protected static final String pinNumber = null;
	ObjectInputStream in;
	ObjectOutputStream out;
	Socket socket;

	PrintWriter OUT; // ������ ������ �Է��ϴ� �κп� ����
	LoginView loginView;
	HostView hostView;
	RoomInformation info;

	JPanel contentPane;
	JTextField txtPinNum;
	JTextField textField;

	public Client() {
		info = new RoomInformation();
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

		JTextArea textArea_1 = new JTextArea(); // ä�ó��� �������� �κ�
		textArea_1.setBounds(39, 493, 475, 113);
		contentPane.add(textArea_1);
		/*
		 * contentPane.add(new JScrollPane(textArea_1)); textArea_1.setEditable(false);
		 */ // ���ϸ� ����_��ũ�� ����� �κ�

		JButton btnSending = new JButton("Sending"); // ���� ���� ��ư
		btnSending.setBounds(218, 446, 106, 27);
		contentPane.add(btnSending);

		JButton btnEntrance = new JButton("ENTRANCE");// Pin��ȣ�� ������-> ä�ù� ����
		btnEntrance.setBounds(558, 67, 106, 38);
		btnEntrance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// ����......... ä�ù� ���� �κ�
				// ???????
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

		JTextArea textArea = new JTextArea(); // ä���� �������� �κ�
		textArea.setEditable(false);
		textArea.setBounds(558, 117, 350, 444);
		contentPane.add(textArea);

		/*
		 * ontentPane.add(new JScrollPane(textArea_1)); textArea.setEditable(false);
		 * 
		 * JScrollPane scrollPane1 = new JScrollPane(textArea);
		 * contentPane.add(scrollPane1);
		 */ // ��ũ�� ����� �κ� -> �������� �ڵ� ��ũ�ѷ� �ٲٱ�

		textField.addActionListener(new ActionListener() { /* ���� �Է��ϴ� �κ� */
			public void actionPerformed(ActionEvent e) {
				OUT.println(textField.getText());
				textField.setText("");
			}
		});

		JTextArea txtrPn = new JTextArea();
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
				// info.endDate.set(Integer.parseInt(hostView.yearText.getText()),Integer.parseInt(hostView.monthText.getText()),Integer.parseInt(hostView.dateText.getText()));
				// Calendar cal= Calendar.getInstance();

				try {
					out.writeInt(111);
					out.flush();
					System.out.println("I send 111");
					out.writeObject(info);
					out.flush();

					Object pinNumber = in.readObject();
					// Client.Client().txtrPn.setText(pinNumber);

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

		String serverAddress = getServerAddress(); // ������ �ּ� ���� ����
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
}