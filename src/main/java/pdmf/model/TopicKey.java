package pdmf.model;

import java.io.Serializable;

public class TopicKey implements Serializable {
	private static final long serialVersionUID = -8409617580136497812L;

	public String tenantid;
	public Integer version;
	public String productName;
	public String topicName;

	public TopicKey(String tenantid, Integer version, String productName, String topicName) {
		super();
		this.tenantid = tenantid;
		this.version = version;
		this.productName = productName;
		this.topicName = topicName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((tenantid == null) ? 0 : tenantid.hashCode());
		result = prime * result + ((topicName == null) ? 0 : topicName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TopicKey other = (TopicKey) obj;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (tenantid == null) {
			if (other.tenantid != null)
				return false;
		} else if (!tenantid.equals(other.tenantid))
			return false;
		if (topicName == null) {
			if (other.topicName != null)
				return false;
		} else if (!topicName.equals(other.topicName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}
