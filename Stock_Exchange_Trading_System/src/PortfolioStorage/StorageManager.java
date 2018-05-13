package PortfolioStorage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class StorageManager {

	private File file1;

	public StorageManager() throws IOException {
		file1 = new File("portOffer.txt");
		
		if (!file1.exists())
			Files.createFile(Paths.get("portOffer.txt"));
		
	}
	
	public void storeHistory(List<OfferInfo> list) throws FileNotFoundException {
		PrintWriter out = new PrintWriter(file1);
		for(OfferInfo info : list) {
			if(info.getType().equals("B")){
				((BidInfo)info).save(out);
			}
			else{
				((AskInfo)info).save(out);
			}
			out.println();
		}
		out.close();
	}

	public List<OfferInfo> loadHistory() throws FileNotFoundException {
		List<OfferInfo> list = new LinkedList<OfferInfo>();
		
		Scanner s = new Scanner(file1);
		String type;
		s.useDelimiter(",");
		
		while (s.hasNext()) {
			type = s.next();
			if(type.equals("B"))
				list.add(new BidInfo(s));
			else
				list.add(new AskInfo(s));
		}
		s.close();
		return list;
	}
}

