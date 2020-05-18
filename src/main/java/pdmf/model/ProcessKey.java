package pdmf.model;

import java.io.Serializable;
import java.util.Objects;

public class ProcessKey implements Serializable {

	private static final long serialVersionUID = 4021320549293252610L;
	public Integer version;
	public String productName;
	public String topicName;
	public String processName;
	public Integer sequence = 0;

	public ProcessKey(Integer version, String productName, String topicName, String processName, Integer sequence) {
		super();
		this.version = version;
		this.productName = productName;
		this.topicName = topicName;
		this.processName = processName;
		this.sequence = sequence;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProcessKey that = (ProcessKey) o;
		return version.equals(that.version) && productName.equals(that.productName) && topicName.equals(that.topicName) && processName.equals(that.processName)
				&& sequence.equals(that.sequence);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, productName, topicName, processName, sequence);
	}
}
