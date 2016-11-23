package shultz.Packets;

public class ItemPacket extends RequestPacket {

	String username, listname, item, originalItem;
	int itemId, quantity;
	double weight, price;
	ActionType type;

	public ItemPacket(String data){
		deserialize(data);
	}
	@Override
	public void deserialize(String data) {
		String[] incommingData = data.split("\n");
		this.setUsername(incommingData[1]);
		this.setType(incommingData[2]);
		this.setListname(incommingData[3]);
		this.setItemId(Integer.parseInt(incommingData[4]));
		this.setItem(incommingData[5]);
		this.setQuantity(Integer.parseInt(incommingData[6]));
		this.setWeight(Double.parseDouble(incommingData[7]));
		this.setPrice(Double.parseDouble(incommingData[8]));
		this.setOriginalItem(incommingData[9]);
	}

	public String getUsername() {
		return username;
	}

	public String getOriginalItem() {
		return originalItem;
	}
	public void setOriginalItem(String originalItem) {
		this.originalItem = originalItem;
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

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(String msg) {
		if (msg.contains("Edit"))
			type = ActionType.EDIT;
		else
			type = ActionType.DELETE;
	}

	public enum ActionType {
		EDIT, DELETE;

	}

}
