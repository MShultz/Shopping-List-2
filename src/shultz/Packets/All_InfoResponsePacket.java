package shultz.Packets;

import shultz.Objects.UserLists;

public class All_InfoResponsePacket extends ResponsePacket {

	private String username;
	private UserLists userLists;
	
	public All_InfoResponsePacket(String username){
		this.setUsername(username);
		this.userLists = new UserLists(username);
	}
	@Override
	public String serialize() {
		return this.getClass().getName() + "\n" + userLists.serialize();
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public UserLists getUserLists(){
		return this.userLists;
	}

}
