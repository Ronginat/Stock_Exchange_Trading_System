package SecuritiesStorage;

import java.io.PrintWriter;
import java.util.Scanner;

public class AskInfo extends OfferInfo{
	private int profit;
	
	public AskInfo (int id, int quantity, int price, String security, int profit){
		super(id, quantity, price, security, "A");
		this.profit = profit;
	}
	
	public AskInfo(Scanner s){
		super(s,"A");
		this.profit = Integer.valueOf(s.next());
	}
	
	public int getProfit (){
		return profit;
	}
	
	public void save(PrintWriter pw){
		super.writeInto(pw);
		pw.print(String.valueOf(profit) + ",");
	}
	
	
}
