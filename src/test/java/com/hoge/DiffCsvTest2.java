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
public class DiffCsvTest2 {
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
	public void testBtscKeiyakuErrorInfo411() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_error_info411/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_error_info411/res", // -r
				"btsb004if0101dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(317, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(1, target.getNotFoundResCount());
		Assert.assertEquals(1, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscKeiyakuErrorInfo412() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_error_info412/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_error_info412/res", // -r
				"btsb004if0101dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(440, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(325, target.getNotFoundResCount());
		Assert.assertEquals(16, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscKeiyakuErrorInfo429() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_error_info429/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_error_info429/res", // -r
				"btsb004if0101dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(102, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(765, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscKeiyakuErrorInfo444() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_error_info444/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_error_info444/res", // -r
				"btsb004if0101dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(262, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(866, target.getNotFoundResCount());
		Assert.assertEquals(5, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscBpExtStatus1() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_bp_ext_status1/exp", // -e
				"src/test/resources/com/hoge/btsc_bp_ext_status1/res", // -r
				"btse008dp", // -b
				"BTSC_BP_EXT_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(570, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscBpExtStatus2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_bp_ext_status2/exp", // -e
				"src/test/resources/com/hoge/btsc_bp_ext_status2/res", // -r
				"btse008dp", // -b
				"BTSC_BP_EXT_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(569, target.getNotFoundResCount());
		Assert.assertEquals(109, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

}
