/**
 *
 */
package com.hoge;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author nakazawasugio
 *
 */
public class DiffCsvB00429Test {
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBtscKeiyakuCheck() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/DiffCsvB00429Test/btsc_keiyaku_check/exp", // -e
				"src/test/resources/com/hoge/DiffCsvB00429Test/btsc_keiyaku_check/res", // -r
				"btsb004if0102dp", // -b
				"BTSC_KEIYAKU_CHECK", // -d
				16, // -targetCol
				"BTS_B004", // -targetValue
//				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(1103, target.getMatchCount());
		Assert.assertEquals(1, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(4724, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKeiyakuErrorInfo() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/DiffCsvB00429Test/btsc_keiyaku_error_info/exp", // -e
				"src/test/resources/com/hoge/DiffCsvB00429Test/btsc_keiyaku_error_info/res", // -r
				"btsb004if0102dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				15, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(102, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(765, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

}
