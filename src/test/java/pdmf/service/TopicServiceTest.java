package pdmf.service;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdmf.model.TopicKey;
import pdmf.model.TopicRec;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class TopicServiceTest extends TestHelper {

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
		setupTopicTestData1();
		setupTopicTestData2();
	}

	TopicService topicService = new TopicService();

	Integer version = 1;
	String productName = "ois";
	String topicName = "customer";
	String processName = "crud";
	Integer seq = 0;
	String operationNameADD = "add";
	Integer operationSeq = 0;

	@Test
	public void getList() {
		List<TopicRec> list = topicService.list(version, productName);
		Assert.assertEquals(4, list.size());
	}

	@Test
	public void getList_Fail() {
		List<TopicRec> list = topicService.list(42, productName);
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void getList_Fail2() {
		List<TopicRec> list = topicService.list(version, productName + "NO");
		Assert.assertEquals(0, list.size());
	}

	@Test
	public void get() {
		topicService.remove(version, productName, topicName, "test");
		TopicRec rec = topicService.get(version, productName, topicName);
		Assert.assertTrue(rec.dltusr != null);
	}

	@Test
	public void getFail() {
		TopicRec rec = topicService.get(version, productName, topicName);
		Assert.assertEquals("customer", rec.key.topicName);
	}

	@Test
	public void exist() {
		TopicKey key = new TopicKey(version, productName, topicName);
		TopicRec rec = new TopicRec(key, null, null, null);
		Boolean exists = topicService.exists(rec);
		Assert.assertTrue(exists);
	}

	@Test
	public void existFail() {

		topicService.remove(version, productName, topicName, "test");

		TopicKey key = new TopicKey(version, productName, topicName);
		Boolean ok = topicService.isDeleteMarked(key);
		Assert.assertTrue(ok);
	}

	@Test
	public void storeNew() {

		topicService.remove(version, productName, topicName, "test");

		TopicKey key = new TopicKey(version, productName, topicName);
		TopicRec rec = new TopicRec(key, "aNewRecord", null, null);
		rec.shortdescr = "kort";
		topicService.store(rec, "test");
		TopicRec fetched = topicService.get(version, productName, topicName);
		Assert.assertNotNull(fetched);

	}

	@Test
	public void storeExisting() {

		TopicRec fetched = topicService.get(version, productName, topicName);
		fetched.description = "CHANGED";
		topicService.store(fetched, "test");
		fetched = topicService.get(version, productName, topicName);

		Assert.assertNotNull(fetched);
		Assert.assertEquals("CHANGED", fetched.description);

	}

	@Test
	public void removeExisting() {

		topicService.remove(version, productName, topicName, "test");

		TopicKey key = new TopicKey(version, productName, topicName);

		Boolean ok = topicService.isDeleteMarked(key);
		Assert.assertTrue(ok);
	}

	@Test
	public void removeNonExisting() {
		TopicKey key = new TopicKey(version, productName, topicName);
		topicService.remove(version, productName, topicName, "test");
		Boolean ok = topicService.isDeleteMarked(key);
		Assert.assertTrue(ok);
	}

	@Test(expected = RecordChangedByAnotherUser.class)
	public void updatedByAnotherUser() {

		TopicRec fetched1 = topicService.get(version, productName, topicName);
		fetched1.description = "CHANGED1";

		TopicRec fetched2 = topicService.get(version, productName, topicName);
		fetched2.description = "CHANGED BY THE FAST ONE";
		topicService.store(fetched2, "test");

		fetched1.description = "CHANGED BY THE SLOW ONE";
		topicService.store(fetched1, "test");

	}

}
