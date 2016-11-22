package shultz.Packets;

import shultz.Objects.UserListNames;

public class List_NameResponsePacket extends ResponsePacket {

	private String username;
	private UserListNames listNames;
	
	public List_NameResponsePacket(String username) {
		this.setUsername(username);
		listNames = new UserListNames(username);
	}
	@Override
	public String serialize() {
		String data = this.getClass().getName() + "\n" + username + "\n" + listNames.getListNames().size() + "\n";
		for(String listName: listNames.getListNames()){
			data += listName + "\n";
		}
		return data;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
