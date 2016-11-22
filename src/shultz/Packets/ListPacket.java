package shultz.Packets;

public class ListPacket extends RequestPacket {
	private ListPacketType type;
	private String username;
	private String listname;
	private int quantity;
	private String description;
	private double price, weight;
	

	public ListPacket(String data) {
		deserialize(data);
	}

	@Override
	public void deserialize(String data) {
		String[] incommingData = data.split("\n");
		this.setType(incommingData[1]);
		this.setListname(incommingData[2]);
		this.setUsername(incommingData[3]);
		if(this.type == ListPacketType.ADDITEM){
			this.setDescription(incommingData[4]);
			this.setQuantity(Integer.parseInt(incommingData[5]));
			this.setWeight(Double.parseDouble(incommingData[6]));
			this.setPrice(Double.parseDouble(incommingData[7]));
		}
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

	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public ListPacketType getType() {
		return type;
	}

	public void setType(String strType) {
		if (strType.equals("Create"))
			this.type = ListPacketType.CREATE;
		else if (strType.equals("Delete"))
			this.type = ListPacketType.DELETE;
		else
			this.type = ListPacketType.ADDITEM;
	}

	public enum ListPacketType {
		CREATE, ADDITEM, DELETE;

		@Override
		public String toString() {
			String str;
			if (this == ListPacketType.ADDITEM) {
				str = "ItemPage";
			} else {
				str = "ListPage";
			}
			return str;
		}

	}
}
