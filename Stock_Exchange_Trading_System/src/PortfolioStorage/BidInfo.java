package PortfolioStorage;

import java.io.PrintWriter;
import java.util.Scanner;

public class BidInfo extends OfferInfo{

	public BidInfo(int id, int quantity, int price, String security) {
		super(id, quantity, price, security, "B");
	}
	public BidInfo(Scanner s){
		super(s,"B");
	}
	
	public void save(PrintWriter pw){
		super.writeInto(pw);
	}

}
