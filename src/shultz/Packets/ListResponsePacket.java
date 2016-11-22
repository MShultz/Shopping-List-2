package shultz.Packets;

public class ListResponsePacket extends ResponsePacket {
	private String actionType;
	private String previousLocation;
	private String listname;
	private String username;
	private boolean success;
	private String errorMessage;
	
	public ListResponsePacket(String listname, String username, boolean success, String errorMessage, String previousLocation, String actionType){
		this.setActionType(actionType);
		this.setListname(listname);
		this.setUsername(username);
		this.setSuccess(success);
		this.setErrorMessage(errorMessage);
		this.setPreviousLocation(previousLocation);
	}
	@Override
	public String serialize() {
		return this.getClass().getName() + "\n" + listname + "\n" + username + "\n" + success + "\n" + errorMessage + "\n" + previousLocation + "\n" + actionType;
	}
	public String getPreviousLocation() {
		return previousLocation;
	}
	public void setPreviousLocation(String previousLocation) {
		this.previousLocation = previousLocation;
	}
	public String getListname() {
		return listname;
	}
	public void setListname(String listname) {
		this.listname = listname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
}
