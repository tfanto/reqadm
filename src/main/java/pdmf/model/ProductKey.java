package pdmf.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductKey implements Serializable {
	private static final long serialVersionUID = 6361881510351353199L;

	public Integer version;
	public String productName;

	public ProductKey(Integer version, String productName) {
		super();
		this.version = version;
		this.productName = productName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ProductKey that = (ProductKey) o;
		return version.equals(that.version) && productName.equals(that.productName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, productName);
	}

}
