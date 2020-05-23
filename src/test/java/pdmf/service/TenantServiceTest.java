package pdmf.service;

import java.util.List;
import java.util.UUID;

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
		String tenantid = UUID.randomUUID().toString();
		Boolean ok = tenantService.exists(tenantid);
		Assert.assertFalse(ok);
	}

	@Test
	public void get_NonExisting() {
		String tenantid = UUID.randomUUID().toString();
		TenantRec rec = tenantService.get(tenantid);
		Assert.assertNull(rec);
	}

	@Test
	public void insert() {
		String tenantid = UUID.randomUUID().toString();
		TenantKey key = new TenantKey(tenantid);
		TenantRec rec = new TenantRec(key, "En tenant");
		tenantService.store(rec,"test");
		Boolean ok = tenantService.exists(tenantid);
		Assert.assertTrue(ok);
	}
	
	@Test
	public void get() {
		String tenantid = UUID.randomUUID().toString();
		TenantKey key = new TenantKey(tenantid);
		TenantRec rec = new TenantRec(key, "En tenant");
		tenantService.store(rec,"test");
		TenantRec fetched = tenantService.get(tenantid);
		Assert.assertTrue(fetched.key.tenantid.equals(tenantid));
	}
	

}
