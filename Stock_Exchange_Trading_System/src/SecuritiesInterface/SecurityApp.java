package SecuritiesInterface;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import SecuritiesLogic.SecurityLogic;
import auth.api.WrongSecretException;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;

public class SecurityApp {
	final static int accountID = 109;
	final static String secret = "thspPC";
	public void run() throws IOException, NotBoundException, WrongSecretException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException, InternalExchangeErrorException, DoesNotHaveThisAssetException, NotEnoughAssetException, InternalServerErrorException, DoesNotHaveThisStockException, InterruptedException{
		Scanner s = new Scanner(System.in);
		int selectOperation;
		SecurityLogic sl = new SecurityLogic();
		do{
			System.out.println("-To show supply and demand please press 1");
			System.out.println("-To buy shares please press 2");
			System.out.println("-To sell shares please press 3");
			System.out.println("-To update buying order please press 4");
			System.out.println("-To update selling order please press 5");
			System.out.println("-To show your open orders please press 6");
			System.out.println("-To refresh please press 7");
			System.out.println("-To show main menu please press 0");
			selectOperation = s.nextInt();
			
			switch(selectOperation){
			case 1: 
				try{
				sl.getSupplyAndDemand();
				}catch(RemoteException ex){
					System.out.println("The request didn't succeeded");
					System.out.println(ex.getMessage());
				}
				break;
			case 2:
				System.out.println("Please enter Share name: ");
				String shareNameToBuy = s.next();
				System.out.println("Please enter amount of shares: ");
				Integer amountToBuy = s.nextInt();
				System.out.println("Please enter price of shares: ");
				Integer priceToBuy = s.nextInt();
				sl.bid(secret, accountID, shareNameToBuy, amountToBuy, priceToBuy);
				break;
			case 3:
				System.out.println("Please enter Share name: ");
				String shareNameToSell = s.next();
				System.out.println("Please enter amount of shares: ");
				Integer amountToSell = s.nextInt();
				System.out.println("Please enter price of shares: ");
				Integer priceToSell = s.nextInt();
				sl.ask(secret, accountID, shareNameToSell, amountToSell, priceToSell);
				break;
			case 4:
				break;
			case 5:
				//sl.
				break;
			case 6:
				sl.displayOpenOrders();
				break;
			case 7:
				sl.checkOrdersStatus();				
				break;
			case 0:
				System.out.println("Exit from Security oparations");
				return;
			default:
				System.out.println("Invalid choice.");
			}
		}while(selectOperation != 0);
	}
}
