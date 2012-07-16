package jabx.model;

public class UserTokenTime {
	private final String email;
	private long lastAction;
	public UserTokenTime(String email) {
		super();
		this.email = email;
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
	@Override
	public String toString() {
		return "UserTokenTime [email=" + email + ", lastAction="
				+ lastAction + "]";
	}


}
