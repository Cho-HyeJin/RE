package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import basic.RoomInformation;
 

public class LoginView extends JFrame{
	
    private Client main;

    private String userName;
    private String emailAdd;
    
   
    public JButton btnJoin;
    public JButton btnLogin;
    //public JPasswordField ssText;
    public JTextField emailText;
    public JPasswordField passText;
    private boolean bLoginCheck;
    
    JPanel panel = new JPanel();
    ImageIcon img = null;
   
    public static void main(String[] args) {
        //new LoginView();
    }
 
    public LoginView() {
        // setting
        setTitle("login");
        setSize(280, 150);
        setResizable(false);
        setBounds(100, 100, 940, 665); //����, ��, ����, ����
       // setLocation(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
       
        // add
        add(panel);
        setVisible(true);
        
        // panel
       // JPanel panel = new JPanel();
        placeLoginPanel(panel);
        
        //ImageIcon back  = new ImageIcon("RE_img.PNG");  //����̹���
     	ImageIcon back  = new ImageIcon("RE_img3.PNG");  //����̹���
        JLabel imgLabel  = new JLabel(back);
        panel.add(imgLabel);
        imgLabel.setVisible(true);
        imgLabel.setBounds(-3, -20, 940, 665);
       

    }
   
    public void placeLoginPanel(JPanel panel){
        panel.setLayout(null);     
        
        JLabel HelloLabel = new JLabel("Hello! Welcome to RE!");
        HelloLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 20));  //=========================== �۾�ü �ٲٴ� �ڵ�
        HelloLabel.setForeground(Color.white);
        HelloLabel.setBounds(122, 119, 209, 18);
		panel.add(HelloLabel);
        
        JLabel passLabel = new JLabel("E-mail");
        passLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));  //======================== �۾�ü �ٲٴ� �ڵ�
        passLabel.setForeground(Color.white);
        passLabel.setBounds(122, 183, 62, 18);
        panel.add(passLabel);
        
        emailText = new JTextField();
        emailText.setBounds(122, 203, 181, 24);
        panel.add(emailText);
        emailText.setColumns(10);
        
        
        JLabel PassLabel = new JLabel("Password");
        PassLabel.setForeground(Color.white);
        PassLabel.setFont(new Font("Berlin Sans FB Demi", Font.BOLD, 18));  //=========================== �۾�ü �ٲٴ� �ڵ�
        PassLabel.setBounds(122, 239, 90, 18);
        panel.add(PassLabel);
       
        
        passText = new JPasswordField();
        passText.setBounds(122, 260, 181, 24);
        panel.add(passText);
        passText.setEchoChar('*');
		passText.setColumns(10);
        
        
		btnJoin = new JButton("JOIN");
		btnJoin.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 13));
		btnJoin.setBounds(122, 321, 79, 27);
		panel.add(btnJoin);
		
		
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Berlin Sans FB Demi", Font.PLAIN, 13));
        btnLogin.setBounds(226, 321, 75, 27);
        panel.add(btnLogin);
        
//        btnLogin.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//               //confirm �������� �׼� ���� �κ�, �� ��ư�� �׼Ǹ����ʸ� ���� ��ǲ���� ���۵ǰ� ������ ����ɿ���
//               //������ �ؽ�Ʈ Area�� �Էµ� ���� �޾ƿð�쿣 .getText() ���� ����Ѵ�.(ex userText.getText()  )
//               main.disposeLogin();
//            }
//        });
    }
   
   
    // mainProcess�� ����
    public void setMain(Client main) {
        this.main = main;
    }
   
}