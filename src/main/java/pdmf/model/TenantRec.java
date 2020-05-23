package pdmf.model;

public class TenantRec {

	public TenantKey key;
	public String description;

	public TenantRec(TenantKey key, String description) {
		super();
		this.key = key;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
