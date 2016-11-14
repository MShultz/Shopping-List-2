package shultz.shopping2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class DataHandler {
	private static final String jdbcConnectionString = "jdbc:mysql://localhost/listapplication";
	static Connection con = null;

	public static void executeUpdate(String query) {
		Statement detailStatement = DataHandler.getNewStatement();
		try {
			detailStatement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Unable to insert into table.");
			e.printStackTrace();
		}
	}

	public static ResultSet executeQuery(String query) {
		Statement detailStatement = DataHandler.getNewStatement();
		ResultSet results = null;
		try {
			results = detailStatement.executeQuery(query);
		} catch (SQLException e) {
			System.out.println("Unable to query table.");
			e.printStackTrace();
		}
		return results;
	}

	public static Statement getNewStatement() {
		Statement state = null;
		try {
			state = getDatabaseConnection().createStatement();
		} catch (SQLException e) {
			System.out.println("Unable to generate statement");
		}
		return state;
	}

	private static Connection getDatabaseConnection() {
		if (con == null) {
			registerDriver();
			try {
				con = DriverManager.getConnection(jdbcConnectionString, "root", "Cuppycakecreep");
			} catch (SQLException e) {
				System.out.println("Fatal Error: Unable to connect to database.");
				e.printStackTrace();
			}
		}
		return con;
	}

	public static String handlePassword(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Unable to find the algorithm requested.");
		}
		md.update(password.getBytes());
		byte[] result = md.digest();
		return Base64.getEncoder().encodeToString(result);

	}

	private static void registerDriver() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Unable to find driver");
		}
	}

	public static int getUserID(String username) {
		int user_ID = -1;
		Statement IDRequest = DataHandler.getNewStatement();
		try {
			ResultSet id = IDRequest.executeQuery("SELECT user_ID from users WHERE username = \"" + username + "\"");
			if (id.next())
				user_ID = id.getInt(1);
			id.close();
		} catch (SQLException e) {
			System.out.println("Unable to get user ID.");
			e.printStackTrace();
		}
		return user_ID;
	}

	public static int getList_ID(int user_ID, String listname) {
		int list_ID = -1;
		Statement IDRequest = DataHandler.getNewStatement();
		try {
			ResultSet id = IDRequest.executeQuery("SELECT list_ID from userLists WHERE user_ID = " + user_ID
					+ " AND listname = \"" + listname + "\"");
			if (id.next())
				list_ID = id.getInt(1);
			id.close();
		} catch (SQLException e) {
			System.out.println("Unable to get list ID.");
			e.printStackTrace();
		}
		return list_ID;
	}
}