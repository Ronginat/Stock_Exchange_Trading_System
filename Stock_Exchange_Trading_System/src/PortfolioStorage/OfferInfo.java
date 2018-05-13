package PortfolioStorage;

import java.io.PrintWriter;
import java.util.Scanner;

public abstract class OfferInfo {
	protected int id;
	protected int quantity;
	protected int price;
	protected String securityName;
	protected String type;

	public OfferInfo(int id, int quantity, int price, String securityName, String type) {
		this.id = id;
		this.quantity = quantity;
		this.price = price;
		this.securityName = securityName;
		this.type = type;
	}
	
	public OfferInfo(Scanner s, String type){
		this.type = type;
		this.id = Integer.valueOf(s.next());
		this.quantity = Integer.valueOf(s.next());
		this.price = Integer.valueOf(s.next());
		securityName = s.next();
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

	public String getSecurity() {
		return securityName;
	}
	
	public String getType(){
		return type;
	}

	public void writeInto(PrintWriter pw){
		pw.print(type + "," + String.valueOf(id) + "," + String.valueOf(quantity) + "," + String.valueOf(price) + "," + securityName);
	}

}
