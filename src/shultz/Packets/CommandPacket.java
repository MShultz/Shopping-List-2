package shultz.Packets;

public class CommandPacket extends RequestPacket {
	private CommandType commandType;
	private String username;
	private String listName;

	public CommandPacket(String data){
		deserialize(data);
	}
	@Override
	public void deserialize(String data) {
		String[] incommingData = data.split("\n");
		this.setCommandType(incommingData[1]);
		this.setUsername(incommingData[2]);
		if (this.commandType == CommandType.LIST_INFO)
			this.setListName(incommingData[3]);

	}

	public enum CommandType {
		LIST_INFO_ALL, LIST_NAME_ALL, LIST_INFO;
	}

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(String commandType) {
		if (commandType.equals("List_Info_All"))
			this.commandType = CommandType.LIST_INFO_ALL;
		else if (commandType.equals("List_Name_All"))
			this.commandType = CommandType.LIST_NAME_ALL;
		else
			this.commandType = CommandType.LIST_INFO;
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

}
