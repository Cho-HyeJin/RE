package client;

import java.util.regex.Pattern;


public class EmailCheck {
	public static void main(String[] args) {
		String mail;
		
//		if(isEmail(mail)  == true)
//			System.out.println("True");
//		else
//			System.out.println("False");
	}


	public static boolean isEmail(String email) {
		boolean check;
		
		if (email == null) //�̸����� �Էµ��� �ʾ��� ��
			return false;
	
		check = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
		return check; //�̸����� �ùٸ� ���� : true, �ùٸ��� ���� ���� : false �� ��ȯ
	}
}