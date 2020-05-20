package pdmf.model;

public class User {

	public String userId;
	public Tenant currentTenant;

	public User() {
		userId = System.getProperty("user.name");
		currentTenant = new Tenant(1, "Forshaga Golv AB");
	}

	public String getUserId() {
		return userId;
	}

	public Tenant getCurrentTenant() {
		return currentTenant;
	}

}
