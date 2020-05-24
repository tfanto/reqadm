package pdmf.model;

public class User {

	public String userId;
	public TenantRec currentTenant;

	public User() {
		userId = System.getProperty("user.name");
	}

	public String getUserId() {
		return userId;
	}

	public void setCurrentTenant(TenantRec currentTenant) {
		this.currentTenant = currentTenant;
	}

	public TenantRec getCurrentTenant() {
		return currentTenant;
	}

}
