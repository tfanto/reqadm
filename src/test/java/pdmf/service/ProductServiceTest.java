package pdmf.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdmf.model.ProductKey;
import pdmf.model.ProductRec;
import pdmf.sys.Db;
import pdmf.sys.RecordChangedByAnotherUser;

public class ProductServiceTest extends TestHelper {
	

	@BeforeClass
	public static void beforeClass() {
		Db.setupDatabasePool();
		Db.clear();
		// setupOperationTestData1();
		// setupOperationTestData2();
		setupDataForProductTests();
	}

	@AfterClass
	public static void afterClass() {
		Db.stopDatabasePool();
	}

	@Before
	public void before() {
		Db.clear();
		// setupOperationTestData1();
		// setupOperationTestData2();
		setupDataForProductTests();
	}

	@After
	public void after() {
	}

	private ProductService productService = new ProductService();

	Integer version = 1;
	String productName = "ois";
	String topicName = "customer";
	String processName = "crud";
	Integer seq = 0;
	String operationNameADD = "add";
	Integer operationSeq = 0;

	@Test
	public void list() {
		List<String> list = productService.list();
		Assert.assertNotNull(list);
	}

	@Test
	public void listByName() {
		List<ProductRec> list = productService.list("ois");
		Assert.assertNotNull(list);
	}

	@Test
	public void listByNameFAIL() {
		List<ProductRec> list = productService.list("oisNOWAY");
		Assert.assertNotNull(list);
	}

	@Test
	public void listByNameAndVersion() {
		List<ProductRec> list = productService.list("ois", 1);
		Assert.assertNotNull(list);
	}

	@Test
	public void listByNameAndVersionFAIL() {
		List<ProductRec> list = productService.list("ois", 134);
		Assert.assertNotNull(list);
	}

	@Test
	public void getList() {
		List<ProductRec> recs = productService.list(version);
		Assert.assertEquals(2, recs.size());
	}

	@Test
	public void getList_Fail() {
		List<ProductRec> recs = productService.list(33);
		Assert.assertEquals(0, recs.size());
	}

	@Test
	public void get() {
		ProductRec rec = productService.get(version, productName);
		Assert.assertNotNull(rec);
	}

	@Test
	public void getFail() {
		ProductRec rec = productService.get(345, productName);
		Assert.assertEquals(null, rec);
	}

	@Test
	public void exist() {

		Boolean exists = productService.exists(1, productName);
		Assert.assertTrue(exists);
	}

	@Test
	public void existFail() {
		productService.remove(42, productName, "test");
		Boolean exists = productService.exists(42, productName);
		Assert.assertFalse(exists);
	}

	@Test
	public void storeNew() {

		ProductKey key = new ProductKey(tenant, version, productName+"NEW");
		ProductRec rec = new ProductRec(key, "aNewRecord", null, null);
		rec.shortdescr = "hepp";
		productService.store(rec, "test");
		ProductRec fetched = productService.get(version, productName+"NEW");
		Assert.assertNotNull(fetched);
	}

	@Test
	public void storeExisting() {

		ProductRec fetched = productService.get(version, productName);
		Assert.assertEquals("ois longdescr", fetched.description);
		fetched.description = "CHANGED";
		productService.store(fetched, "test");
		fetched = productService.get(version, productName);
		Assert.assertNotNull(fetched);
		Assert.assertEquals("CHANGED", fetched.description);

	}

	@Test
	public void removeExisting() {
		ProductKey key = new ProductKey(tenant, version, productName);
		ProductRec rec = new ProductRec(key, "aNewRecord2", null, null);
		rec.chgnbr = 0;
		productService.store(rec, "test");

		ProductRec fetched = productService.get(version, productName);
		Assert.assertEquals("aNewRecord2", fetched.description);
		productService.remove(version, productName, "test");

		ProductRec deleteMarked =   productService.get(version, productName);
		Assert.assertTrue(deleteMarked.dltusr != null);
	}

	@Test
	public void removeNonExisting() {
		productService.remove(171, productName, "test");
		Boolean exists = productService.exists(171, productName);
		Assert.assertFalse(exists);
	}

	@Test
	public void simpleQuery() {

		List<Map<String, Object>> rs = Db.simpleQuery("select * from product");
		assertNotNull(rs);

	}

	@Test(expected = RecordChangedByAnotherUser.class)
	public void updatedByAnotherUser() {

		ProductRec fetched1 = productService.get(version, productName);
		ProductRec fetched2 = productService.get(version, productName);
		productService.store(fetched2, "test");
		productService.store(fetched1, "test");

	}

	@Test
	public void versionTest() {

		ProductKey key = new ProductKey(tenant, version, productName);

		ProductRec rec = productService.get(key.version, key.productName);
		Boolean exists = productService.exists(version, productName);
		Assert.assertTrue(exists);

		int newVersion = version + 5;
		productService.createNewVersion(version, newVersion, productName, "test");
		productService.store(rec, "test");

	}

}
