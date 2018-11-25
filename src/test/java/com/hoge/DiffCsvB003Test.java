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
public class DiffCsvB003Test {
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
				"src/test/resources/com/hoge/DiffCsvB003Test/btsc_keiyaku_check/exp", // -e
				"src/test/resources/com/hoge/DiffCsvB003Test/btsc_keiyaku_check/res", // -r
				"btsb003dp", // -b
				"BTSC_KEIYAKU_CHECK", // -d
				16, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(4758, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(1070, target.getNotFoundResCount());
		Assert.assertEquals(1070, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscKeiyakuCheckNew() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/DiffCsvB003Test/btsc_keiyaku_check/exp", // -e
				"src/test/resources/com/hoge/DiffCsvB003Test/btsc_keiyaku_check/res", // -r
				"btsb003dp", // -b
				"BTSC_KEIYAKU_CHECK_NEW", // -d
				16, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(5814, target.getMatchCount());
		Assert.assertEquals(14, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(16, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

}
