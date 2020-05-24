package pdmf.model;

public class User {

	public String userId;
	public TenantRec currentTenant;

	public User() {
		userId = System.getProperty("user.name");
		TenantKey key = new TenantKey("1");
		currentTenant = new TenantRec(key, "Forshaga Golv AB");
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
