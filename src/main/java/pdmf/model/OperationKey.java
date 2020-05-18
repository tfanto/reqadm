package pdmf.model;

import java.io.Serializable;
import java.util.Objects;

public class OperationKey implements Serializable {
	private static final long serialVersionUID = -8409422351081296093L;
	public Integer version;
	public String productName;
	public String topicName;
	public String processName;
	public Integer sequence = 0;
	public String operationName;
	public Integer operationSequence = 0;

	public OperationKey(Integer version, String productName, String topicName, String processName, Integer sequence, String operationName,
			Integer operationSequence) {
		super();
		this.version = version;
		this.productName = productName;
		this.topicName = topicName;
		this.processName = processName;
		this.sequence = sequence;
		this.operationName = operationName;
		this.operationSequence = operationSequence;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OperationKey that = (OperationKey) o;
		return version.equals(that.version) && productName.equals(that.productName) && topicName.equals(that.topicName) && processName.equals(that.processName)
				&& sequence.equals(that.sequence) && operationName.equals(that.operationName) && operationSequence.equals(that.operationSequence);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, productName, topicName, processName, sequence, operationName, operationSequence);
	}
}
