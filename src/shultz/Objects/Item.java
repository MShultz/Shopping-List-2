package shultz.Objects;

public class Item {
	String description;
	double weight;
	int quantity;
	int item_ID;
	double price;

	public Item(int item_ID, String description, int quantity, double weight, double price) {
		this.setDescription(description);
		this.setQuantity(quantity);
		this.setWeight(weight);
		this.setPrice(price);
		this.setItem_ID(item_ID);
	}

	public String serialize() {
		return item_ID + "\n" + description + "\n" + quantity + "\n" + weight + "\n" + price + "\n";
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getItem_ID() {
		return item_ID;
	}

	public void setItem_ID(int item_ID) {
		this.item_ID = item_ID;
	}

}
