package General;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

import PortfolioInterface.PortfolioApp;
import PortfolioLogic.Portfolio;
import PortfolioLogic.Security;
import SecuritiesInterface.SecurityApp;
import SecuritiesLogic.Ask;
import SecuritiesLogic.Bond;
import SecuritiesLogic.Offer;
import SecuritiesLogic.SecurityLogic;
import SecuritiesLogic.Share;
import auth.api.WrongSecretException;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;
import bank.api.NotEnoughAssetException;
import exchange.api.DoesNotHaveThisStockException;
import exchange.api.InternalExchangeErrorException;
import exchange.api.NoSuchAccountException;
import exchange.api.NotEnoughStockException;
import exchange.api.StockNotTradedException;


public class AppMain {
	
	public static final String BANKADDRESS = "rmi://172.20.18.151/Bank";
	public static final String STOCKADDRESS = "rmi://172.20.18.151/Exchange";
	public static final int accountId = 109;
	public static final String secret = "thspPC";
	
	public static void main(String[] args) throws IOException, NotBoundException, WrongSecretException, NoSuchAccountException, NotEnoughStockException, StockNotTradedException, InternalExchangeErrorException, DoesNotHaveThisAssetException, NotEnoughAssetException, InternalServerErrorException, DoesNotHaveThisStockException, InterruptedException {		
		Scanner s = new Scanner(System.in);
		System.out.println("Welcome to Stock Exchange Traiding System");
		System.out.println();
		boolean auth = false;
		/*do{
			System.out.println("Please enter username: ");
			String userName = s.nextLine();
			System.out.println("Please enter password: ");
			String password = s.nextLine();
			if((userName.equals("XXX"))&&(password.equals("111"))){
				auth = true;
			}
			else{
				System.out.println("Wrong username or password");
				auth = false;
			}
		}while(!auth);
		*/
		
		int selectOperation;
		PortfolioApp pa;// = new PortfolioApp();
		SecurityApp sa;// = new SecurityApp();
		do{
			System.out.println("-To make securities oparations please press 1");
			System.out.println("-To manage your Portfolio please press 2");
			System.out.println("-To exit press 0");
			selectOperation = s.nextInt();
			
			switch(selectOperation){
			case 1:
				sa = new SecurityApp();
				sa.run();
				break;
			case 2:
				pa = new PortfolioApp();
				pa.run();
				break;
			case 0:
				System.out.println("Thank you and Good-Bye");
				System.exit(0);
			default:
				System.out.println("Invalid choice.");
				break;
			}
		}while(selectOperation != 0);
	}
}
