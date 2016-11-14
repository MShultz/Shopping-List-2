package shultz.Packets;

public class UserResponsePacket extends ResponsePacket {
	private String previousLocation;
	private String username;
	private boolean success;
	private String errorMessage;

	public UserResponsePacket(String username, boolean success, String errorMessage, String previousLocation) {
		this.setUsername(username);
		this.setSuccess(success);
		this.setErrorMessage(errorMessage);
		this.setPreviousLocation(previousLocation);
	}

	@Override
	public String serialize() {
		return this.getClass().getName() + "\n" + username + "\n" + success + "\n" + errorMessage + "\n" + previousLocation;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPreviousLocation() {
		return previousLocation;
	}

	public void setPreviousLocation(String previousLocation) {
		this.previousLocation = previousLocation;
	}
	

	
}
