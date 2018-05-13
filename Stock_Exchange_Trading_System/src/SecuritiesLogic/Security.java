package SecuritiesLogic;

public class Security {
	
	protected String name_;

	public Security(String name_) {
		this.name_ = name_;
	}
	
	public String getName() {
		return name_;
	}

	public void setName(String name_) {
		this.name_ = name_;
	}

	@Override
	public String toString() {
		return "Securities name=" + name_ + "]";
	}
	

	
	
}
