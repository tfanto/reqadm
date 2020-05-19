package pdmf.model;

import java.io.Serializable;

public class OperationKey implements Serializable {
	private static final long serialVersionUID = -8409422351081296093L;
	public Integer tenant;
	public Integer version;
	public String productName;
	public String topicName;
	public String processName;
	public Integer sequence = 0;
	public String operationName;
	public Integer operationSequence = 0;

	public OperationKey(Integer tenant, Integer version, String productName, String topicName, String processName,
			Integer sequence, String operationName, Integer operationSequence) {
		super();
		this.tenant = tenant;
		this.version = version;
		this.productName = productName;
		this.topicName = topicName;
		this.processName = processName;
		this.sequence = sequence;
		this.operationName = operationName;
		this.operationSequence = operationSequence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operationName == null) ? 0 : operationName.hashCode());
		result = prime * result + ((operationSequence == null) ? 0 : operationSequence.hashCode());
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
		result = prime * result + ((tenant == null) ? 0 : tenant.hashCode());
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
		OperationKey other = (OperationKey) obj;
		if (operationName == null) {
			if (other.operationName != null)
				return false;
		} else if (!operationName.equals(other.operationName))
			return false;
		if (operationSequence == null) {
			if (other.operationSequence != null)
				return false;
		} else if (!operationSequence.equals(other.operationSequence))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (sequence == null) {
			if (other.sequence != null)
				return false;
		} else if (!sequence.equals(other.sequence))
			return false;
		if (tenant == null) {
			if (other.tenant != null)
				return false;
		} else if (!tenant.equals(other.tenant))
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
