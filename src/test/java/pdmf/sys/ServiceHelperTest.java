package pdmf.sys;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.model.ProcessKey;
import pdmf.model.ProcessRec;
import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.model.TopicKey;
import pdmf.model.TopicRec;
import pdmf.service.support.ServiceHelper;

public class ServiceHelperTest {

	OperationKey operationKey1 = new OperationKey(1, null, null, null, null, null, null);
	OperationKey operationKey2 = new OperationKey(1, "product", null, null, null, null, null);
	OperationKey operationKey3 = new OperationKey(3, "product", "topic", null, null, null, null);
	OperationKey operationKey4 = new OperationKey(1, "product2", "topic", "process", null, null, null);
	OperationKey operationKey5 = new OperationKey(1, "product", "topic2", "process", 1, null, null);
	OperationKey operationKey6 = new OperationKey(1, "product", "topic", "process2", 1, null, 1);
	OperationKey operationKey7 = new OperationKey(1, "product", "topic", "process2", 1, "oper", null);
	OperationKey operationKey8 = new OperationKey(1, "product", "topic", "process2", 1, "oper", 1);

	ProcessKey processKey1 = new ProcessKey(1, null, null, null, null);
	ProcessKey processKey2 = new ProcessKey(1, "product", null, null, null);
	ProcessKey processKey3 = new ProcessKey(3, "product", "topic", null, null);
	ProcessKey processKey4 = new ProcessKey(3, "product", "topic", "process", null);
	ProcessKey processKey5 = new ProcessKey(3, "product", "topic", "process", 1);

	TopicKey topicKey1 = new TopicKey(1, null, null);
	TopicKey topicKey2 = new TopicKey(1, "product", null);
	TopicKey topicKey3 = new TopicKey(3, "product", "topic");

	ProductKey productKey1 = new ProductKey(1, null);
	ProductKey productKey2 = new ProductKey(1, "product");

	@Test
	public void testOperationKey() {

		try {
			OperationRec rec = null;
			ServiceHelper.validate(rec);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(null, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			operationKey1.version = null;
			ServiceHelper.validate(new OperationRec(operationKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey2, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey3, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey4, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey5, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey6, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new OperationRec(operationKey7, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate(new OperationRec(operationKey8, null, null, null));
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}
	}

	@Test
	public void testProcessKey() {

		try {
			ProcessRec rec = null;
			ServiceHelper.validate(rec);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProcessRec(processKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			processKey1.version = null;
			ServiceHelper.validate(new ProcessRec(processKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProcessRec(null, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProcessRec(processKey2, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProcessRec(processKey3, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProcessRec(processKey4, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate(new ProcessRec(processKey5, null, null, null));
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}

	}

	@Test
	public void testTopicKey() {

		try {
			TopicRec rec = null;
			ServiceHelper.validate(rec);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new TopicRec(null, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new TopicRec(topicKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			topicKey1.version = null;
			ServiceHelper.validate(new TopicRec(topicKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new TopicRec(topicKey2, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate(new TopicRec(topicKey3, null, null, null));
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}

	}

	@Test
	public void testProductAndVersionKey() {

		try {
			ProductRec rec = null;
			ServiceHelper.validate(rec);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			ServiceHelper.validate(new ProductRec(null, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate(new ProductRec(productKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			productKey1.version = null;
			ServiceHelper.validate(new ProductRec(productKey1, null, null, null));
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate(new ProductRec(productKey2, null, null, null));
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}

	}

	@Test
	public void testStrAndInt() {

		try {
			ServiceHelper.validate("fält", "3");
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}
		try {
			ServiceHelper.validate("fält", "");
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			String dta = null;
			ServiceHelper.validate("fält", dta);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}

		try {
			ServiceHelper.validate("fält", 3);
			assertTrue(true);
		} catch (IllegalArgumentException e) {
			assertTrue(false);
		}
		try {
			Integer dta = null;
			ServiceHelper.validate("fält", dta);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
