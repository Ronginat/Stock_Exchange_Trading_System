package SecuritiesStorage;

import java.io.PrintWriter;
import java.util.Scanner;

public abstract class SecurityInfo {
	
	protected String name;
	protected String type;
	
	public SecurityInfo(String name, String type){
		this.name = name;
		this.type = type;
	}
	
	public SecurityInfo(Scanner s, String type){
		this.name = s.next();
		this.type = type;
	}
	

	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public void writeInto(PrintWriter pw){
		pw.print(type + "," + name + ",");
	}

}
