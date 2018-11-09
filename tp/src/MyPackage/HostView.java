package MyPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.ComboBoxEditor;

public class HostView extends JFrame {

   private Client main;
   private RoomInformation info;
      
    public JButton btnConfirm;
    private JButton btnInit;
    public JTextField userText,numJoinText,secQText,secAText,yearText,monthText,dateText;
    public JComboBox joinNum;
   
    public static void main(String[] args) {
        //new LoginView();
    }
 
    public HostView() {
        // setting
        setTitle("Create R;E");
        setSize(400, 260);
        setResizable(false);
        setLocation(400, 300);
       // setDefaultCloseOperation(EXIT_ON_CLOSE);
       
        // panel
        JPanel panel = new JPanel();
        placeLoginPanel(panel);
       
       
        // add
        add(panel);
       
        // visiible
        setVisible(true);
        
        
       
    }
   
    public void placeLoginPanel(JPanel panel){
        panel.setLayout(null);     
        JLabel userLabel = new JLabel("�׷� ��");
        userLabel.setBounds(55, 10, 80, 25);
        panel.add(userLabel);
       
        JLabel userLabel3 = new JLabel("�ο� ��");
        userLabel3.setBounds(55, 40, 80, 25);
        panel.add(userLabel3);
        
        JLabel userLabel4 = new JLabel("���� ��¥");
        userLabel4.setBounds(55, 70, 80, 25);
        panel.add(userLabel4);
        
        JLabel userLabel5 = new JLabel("��������");
        userLabel5.setBounds(50, 100, 80, 25);
        panel.add(userLabel5);
        
        JLabel userLabel6= new JLabel("�������� ��");
        userLabel6.setBounds(45, 130, 80, 25);
        panel.add(userLabel6);
        
        JLabel userLabel7 = new JLabel("��");
        userLabel7.setBounds(245, 70, 15, 25);
        panel.add(userLabel7);
        
        JLabel userLabel8= new JLabel("��");
        userLabel8.setBounds(295, 70, 15, 25);
        panel.add(userLabel8);
        
        JLabel userLabel9= new JLabel("��");
        userLabel9.setBounds(345, 70, 15, 25);
        panel.add(userLabel9);
        
        
       
        userText = new JTextField(20);
        userText.setBounds(200, 10, 160, 25);
        panel.add(userText);
        
        String[] number={"1", "2", "3", "4","5"};
        joinNum = new JComboBox(number);
        joinNum.setBounds(200,40,160,25);
        panel.add(joinNum);
        
        
        
        
        
        yearText = new JTextField(20);
        yearText.setBounds(200, 70, 40, 25);
        panel.add(yearText);
        
        monthText = new JTextField(20);
        monthText.setBounds(265, 70, 25, 25);
        panel.add(monthText);
        
        dateText = new JTextField(20);
        dateText.setBounds(315, 70, 25, 25);
        panel.add(dateText);
        
        
        
        secQText = new JTextField(20);
        secQText.setBounds(200, 100, 160, 25);
        panel.add(secQText);
        
        secAText = new JTextField(20);
        secAText.setBounds(200, 130, 160, 25);
        panel.add(secAText);
        secAText.addActionListener(new ActionListener() {          
            @Override
            public void actionPerformed(ActionEvent e) {
               main.disposeHost();
            }
        });
        
        
       
        btnInit = new JButton("Close");
        btnInit.setBounds(70, 175, 100, 25);
        panel.add(btnInit);
        btnInit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               main.disposeHost();
            }
        });
       
        btnConfirm = new JButton("Confirm");
        btnConfirm.setBounds(230, 175, 100, 25);
        panel.add(btnConfirm);
               
    }
   
 
   
    // mainProcess�� ����
    public void setMain(Client main) {
        this.main = main;
    }
    public void setInfo(RoomInformation info) {
    	this.info = info;
    }
   
}