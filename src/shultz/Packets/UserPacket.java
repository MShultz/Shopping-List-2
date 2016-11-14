package shultz.Packets;

public class UserPacket extends RequestPacket {
	private UserPacketType type;
	private String username;
	private String password;
	private String confirmPass;

	public UserPacket(String data) {
		deserialize(data);
	}

	@Override
	public void deserialize(String data) {
		String[] incommingData = data.split("\n");
		this.setType(incommingData[1]);
		this.setUsername(incommingData[2]);
		this.setPassword(incommingData[3]);
		if(incommingData[1].equals("Add"))
			this.setConfirmPass(incommingData[4]);
			

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPass() {
		return confirmPass;
	}

	public void setConfirmPass(String confirmPass) {
		this.confirmPass = confirmPass;
	}

	public UserPacketType getType() {
		return type;
	}

	public void setType(String strType) {
		if (strType.equals("Login"))
			this.type = UserPacketType.LOGIN;
		else
			this.type = UserPacketType.ADD;
	}

	public enum UserPacketType {
		LOGIN, ADD;

		@Override
		public String toString() {
			String str;
			if (this == UserPacketType.LOGIN) {
				str = "LoginPage";
			} else {
				str = "SignupPage";
			}
			return str;
		}
	}

}
