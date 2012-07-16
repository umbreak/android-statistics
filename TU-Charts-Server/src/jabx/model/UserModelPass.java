package jabx.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class UserModelPass {
	private String username;
	private String password;
	private String description;
	private String email;

	private int[] charts_denied;
	private int[] categories_denied;
	
	public UserModelPass() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	 public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	 public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public int[] getCharts_denied() {
		return charts_denied;
	}


	public void setCharts_denied(int[] charts_denied) {
		this.charts_denied = charts_denied;
	}

	public int[] getCategories_denied() {
		return categories_denied;
	}


	public void setCategories_denied(int[] categories_denied) {
		this.categories_denied = categories_denied;
	}
	
	
}
