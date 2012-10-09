package models;

public class UserTokenTime {
	private final String email;
	private long lastAction;
	private long creation;
	public UserTokenTime(String email) {
		super();
		this.email = email;
		setCreation(System.currentTimeMillis());
		lastAction = System.currentTimeMillis();
	}
	public long getLastAction() {
		return lastAction;
	}
	public void updateLastAction(){
		this.lastAction=System.currentTimeMillis();
	}
	public String getEmail() {
		return email;
	}

	public long getCreation() {
		return creation;
	}
	public void setCreation(long creation) {
		this.creation = creation;
	}
	
	@Override
	public String toString() {
		return "UserTokenTime [email=" + email + ", lastAction="
				+ lastAction + "]";
	}
}
