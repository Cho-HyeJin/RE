package server;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class Database {
	Connection con = null;
	PreparedStatement userPS = null;

	public static void main(String[] args) {
		Database redb = new Database();

		// System.out.println(redb.insertUserInfor("ChanYoung",
		// "young221718@gmail.com"));
		try {
			redb.con.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * constructor
	 */
	public Database() {
		try {
			ConnectDB();
			con.setAutoCommit(false);
			String userPSQL = "insert into user_information values(?, ?);";
			userPS = (PreparedStatement) con.prepareStatement(userPSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ConnectDB �����ͺ��̽��� �����ϴ� �޼����Դϴ�.
	 */
	public void ConnectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/re_db";
			String user = "root", passwd = "12345";
			con = (Connection) DriverManager.getConnection(url, user, passwd);
			System.out.println(con);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * insertUserInfor ���� ������ �����ͺ��̽��� �߰��ϰ� ���� �� ���� �Ǵ� �Լ��̴�.
	 * 
	 * @param name
	 *            : user's name
	 * @param email
	 *            : user's email
	 * @return if success return true, else return false
	 */
	public boolean insertUserInfor(String name, String email) {

		try {
			userPS.setString(1, name);
			userPS.setString(2, email);

			int count = userPS.executeUpdate();
			if (count != 2) {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * DisconnectDB ������ ���̽��� ������ ���� �޼����̴�.
	 */
	public void DisconnectDB() {

		try {
			if (con != null && !con.isClosed())
				con.close();
			if (userPS != null && !userPS.isClosed())
				userPS.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
