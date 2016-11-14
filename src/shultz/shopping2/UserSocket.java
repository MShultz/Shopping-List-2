package shultz.shopping2;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import shultz.Packets.UserPacket;
import shultz.Packets.UserPacket.UserPacketType;
import shultz.shopping2.DataHandler;
import shultz.Packets.ResponsePacket;
import shultz.Packets.UserResponsePacket;

@ServerEndpoint("/userSocket")
public class UserSocket {
	@OnMessage
	public void onMessage(final String message, Session clientSession) throws IOException {
		ResponsePacket packet = null;
		System.out.println(message);
		if(message.contains("UserPacket")){
			UserPacket currentPacket = new UserPacket(message);
			if(currentPacket.getType() == UserPacketType.LOGIN)
				packet  = login(currentPacket);
			else
				packet = signUp(currentPacket.getUsername(), currentPacket.getPassword(), currentPacket.getConfirmPass(), currentPacket.getType().toString());
		}
		clientSession.getBasicRemote().sendText(packet.serialize());

	}
	
	private ResponsePacket signUp(String username, String pass, String confirmPass, String previousLocation) {
		UserResponsePacket response  = new UserResponsePacket(username, true,"", previousLocation);
		try {
			if (usernameExists(username)) {
				response.setSuccess(false);
				response.setErrorMessage("Unfortunately, this username already exists.");
			} else if (passwordsAreEqual(pass, confirmPass)) {
				createUser(username, pass);
			} else {
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
	private void createUser(String username, String password) throws SQLException {
		DataHandler.executeUpdate("INSERT into users (username, `password`) VALUES (\"" + username + "\",\""
				+ DataHandler.handlePassword(password) + "\");");
	}

	private boolean usernameExists(String username) throws SQLException {
		ResultSet usernames = DataHandler.executeQuery("SELECT username FROM users where username = '" + username + "'");
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
					response = new UserResponsePacket(userInfo.getUsername(), false, "Your password is incorrect.", userInfo.getType().toString());
			} else {
				response = new UserResponsePacket(userInfo.getUsername(), false, "That username does not exist.", userInfo.getType().toString());
			}
			user.close();
		} catch (SQLException e) {
			System.out.println("Unable to login.");
			response = new UserResponsePacket(userInfo.getUsername(), false, "Unable to Login", userInfo.getType().toString());
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
