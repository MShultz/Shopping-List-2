package shultz.Objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import shultz.shopping2.DataHandler;

public class UserListNames {
	private ArrayList<String> listNames = new ArrayList<String>();
	private String username;
	
	public UserListNames(String username){
		this.setUsername(username);
		this.setListNames();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setListNames(){
		int user_ID = DataHandler.getUserID(username);
		try {
			ResultSet lists = DataHandler.executeQuery("SELECT listname FROM userLists WHERE user_ID = " + user_ID);
			while(lists.next()){
				listNames.add(lists.getString(1));
			}
		} catch (SQLException e) {
			System.out.println("Unable to get lists.");
		}
	}
	
	public ArrayList<String> getListNames(){
		return listNames;
	}
	
}
