package SecuritiesLogic;

public class Ask extends Offer{
	private int profit;
	
	public Ask (int id, int quantity, int price, String name_security, int profit){
		super(id, quantity, price, name_security);
		this.profit = profit;
	}
	
	public int getProfit (){
		return profit;
	}
	
	public void setProfit (int profit){
		this.profit = profit;
	}
	
	public String toString (){
		String str = super.toString();
		return "Ask: "+str+" "+profit;
	}
	
}
