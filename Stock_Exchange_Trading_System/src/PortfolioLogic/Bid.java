package PortfolioLogic;

public class Bid extends Offer{
	
	public Bid (int id, int quantity, int price, String name_security){
		super(id, quantity, price, name_security);
	}
	
	public String toString(){
		return super.toString();
	}
}
