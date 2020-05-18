package pdmf.service;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdmf.model.OperationKey;
import pdmf.model.OperationRec;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class OperationServiceTest extends TestHelper {

	@BeforeClass
	public static void beforeClass() {
		Db.setupDatabasePool();
	}

	@AfterClass
	public static void afterClass() {
		Db.stopDatabasePool();
	}

	@Before
	public void before() {
		Db.clear();
		setupOperationTestData1();
		setupOperationTestData2();
	}

	OperationService operationService = new OperationService();

	Integer version = 1;
	String productName = "ois";
	String topicName = "customer";
	String processName = "crud";
	Integer seq = 0;
	String operationNameADD = "add";
	Integer operationSeq = 0;

	@Test
	public void getList() {
		List<OperationRec> operations = operationService.list(version, productName, topicName, processName, seq);
		Assert.assertTrue(operations.size() > 0);
	}

	@Test
	public void getList_Fail() {
		List<OperationRec> operations = operationService.list(version, productName, topicName, processName, 33);
		Assert.assertEquals(0, operations.size());
	}

	@Test
	public void getList_Fail2() {
		List<OperationRec> operations = operationService.list(version, productName + "NO", topicName, processName, seq);
		Assert.assertEquals(0, operations.size());
	}
	
	@Test
	public void getList2() {
		List<OperationRec> operations = operationService.list(version, productName , topicName, processName);
		Assert.assertEquals(3, operations.size());
	}
	
	@Test
	public void getList2_FAIL() {
		List<OperationRec> operations = operationService.list(version, productName + "NOWAY" , topicName, processName);
		Assert.assertEquals(0, operations.size());
	}

	@Test
	public void get() {
		OperationRec operation = operationService.get(version, productName, topicName, processName, seq, "YADAYDYA", operationSeq);
		Assert.assertEquals(null, operation);
	}

	@Test
	public void getFail() {
		OperationRec operation = operationService.get(version, productName, topicName, processName, seq, operationNameADD, operationSeq);
		Assert.assertEquals("customer", operation.key.topicName);
	}

	@Test
	public void exist() {
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, operationNameADD, operationSeq);
		OperationRec rec = new OperationRec(key, null, null, null);
		Boolean exists = operationService.exists(rec);
		Assert.assertTrue(exists);
	}

	@Test
	public void existFail() {
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "yadayada", operationSeq);
		OperationRec rec = new OperationRec(key, null, null, null);
		Boolean exists = operationService.exists(rec);
		Assert.assertFalse(exists);
	}


	@Test
	public void storeNew() {
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "yadayada", operationSeq);
		OperationRec rec = new OperationRec(key, "aNewRecord", null, null);
		rec.shortdescr = "ya";
		operationService.store(rec, "test");
		OperationRec fetched = operationService.get(version, productName, topicName, processName, seq, "yadayada", operationSeq);
		Assert.assertNotNull(fetched);

	}

	@Test
	public void storeExisting() {
		operationService.remove(version, productName, topicName, processName, seq, "newRecord2", operationSeq, "test");
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "newRecord2", operationSeq);
		OperationRec rec = new OperationRec(key, "aNewRecord2", null, null);
		rec.shortdescr = "kort rackare";
		operationService.store(rec, "test");
		OperationRec fetched = operationService.get(version, productName, topicName, processName, seq, "newRecord2", operationSeq);
		Assert.assertEquals("aNewRecord2", fetched.description);
		fetched.description = "CHANGED";
		operationService.store(fetched, "test");
		fetched = operationService.get(version, productName, topicName, processName, seq, "newRecord2", operationSeq);

		Assert.assertNotNull(fetched);
		Assert.assertEquals("CHANGED", fetched.description);

	}

	@Test
	public void removeExisting() {
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "newRecord42", operationSeq);
		OperationRec rec = new OperationRec(key, "aNewRecord2", null, null);
		operationService.store(rec, "test");
		OperationRec fetched = operationService.get(version, productName, topicName, processName, seq, "newRecord42", operationSeq);
		Assert.assertEquals("aNewRecord2", fetched.description);
		operationService.remove(version, productName, topicName, processName, seq, "newRecord42", operationSeq, "test");

		OperationKey key2 = new OperationKey(version, productName, topicName, processName, seq, "newRecord42", operationSeq);

		OperationRec lookup = new OperationRec(key2, null, null, null);
		Assert.assertFalse(lookup.dltusr != null);
	}

	@Test
	public void removeNonExisting() {
		operationService.remove(version, productName, topicName, processName, seq, "newRecord424", operationSeq, "test");
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "newRecord424", operationSeq);
		OperationRec lookup = new OperationRec(key, null, null, null);
		Boolean exists = operationService.exists(lookup);
		Assert.assertFalse(exists);
	}

	@Test(expected = RecordChangedByAnotherUser.class)
	public void updatedByAnotherUser() {

		operationService.remove(version, productName, topicName, processName, seq, "newRecord2", operationSeq, "test");
		OperationKey key = new OperationKey(version, productName, topicName, processName, seq, "newRecord2", operationSeq);
		OperationRec rec = new OperationRec(key, "aNewRecord2", null, null);
		operationService.store(rec, "test");
		OperationRec fetched1 = operationService.get(version, productName, topicName, processName, seq, "newRecord2", operationSeq);
		Assert.assertEquals("aNewRecord2", fetched1.description);
		fetched1.description = "CHANGED1";

		OperationRec fetched2 = operationService.get(version, productName, topicName, processName, seq, "newRecord2", operationSeq);
		Assert.assertEquals("aNewRecord2", fetched2.description);
		fetched2.description = "CHANGED BY THE FAST ONE";
		operationService.store(fetched2, "test");

		fetched1.description = "CHANGED BY THE SLOW ONE";
		operationService.store(fetched1, "test");

	}

}
