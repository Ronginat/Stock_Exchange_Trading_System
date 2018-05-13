package PortfolioLogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PortfolioStorage.AskInfo;
import PortfolioStorage.BidInfo;
import PortfolioStorage.OfferInfo;
import PortfolioStorage.StorageManager;
import auth.api.WrongSecretException;
import bank.api.BankManager;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import exchange.api.ExchangeManager;

public class Portfolio {
	
	BankManager bank; 
	ExchangeManager exchange;
	private StorageManager storage;
	private Map<Integer, Offer> history = null;
	private static Portfolio portfolioInstance;
	
	public Portfolio() throws IOException, NotBoundException {
		history = new HashMap<Integer, Offer>();
		this.storage=new StorageManager();
		this.bank = (BankManager) Naming.lookup(General.AppMain.BANKADDRESS);
		this.exchange = (ExchangeManager) Naming.lookup(General.AppMain.STOCKADDRESS); 
		loadHistory();
	}
	
	public Map<Integer, Offer> getHistory(){
		return history;
	}

	public void addOffer(SecuritiesLogic.Offer offer) throws FileNotFoundException {
		loadHistory();
		Offer tmp;
		if (offer instanceof SecuritiesLogic.Bid) {
			tmp = new Bid(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security());
			
		}
		else{
			tmp = new Ask(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security(), ((SecuritiesLogic.Ask)offer).getProfit());
		}
		history.put(tmp.getId(), tmp);
		saveHistory();
	}

	public void showAssets() throws RemoteException, WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException{ 
		Set<String> assets = bank.getAssets("thspPC", 109);
		for(String s : assets)
			System.out.println(s + " quantity: " + bank.getQuantityOfAsset("thspPC", 109, s)+" value: "+calculateAssetsValue(s));
	}
	
	public double calculateAssetsValue(String assetName) throws RemoteException{
		Map <Integer, Integer> demand = exchange.getDemand(assetName);
		int amount = 0;
		int sum = 0;
		for(Map.Entry<Integer, Integer> quote : demand.entrySet()) {
			sum+=quote.getKey()*quote.getValue();
			amount+=quote.getValue();
		}
		return sum/amount;
	}
	
	public void displayHistory() throws FileNotFoundException
	{
		loadHistory();
		Iterator<?> it = history.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<?,?> pair = (Map.Entry<?,?>)it.next();
	        System.out.println(pair.getValue());
		}
	}
	
	public void loadHistory() throws FileNotFoundException
	{
	    List<OfferInfo> historylist = new LinkedList<OfferInfo>();
	    historylist=storage.loadHistory();
		for (OfferInfo offerinfo : historylist) {
			if(offerinfo instanceof BidInfo)
				history.put(offerinfo.getId(),new Bid(offerinfo.getId(),offerinfo.getQuantity(),offerinfo.getPrice(), offerinfo.getSecurity()));
			else
				history.put(offerinfo.getId(),new Ask(offerinfo.getId(),offerinfo.getQuantity(),offerinfo.getPrice(), offerinfo.getSecurity(), ((AskInfo)offerinfo).getProfit()));
		}
		
	}

	public void saveHistory() throws FileNotFoundException
	{	   
	    List<OfferInfo> historylist = new LinkedList<OfferInfo>();
	    Iterator<?> it = history.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<?,?> pair = (Map.Entry<?,?>)it.next();
			Offer offer = (Offer)pair.getValue();

			if(offer instanceof Bid){
				historylist.add(new BidInfo(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security()));
			}
			else{
				historylist.add(new AskInfo(offer.getId(), offer.getQuantity(), offer.getPrice(), offer.getName_security(), ((Ask)offer).getProfit()));
			}
		}
		storage.storeHistory(historylist);
	}
	
	public static Portfolio instance() throws IOException, NotBoundException{
		if(portfolioInstance == null){
			portfolioInstance = new Portfolio();
		}
		return portfolioInstance;
	}
	
}
