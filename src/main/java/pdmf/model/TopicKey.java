package pdmf.model;

import java.io.Serializable;
import java.util.Objects;

public class TopicKey implements Serializable {
	private static final long serialVersionUID = -8409617580136497812L;

	public Integer version;
	public String productName;
	public String topicName;

	public TopicKey(Integer version, String productName, String topicName) {
		super();
		this.version = version;
		this.productName = productName;
		this.topicName = topicName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TopicKey topicKey = (TopicKey) o;
		return version.equals(topicKey.version) && productName.equals(topicKey.productName) && topicName.equals(topicKey.topicName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, productName, topicName);
	}

}
