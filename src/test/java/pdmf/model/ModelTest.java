package pdmf.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class ModelTest {

	OperationKey operationKey1 = new OperationKey(1,1, "product", "topic", "process", 1, "operation", 1);
	OperationKey operationKey11 = new OperationKey(1,1, null, "topic", "process", 1, "operation", 1);
	OperationKey operationKey111 = new OperationKey(1,1, "product", null, "process", 1, "operation", 1);
	OperationKey operationKey1111 = new OperationKey(1,1, "product", "topic", null, 1, "operation", 1);
	OperationKey operationKey2 = new OperationKey(1,1, "product", "topic", "process", 1, "operation", 1);
	OperationKey operationKey3 = new OperationKey(1,3, "product", "topic", "process", 1, "operation", 1);
	OperationKey operationKey4 = new OperationKey(1,1, "product2", "topic", "process", 1, "operation", 1);
	OperationKey operationKey5 = new OperationKey(1,1, "product", "topic2", "process", 1, "operation", 1);
	OperationKey operationKey6 = new OperationKey(1,1, "product", "topic", "process2", 1, "operation", 1);
	OperationKey operationKey7 = new OperationKey(1,1, "product", "topic", "process2", 1, null, 1);
	ProcessKey processKey1 = new ProcessKey(1,1, "product", "topic", "process", 1);
	ProcessKey processKey2 = new ProcessKey(1,1, "product", "topic", "process", 1);
	ProcessKey processKey3 = new ProcessKey(1,3, "product", "topic", "process", 1);
	TopicKey topicKey1 = new TopicKey(1,1, "product", "topic");
	TopicKey topicKey2 = new TopicKey(1,1, "product", "topic");
	TopicKey topicKey3 = new TopicKey(1,3, "product", "topic");
	ProductKey productKey1 = new ProductKey(1,1, "product");
	ProductKey productKey2 = new ProductKey(1,1, "product");
	ProductKey productKey3 = new ProductKey(1,3, "product");

	@Test
	public void testHashes() {
		int operation1hash = operationKey1.hashCode();
		int operation2hash = operationKey2.hashCode();
		int operation3hash = operationKey3.hashCode();
		assertEquals(operation1hash, operation2hash);
		assertNotEquals(operation1hash, operation3hash);

		int process1hash = processKey1.hashCode();
		int process2hash = processKey2.hashCode();
		int process3hash = processKey3.hashCode();
		assertEquals(process1hash, process2hash);
		assertNotEquals(process1hash, process3hash);

		int topic1hash = topicKey1.hashCode();
		int topic2hash = topicKey2.hashCode();
		int topic3hash = topicKey3.hashCode();
		assertEquals(topic1hash, topic2hash);
		assertNotEquals(topic1hash, topic3hash);

		int product1hash = productKey1.hashCode();
		int product2hash = productKey2.hashCode();
		int product3hash = productKey3.hashCode();
		assertEquals(product1hash, product2hash);
		assertNotEquals(product1hash, product3hash);

	}

	@Test
	public void testEquals() {

		OperationKey o = null;
		ProcessKey p = null;
		TopicKey t = null;
		ProductKey pr = null;
		assertNotEquals(operationKey1, o);
		assertNotEquals(o, operationKey1);
		assertNotEquals(processKey1, p);
		assertNotEquals(topicKey1, t);
		assertNotEquals(productKey1, pr);

		assertEquals(operationKey1, operationKey2);
		assertEquals(processKey1, processKey2);
		assertEquals(topicKey1, topicKey2);
		assertEquals(productKey1, productKey2);

		assertNotEquals(operationKey1, operationKey4);
		assertNotEquals(operationKey1, operationKey5);
		assertNotEquals(operationKey1, operationKey6);
		assertNotEquals(operationKey1, operationKey7);
		assertNotEquals(operationKey1, operationKey11);
		assertNotEquals(operationKey1, operationKey111);
		assertNotEquals(operationKey1, operationKey1111);

		assertNotEquals(processKey1, processKey3);
		assertNotEquals(topicKey1, topicKey3);
		assertNotEquals(productKey1, productKey3);
	}

}
