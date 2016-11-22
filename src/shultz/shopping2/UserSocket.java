package shultz.shopping2;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import shultz.Packets.UserPacket;
import shultz.Packets.UserPacket.UserPacketType;
import shultz.shopping2.DataHandler;
import shultz.Packets.All_InfoResponsePacket;
import shultz.Packets.CommandPacket;
import shultz.Packets.CommandPacket.CommandType;
import shultz.Packets.ListPacket;
import shultz.Packets.ListPacket.ListPacketType;
import shultz.Packets.ListResponsePacket;
import shultz.Packets.List_InfoResponsePacket;
import shultz.Packets.List_NameResponsePacket;
import shultz.Packets.ResponsePacket;
import shultz.Packets.UserResponsePacket;

@ServerEndpoint("/userSocket")
public class UserSocket {
	@OnMessage
	public void onMessage(final String message, Session clientSession) throws IOException {
		ResponsePacket packet = null;
		if (message.contains("UserPacket")) {
			UserPacket currentPacket = new UserPacket(message);
			if (currentPacket.getType() == UserPacketType.LOGIN)
				packet = login(currentPacket);
			else
				packet = signUp(currentPacket.getUsername(), currentPacket.getPassword(),
						currentPacket.getConfirmPass(), currentPacket.getType().toString());
		} else if (message.contains("ListPacket")) {
			ListPacket currentPacket = new ListPacket(message);
			if (currentPacket.getType() == ListPacketType.CREATE)
				packet = addList(currentPacket.getListname(), currentPacket.getUsername(),
						currentPacket.getType().toString());
			else if (currentPacket.getType() == ListPacketType.DELETE)
				packet = deleteList(currentPacket.getUsername(), currentPacket.getListname());
			else
				packet = insertDetails(currentPacket.getUsername(), currentPacket.getListname(),
						currentPacket.getDescription(), currentPacket.getQuantity(), currentPacket.getWeight(),
						currentPacket.getPrice());
		} else if (message.contains("Command")) {
			CommandPacket currentPacket = new CommandPacket(message);
			if (currentPacket.getCommandType() == CommandType.LIST_INFO_ALL)
				packet = new All_InfoResponsePacket(currentPacket.getUsername());
			else if (currentPacket.getCommandType() == CommandType.LIST_INFO)
				packet = new List_InfoResponsePacket(currentPacket.getUsername(), currentPacket.getListName());
			else
				packet = new List_NameResponsePacket(currentPacket.getUsername());
		}
		clientSession.getBasicRemote().sendText(packet.serialize());

	}

	private ResponsePacket insertDetails(String username, String listname, String itemDescription, int quantity,
			double weight, double price) {
		ListResponsePacket response = new ListResponsePacket(listname, username, false, "That item already exists",
				"ItemPage", "AddItem");
		if (!itemDescription.isEmpty() && itemDescription != null) {
			response.setSuccess(true);
			response.setErrorMessage("");
			handleItemAddition(itemDescription, DataHandler.getList_ID(DataHandler.getUserID(username), listname),
					price, weight, quantity);
		}
		return response;
	}

	private void handleItemAddition(String itemDescription, int list_ID, double price, double weight, int quantity) {
		if (!itemExists(itemDescription, list_ID)) {
			handleDescription(itemDescription, list_ID);
			handleItemDetails(itemDescription, list_ID, price, weight, quantity);
		}
	}

	private void handleItemDetails(String itemDescription, int list_ID, double price, double weight, int quantity) {
		DataHandler.executeUpdate("UPDATE listDetails SET price=" + price + " WHERE list_ID =" + list_ID
				+ " AND description = \"" + itemDescription + "\"");
		DataHandler.executeUpdate("UPDATE listDetails SET quantity=" + quantity + " WHERE list_ID =" + list_ID
				+ " AND description = \"" + itemDescription + "\"");
		DataHandler.executeUpdate("UPDATE listDetails SET weight=" + weight + " WHERE list_ID =" + list_ID
				+ " AND description = \"" + itemDescription + "\"");
	}

	private boolean itemExists(String item, int list_ID) {
		boolean exists = false;
		ResultSet results = DataHandler
				.executeQuery("SELECT * From listDetails WHERE description=\"" + item + "\" AND list_ID=" + list_ID);
		try {
			exists = results.next();
			results.close();
		} catch (SQLException e) {
			System.out.println("Error reading results.");
		}
		return exists;
	}

	private void handleDescription(String itemDescription, int list_ID) {
		DataHandler.executeUpdate("INSERT into listDetails (list_ID, description) VALUES (" + list_ID + ", \""
				+ itemDescription + "\");");
	}

	private ResponsePacket deleteList(String username, String listName) {
		int user_ID = DataHandler.getUserID(username);
		int list_ID = DataHandler.getList_ID(user_ID, listName);
		deleteFromListDetails(list_ID);
		deleteFromUserLists(list_ID, user_ID);
		return new ListResponsePacket(listName, username, true, "", "ListPage", "Delete");
	}

	private void deleteFromListDetails(int list_ID) {
		DataHandler.executeUpdate("DELETE FROM listdetails WHERE list_ID=" + list_ID);
	}

	private void deleteFromUserLists(int list_ID, int user_ID) {
		DataHandler.executeUpdate("DELETE FROM userLists WHERE list_ID=" + list_ID + " AND user_ID=" + user_ID);
	}

	private ResponsePacket addList(String listname, String username, String previousLocation) {
		ListResponsePacket response = new ListResponsePacket(listname, username, true, "", previousLocation,
				"CreatedList");
		int user_ID = DataHandler.getUserID(username);
		if (listNameExists(listname, user_ID)) {
			response.setSuccess(false);
			response.setErrorMessage("You already have a list by the name:" + listname + ".");
		} else {
			addList(user_ID, listname);
		}
		return response;
	}

	private void addList(int user_ID, String listname) {
		DataHandler.executeUpdate(
				"INSERT into userLists (user_ID, listname) VALUES (" + user_ID + ",\"" + listname + "\");");
	}

	private boolean listNameExists(String listname, int user_ID) {
		ResultSet results = DataHandler
				.executeQuery("Select * FROM userLists WHERE listname = \"" + listname + "\" AND user_ID =" + user_ID);
		boolean exists = false;
		try {
			exists = results.next();
			results.close();
		} catch (SQLException e) {
			System.out.println("Error reading results");
		}
		return exists;
	}

	private ResponsePacket signUp(String username, String pass, String confirmPass, String previousLocation) {
		System.out.println("Made it to sign up.");
		UserResponsePacket response = new UserResponsePacket(username, true, "", previousLocation);
		try {
			if (usernameExists(username)) {
				System.out.println("Username Exists");
				response.setSuccess(false);
				response.setErrorMessage("Unfortunately, this username already exists.");
			} else if (passwordsAreEqual(pass, confirmPass)) {
				System.out.println("Creating user");
				createUser(username, pass);
			} else {
				System.out.println("Passwords don't match");
				response.setSuccess(false);
				response.setErrorMessage("Your confirmation password does not match; try again.");
			}
		} catch (SQLException e) {
			System.out.println("Unable to read from database");
			response.setSuccess(false);
			response.setErrorMessage("Unable to login.");
		}
		return response;
	}

	private void createUser(String username, String password){
		DataHandler.executeUpdate("INSERT into users (username, `password`) VALUES (\"" + username + "\",\""
				+ DataHandler.handlePassword(password) + "\");");
		System.out.println("Created the user");
	}

	private boolean usernameExists(String username) throws SQLException {
		ResultSet usernames = DataHandler
				.executeQuery("SELECT username FROM users where username = '" + username + "'");
		System.out.println("username:" + username);
		boolean exists = usernames.next();
		usernames.close();
		return exists;
	}

	private boolean passwordsAreEqual(String password, String confirmationPass) {
		return password.equals(confirmationPass);
	}

	private ResponsePacket login(UserPacket userInfo) {
		UserResponsePacket response;
		try {
			ResultSet user = getUserInformation(userInfo.getUsername());
			if (user.next()) {
				if (passwordValid(userInfo.getPassword(), user)) {
					response = new UserResponsePacket(userInfo.getUsername(), true, "", userInfo.getType().toString());
				} else
					response = new UserResponsePacket(userInfo.getUsername(), false, "Your password is incorrect.",
							userInfo.getType().toString());
			} else {
				response = new UserResponsePacket(userInfo.getUsername(), false, "That username does not exist.",
						userInfo.getType().toString());
			}
			user.close();
		} catch (SQLException e) {
			System.out.println("Unable to login.");
			response = new UserResponsePacket(userInfo.getUsername(), false, "Unable to Login",
					userInfo.getType().toString());
		}
		return response;
	}

	private ResultSet getUserInformation(String username) throws SQLException {
		Statement userNameRequest = DataHandler.getNewStatement();
		return userNameRequest.executeQuery("SELECT * FROM users where username = '" + username + "'");
	}

	private boolean passwordValid(String password, ResultSet user) throws SQLException {
		return DataHandler.handlePassword(password).equals(user.getString(3));
	}
}
