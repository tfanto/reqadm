package pdmf.model;

public class Tenant {

	private Integer id;
	private String tenantName;

	public Tenant(Integer id, String tenantName) {
		super();
		this.id = id;
		this.tenantName = tenantName;
	}

	public Integer getId() {
		return id;
	}


	public String getTenantName() {
		return tenantName;
	}

	
}
