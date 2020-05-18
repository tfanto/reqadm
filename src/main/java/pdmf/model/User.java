package pdmf.model;

public class User {

	public String userId;
	
	public User() {
		userId = System.getProperty("user.name");
	}

	public String getUserId() {
		return userId;
	}

}
