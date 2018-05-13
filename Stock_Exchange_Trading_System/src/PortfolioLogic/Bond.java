package PortfolioLogic;

public class Bond extends Security {

	private int loan_Amount;
	private double interest;
	
	public Bond(String name_,int loan_Amount,double interest) {
		super(name_);
		this.loan_Amount=loan_Amount;
		this.interest=interest;
	}

	public int getLoan_Amount() {
		return loan_Amount;
	}

	public void setLoan_Amount(int loan_Amount) {
		this.loan_Amount = loan_Amount;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	@Override
	public String toString() {
		String str="";//=super.toString();
		return str+" Type: Bond loan_Amount=" + loan_Amount + ", interest=" + interest ;
	}
	

}
