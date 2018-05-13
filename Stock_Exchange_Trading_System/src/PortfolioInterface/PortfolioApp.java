package PortfolioInterface;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;
import PortfolioLogic.*;
import auth.api.WrongSecretException;
import bank.api.DoesNotHaveThisAssetException;
import bank.api.InternalServerErrorException;

public class PortfolioApp {
	public void run() throws IOException, NotBoundException, WrongSecretException, InternalServerErrorException, DoesNotHaveThisAssetException{
		Scanner s = new Scanner(System.in);
		int selectOperation;
		Portfolio pf = Portfolio.instance();
		do{
			System.out.println("-To show your history offers please press 1");
			System.out.println("-To show all of your assets please press 2");
			System.out.println("-To show main menu please press 0");
			selectOperation = s.nextInt();
			switch(selectOperation){
			case 1: 
				pf.getHistory();
				break;
			case 2:
				pf.showAssets();
				break;
			case 0:
				System.out.println("Exit from Portfolio managment");
				return;
			default:
				System.out.println("Invalid choice.");
			}
		}while(selectOperation != 0);
		s.close();
	}
}
