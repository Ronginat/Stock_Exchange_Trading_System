package SecuritiesLogic;

public class Share extends Security {

	private int current_value;
	
	public Share(String name_,int current_value) {
		super(name_);
		this.current_value=current_value;
	}

	public int getCurrent_value() {
		return current_value;
	}

	public void setCurrent_value(int current_value) {
		this.current_value = current_value;
	}

	@Override
	public String toString() {
		String str=super.toString();
		return str+" Type: Share current_value=" + current_value;
	}
	

}
