package pdmf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pdmf.sys.Db;

public class SearchServiceTest extends TestHelper {

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
		setupOperationTestData1();
		setupOperationTestData2();
		
	}

	@After
	public void after() {
	}

	private SearchService tenantsearchService = new SearchService();

	@Test
	public void listFail() {
		List<String> criteriaList = new ArrayList<>();
		List<Map<Object, List<String>>> list = tenantsearchService.search(criteriaList, true, true, true, true, "1");
		Assert.assertNull(list);
	}

	@Test
	public void list() {
		List<String> criteriaList = new ArrayList<>();
		criteriaList.add("assets");
		criteriaList.add("maintain assets");
		criteriaList.add("maintain");
		List<Map<Object, List<String>>> list = tenantsearchService.search(criteriaList, true, true, true, true, "1");
		Assert.assertNotNull(list);
	}

}
