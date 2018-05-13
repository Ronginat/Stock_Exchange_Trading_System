package SecuritiesLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PortfolioLogic.Portfolio;
import SecuritiesStorage.AskInfo;
import SecuritiesStorage.BidInfo;
import SecuritiesStorage.OfferInfo;
import SecuritiesStorage.SecurityInfo;
import SecuritiesStorage.StorageManager;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.ExchangeManager;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;
import exchange.api.Order;


public class SecurityLogic {
	
	BankManager bank; 
	ExchangeManager exchange; 
	
	private StorageManager storage;
	private Map<Integer,Offer> openOrders;
	
	public SecurityLogic(Map<Integer,Offer> openOrders) throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException {
		this.storage = new StorageManager();
		this.bank = (BankManager) Naming.lookup(General.AppMain.BANKADDRESS);
		this.exchange = (ExchangeManager) Naming.lookup(General.AppMain.STOCKADDRESS); 
		this.openOrders = load();
	}

	public SecurityLogic() throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException {
		 openOrders=new HashMap<Integer,Offer>();
		 this.storage=new StorageManager();
		 this.bank = (BankManager) Naming.lookup(General.AppMain.BANKADDRESS);
		 this.exchange = (ExchangeManager) Naming.lookup(General.AppMain.STOCKADDRESS);

	}
	
	public void getSupplyAndDemand() throws RemoteException
	{
		for(String stockName : exchange.getStockNames()) {
			
			System.out.println("Supply of stock " + stockName);
			System.out.println("Price\tAmount");
			Map<Integer, Integer> supply = exchange.getSupply(stockName);
			for(Map.Entry<Integer, Integer> quote : supply.entrySet()) {
				System.out.println(quote.getKey() + "\t" + quote.getValue());
			}
	
			System.out.println("Demand for stock " + stockName);
			System.out.println("Price\tAmount");
			Map<Integer, Integer> demand = exchange.getDemand(stockName);
			for(Map.Entry<Integer, Integer> quote : demand.entrySet()) {
				System.out.println(quote.getKey() + "\t" + quote.getValue());
			}
		}
	}
	// place a new bid = buy in the stock exchange
	public void bid(String secret, Integer accoundId, String stockName,Integer amount, Integer price) throws RemoteException, WrongSecretException, NotEnoughStockException, InternalExchangeErrorException, InternalServerErrorException, DoesNotHaveThisStockException, InterruptedException{
		try{
		bank.transferAssets(secret, accoundId, 3373, "NIS", amount*price);
		Thread.sleep(1500);
		int id = exchange.placeBid(secret, accoundId, stockName, amount, price);
		Order order = exchange.getOrderDetails(secret, accoundId, id);
		Offer tmp = new Bid(order.getId(), order.getAmount(), order.getPrice(), order.getStockName());
		openOrders.put(id,tmp);
		save();
		}catch(NotEnoughAssetException ex){
			System.out.println("You don't have enough "+ ex.getAsset() + " you need " + 
					(ex.getRequestedAmount() - ex.getCurrentAmount()) + " NIS more");
		}catch(DoesNotHaveThisAssetException ex){
			System.out.println("You don't have the asset: " + ex.getAsset());
		}catch(NoSuchAccountException ex){
			System.out.println(" No such account number");
		}catch(StockNotTradedException ex){
			System.out.println(" The stock: " + ex.getStockName() + " is not traded");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	
	////// calculate profitttt
	// place a new ask = sell in the stock exchange
	public void ask(String secret, Integer accoundId, String stockName,Integer amount, Integer price) throws RemoteException, WrongSecretException, NotEnoughStockException, InternalExchangeErrorException, InternalServerErrorException, InterruptedException{
		try{
		bank.transferAssets(secret, accoundId, 3373, stockName, amount);
		Thread.sleep(1500);
		int id = exchange.placeAsk(secret, accoundId, stockName, amount, price);
		Order order = exchange.getOrderDetails(secret, accoundId, id);
		Offer tmp = new Ask(order.getId(), order.getAmount(), order.getPrice(), order.getStockName(), 0);
		openOrders.put(id,tmp);
		save();
		}catch(NotEnoughAssetException ex){
			System.out.println("You don't have enough "+ ex.getAsset() + " you need " + 
					(ex.getRequestedAmount() - ex.getCurrentAmount()) + " NIS more");
		}catch(DoesNotHaveThisAssetException ex){
			System.out.println("You don't have the asset: " + ex.getAsset());
		}catch(NoSuchAccountException ex){
			System.out.println(" No such account number");
		}catch(StockNotTradedException ex){
			System.out.println(" The stock: " + ex.getStockName() + " is not traded");
		} catch (DoesNotHaveThisStockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	public void AddOpenOrder(Offer offer) throws FileNotFoundException
	{
		openOrders.put(offer.getId(),offer);
		save();
	}
	// check whether an open order got a match in the stock exchange
	public void checkOrdersStatus(){
		try {
			List<Integer> ordersId = exchange.getOpenOrders(General.AppMain.secret, General.AppMain.accountId);
			Iterator<Integer> it = openOrders.keySet().iterator();
			while(it.hasNext()){
				int id = it.next();
				if(!ordersId.contains(id))
					removeOpenOrder(id);
			}
		} catch (NoSuchAccountException | WrongSecretException | InternalExchangeErrorException | IOException | NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	// remove an offer from open orders and add it to the history in the portfolio logic
	public void removeOpenOrder(int offerId) throws IOException, NotBoundException
	{
		Portfolio port = Portfolio.instance();
		port.addOffer(openOrders.get(offerId));
		openOrders.remove(offerId);
		save();
	}

	public Map<Integer, Offer> load() throws FileNotFoundException
	{
		Map<Integer, Offer> orders = new HashMap<Integer, Offer>();
	    List<OfferInfo> listOrders = new LinkedList<OfferInfo>();
	    listOrders=storage.loadOpenOrders();
		for (OfferInfo offerinfo : listOrders) {
			if(offerinfo instanceof BidInfo)
				orders.put(offerinfo.getId(),new Bid(offerinfo.getId(),offerinfo.getQuantity(),offerinfo.getPrice(), offerinfo.getSecurity()));
			else
				orders.put(offerinfo.getId(),new Ask(offerinfo.getId(),offerinfo.getQuantity(),offerinfo.getPrice(), offerinfo.getSecurity(), ((AskInfo)offerinfo).getProfit()));
						
		}
		return orders;
	}

	public void save() throws FileNotFoundException
	{
		List<OfferInfo> list = new LinkedList<OfferInfo>();
	    Iterator<?> it = openOrders.entrySet().iterator();
	    while (it.hasNext()) {
			Map.Entry<?, ?> pair = (Map.Entry<?,?>)it.next();
	        
	        if (pair.getValue() instanceof Bid) {
	        	Offer offer = (Offer)pair.getValue();
	        	 list.add(new BidInfo(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security()));			
			}
	        else
	        {
	        	Offer offer = (Offer)pair.getValue();
	        	list.add(new AskInfo(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security(), ((Ask)offer).getProfit()));		
	        }
	        
	    }
	   storage.storeOpenOrders(list);	
	}
	
	public void displayOpenOrders() throws FileNotFoundException{
		
		
		
		openOrders=load();
		Iterator<?> it = openOrders.values().iterator();
		while(it.hasNext()){
			System.out.println(it.next().toString());
		}
		
	}
}
