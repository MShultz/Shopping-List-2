package shultz.Packets;

public class ItemResponsePacket extends ResponsePacket {

	boolean success;
	public ItemResponsePacket(boolean success) {
		this.setSuccess(success);
	}
	@Override
	public String serialize() {
		return this.getClass().getName() + "\n" + success;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}

}
