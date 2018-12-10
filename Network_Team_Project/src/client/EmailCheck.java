package client;

import java.util.regex.Pattern;

/**
 * This class is for email validation.
The RE program should be notified by email when the type capsule is open.
So it is important to get a valid e-mail address when registering.
 * 
 * 
 * @author ������
 *
 */

public class EmailCheck {
	public static void main(String[] args) {
		
	}


	public static boolean isEmail(String email) {
		boolean check;
		
		if (email == null) //�̸����� �Էµ��� �ʾ��� ��
			return false;
	
		check = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
		return check; //�̸����� �ùٸ� ���� : true, �ùٸ��� ���� ���� : false �� ��ȯ
	}
}