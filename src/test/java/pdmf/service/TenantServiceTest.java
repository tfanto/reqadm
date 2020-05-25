package pdmf.service;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdmf.model.TenantKey;
import pdmf.model.TenantRec;
import pdmf.sys.Db;

public class TenantServiceTest extends TestHelper {

	Random rnd = new Random();

	@BeforeClass
	public static void beforeClass() {
		Db.setupDatabasePool();
		Db.clear();
		// setupOperationTestData1();
		// setupOperationTestData2();
	}

	@AfterClass
	public static void afterClass() {
		Db.stopDatabasePool();
	}

	@Before
	public void before() {
		Db.clear();
	}

	@After
	public void after() {
	}

	private TenantService tenantService = new TenantService();

	@Test
	public void list() {
		List<TenantRec> list = tenantService.list();
		Assert.assertNotNull(list);
	}

	@Test
	public void exists() {
		Integer i = rnd.nextInt();
		String tenantid = i.toString();
		Boolean ok = tenantService.exists(tenantid);
		Assert.assertFalse(ok);
	}

	@Test
	public void get_NonExisting() {
		Integer i = rnd.nextInt();
		String tenantid = i.toString();
		TenantRec rec = tenantService.get(tenantid);
		Assert.assertNull(rec);
	}

	@Test
	public void insert() {
		Integer i = rnd.nextInt();
		String tenantid = i.toString();
		TenantKey key = new TenantKey(tenantid);
		TenantRec rec = new TenantRec(key, "En tenant");
		tenantService.store(rec, "test");
		Boolean ok = tenantService.exists(tenantid);
		Assert.assertTrue(ok);
		tenantService.remove(tenantid);
	}

	@Test
	public void get() {
		Integer i = rnd.nextInt();
		String tenantid = i.toString();
		TenantKey key = new TenantKey(tenantid);
		TenantRec rec = new TenantRec(key, "En tenant");
		tenantService.store(rec, "test");
		TenantRec fetched = tenantService.get(tenantid);
		Assert.assertTrue(fetched.key.tenantid.equals(tenantid));
		tenantService.remove(tenantid);
	}

	@Test
	public void update() {
		Integer i = rnd.nextInt();
		String tenantid = i.toString();
		TenantKey key = new TenantKey(tenantid);
		TenantRec rec = new TenantRec(key, "En tenant");
		tenantService.store(rec, "test");
		TenantRec fetched = tenantService.get(tenantid);
		fetched.description = "apa";
		tenantService.store(fetched, "test");
		TenantRec fetchedAgain = tenantService.get(tenantid);
		Assert.assertEquals("apa", fetchedAgain.description);
		tenantService.remove(tenantid);
	}

}
