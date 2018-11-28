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
public class DiffCsvIf0507Test {
	static boolean LOG_OUT = false;

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
	public void testBtscInfomation() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_infomation/exp", // -e
				"src/test/resources/com/hoge/btsc_infomation/res", // -r
				"btse008dp", // -b
				"BTSC_INFOMATION", // -d
				0, // -targetCol
				null, // -targetValue
				LOG_OUT// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(2, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscShijisuChkRes() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_shijisu_chk_res/exp", // -e
				"src/test/resources/com/hoge/btsc_shijisu_chk_res/res", // -r
				"btsb004dp", // -b
				"BTSC_SHIJISU_CHK_RES", // -d
				0, // -targetCol
				null, // -targetValue
				LOG_OUT// -l
		);
		target.exec();
		Assert.assertEquals(20, target.getMatchCount());
		Assert.assertEquals(4, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscShijisuIf() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_shijisu_if/exp", // -e
				"src/test/resources/com/hoge/btsc_shijisu_if/res", // -r
				"btsb004dp", // -b
				"BTSC_SHIJISU_IF", // -d
				0, // -targetCol
				null, // -targetValue
				LOG_OUT// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(24, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	/**
	 * BTSC_YUKO_KWH_L_EXT Test method for {@link com.hoge.DiffCsv#exec()}.
	 *
	 * @throws IOException
	 */
	@Test
	public void testBtscYukoKwhLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_yuko_kwh_l_ext/exp", // -e
				"src/test/resources/com/hoge/btsc_yuko_kwh_l_ext/res", // -r
				"btse001dp", // -b
				"BTSC_YUKO_KWH_L_EXT", // -d
				110, // -targetCol
				null, // -targetValue
				LOG_OUT// -l
		);
		target.exec();
		Assert.assertEquals(6, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscYukoKwhMonthLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_yuko_kwh_month_l_ext/exp", // -e
				"src/test/resources/com/hoge/btsc_yuko_kwh_month_l_ext/res", // -r
				"btsb004dp", // -b
				"BTSC_YUKO_KWH_MONTH_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				LOG_OUT// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(4, target.getUnMatchCount());
		Assert.assertEquals(22, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
	}

}
