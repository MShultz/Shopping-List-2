package shultz.Packets;

import shultz.Objects.List;

public class List_InfoResponsePacket extends ResponsePacket {

	private String username, listname;
	private List list;
	
	public List_InfoResponsePacket(String username, String listname) {
		this.setUsername(username);
		this.setListname(listname);
		list = new List(username, listname);
		
	}
	@Override
	public String serialize() {
		return this.getClass().getName() + "\n" + list.serialize();
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getListname() {
		return listname;
	}
	public void setListname(String listname) {
		this.listname = listname;
	}
	
	

}
