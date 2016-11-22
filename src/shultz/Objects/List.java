package shultz.Objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import shultz.shopping2.DataHandler;

public class List {
	private ArrayList<Item> listItems = new ArrayList<Item>();
	private String username, listName;
	private double totalPrice;

	public List(String username, String listName) {
		this.setUsername(username);
		this.setListName(listName);
		this.constructList();
	}

	private void constructList() {
		Statement listRequest = DataHandler.getNewStatement();
		ResultSet listItems;
		try {
			listItems = listRequest.executeQuery("SELECT * FROM listDetails WHERE list_ID ="
					+ DataHandler.getList_ID(DataHandler.getUserID(this.username), this.getListName()));
			while (listItems.next()) {
				addItem(new Item(listItems.getInt(2),listItems.getString(3), listItems.getInt(4), listItems.getDouble(5),
						listItems.getDouble(6)));
				totalPrice += listItems.getDouble(6);
			}
		} catch (SQLException e) {
			System.out.println("Unable to get list items");
		}
	}
	
	public String serialize(){
		String list = listName + "\n" + listItems.size() + "\n";
		for(Item item: listItems){
			list += item.serialize();
		}
		list += totalPrice + "\n";
		return list;
	}

	public void addItem(Item item) {
		listItems.add(item);
	}

	public ArrayList<Item> getListItems() {
		 return listItems;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}
	
	public double getTotalPrice(){
		return totalPrice;
	}
}
