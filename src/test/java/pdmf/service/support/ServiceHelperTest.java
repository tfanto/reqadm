package pdmf.service.support;

import org.junit.Assert;
import org.junit.Test;

public class ServiceHelperTest {

	@Test
	public void testEnsureLength() {
		String str = null;
		String result = ServiceHelper.ensureStringLength(str, 5);
		Assert.assertNull(result);
	}

	@Test
	public void testEnsureLength2() {
		String str = "hej";
		String result = ServiceHelper.ensureStringLength(str, 5);
		Assert.assertEquals(3, result.length());
	}
	
	@Test
	public void testEnsureLength3() {
		String str = "hej";
		String result = ServiceHelper.ensureStringLength(str, 3);
		Assert.assertEquals(3, result.length());
	}
	
	@Test
	public void testEnsureLength4() {
		String str = "hejvaddetgår";
		String result = ServiceHelper.ensureStringLength(str, 3);
		Assert.assertEquals(3, result.length());
	}

	
}
