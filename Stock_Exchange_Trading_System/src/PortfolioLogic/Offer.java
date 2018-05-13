package PortfolioLogic;

public abstract class Offer {
	protected int id;
	protected int quantity;
	protected int price;
	protected String name_security;

	public Offer(int id, int quantity, int price, String name_security) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.name_security = name_security;
	}

	public int getId() {
		return id;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getPrice() {
		return price;
	}

	public String getName_security() {
		return name_security;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setName_security(String name_security) {
		this.name_security = name_security;
	}

	public String toString() {
		return id + " " + quantity + " " + price + " " + name_security;
	}
}
