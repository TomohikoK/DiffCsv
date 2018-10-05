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
public class DiffCsvTest {
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

	/**
	 * BTS_YUKO_KWH_L_EXT Test method for {@link com.hoge.DiffCsv#exec()}.
	 * @throws IOException
	 */
	@Test
	public void testBtsYukoKwhLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/bts_yuko_kwh_l_ext/exp", // -e
				"src/test/resources/com/hoge/bts_yuko_kwh_l_ext/res", // -r
				"btse001dp", // -b
				"BTS_YUKO_KWH_L_EXT", // -d
				110, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(3116, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}
	/**
	 * BTSC_YUKO_KWH_L_EXT Test method for {@link com.hoge.DiffCsv#exec()}.
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
				"BTS-E001", // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(3274, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(253, target.getNotFoundExpCount());
	}
	/**
	 * BTSC_YUKO_KWH_L_EXT Test method for {@link com.hoge.DiffCsv#exec()}.
	 * @throws IOException
	 */
	@Test
	public void testIf0511() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0511/exp", // -e
				"src/test/resources/com/hoge/if0511/res", // -r
				"if0511dp", // -b
				"IF0511", // -d
				110, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(17, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}
	@Test
	public void testIf0506() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0506/exp", // -e
				"src/test/resources/com/hoge/if0506/res/btsc_yuko_kwh_l_ext", // -r
				"if0506dp", // -b
				"BTSC_YUKO_KWH_L_EXT", // -d
				110, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(2, target.getUnMatchCount());
		Assert.assertEquals(4, target.getNotFoundResCount());
		Assert.assertEquals(1, target.getNotFoundExpCount());
	}
}
