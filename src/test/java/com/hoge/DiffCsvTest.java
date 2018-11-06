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
	 *
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
	 *
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
		Assert.assertEquals(16, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(1, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testIf0506() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0506/exp", // -e
				"src/test/resources/com/hoge/if0506/res/if0506", // -r
				"if0506dp", // -b
				"IF0506", // -d
				110, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(7, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testbtsc_before_calc_chk_l() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_before_calc_chk_l/exp", // -e
				"src/test/resources/com/hoge/btsc_before_calc_chk_l/res", // -r
				"btse008dp", // -b
				"BTSC_BEFORE_CALC_CHK_L", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(44, target.getMatchCount());
		Assert.assertEquals(6, target.getUnMatchCount());
		Assert.assertEquals(1, target.getNotFoundResCount());
		Assert.assertEquals(1, target.getNotFoundExpCount());
	}

	@Test
	public void testbtsc_before_calc_chk_l_detail() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_before_calc_chk_l_detail/exp", // -e
				"src/test/resources/com/hoge/btsc_before_calc_chk_l_detail/res", // -r
				"btse008dp", // -b
				"BTSC_BEFORE_CALC_CHK_L_DETAIL", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(582, target.getMatchCount());
		Assert.assertEquals(14, target.getUnMatchCount());
		Assert.assertEquals(4, target.getNotFoundResCount());
		Assert.assertEquals(1, target.getNotFoundExpCount());
	}

	@Test
	public void testbtsc_calc_chk_cnt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_calc_chk_cnt/exp", // -e
				"src/test/resources/com/hoge/btsc_calc_chk_cnt/res", // -r
				"btse008dp", // -b
				"BTSC_CALC_CHK_CNT", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(1, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testbtsc_bp_ext_status() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_bp_ext_status/exp", // -e
				"src/test/resources/com/hoge/btsc_bp_ext_status/res", // -r
				"btse008dp", // -b
				"BTSC_BP_EXT_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(570, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtsKKakuteiJLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/bts_k_kakutei_j_l_ext/exp", // -e
				"src/test/resources/com/hoge/bts_k_kakutei_j_l_ext/res", // -r
				"btse008dp", // -b
				"BTS_K_KAKUTEI_J_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(512, target.getMatchCount());
		Assert.assertEquals(57, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscIf0101RenkeiData() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0101_renkei_data/exp", // -e
				"src/test/resources/com/hoge/btsc_if0101_renkei_data/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0101_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(27, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscIf0102RenkeiData() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0102_renkei_data/exp", // -e
				"src/test/resources/com/hoge/btsc_if0102_renkei_data/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0102_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(415, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscIf0209RenkeiData() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0209_renkei_data/exp", // -e
				"src/test/resources/com/hoge/btsc_if0209_renkei_data/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0209_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(102, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscIf0404RenkeiData() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0404_renkei_data/exp", // -e
				"src/test/resources/com/hoge/btsc_if0404_renkei_data/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0404_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(260, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKeiyakuCheck() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_check/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_check/res", // -r
				"btsb004dp", // -b
				"BTSC_KEIYAKU_CHECK", // -d
				16, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(551, target.getMatchCount());
		Assert.assertEquals(5277, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKeiyakuCheckNew() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_check_NEW/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_check_NEW/res", // -r
				"btsb004dp", // -b
				"BTSC_KEIYAKU_CHECK", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(702, target.getMatchCount());
		Assert.assertEquals(127, target.getUnMatchCount());
		Assert.assertEquals(983, target.getNotFoundResCount());
		Assert.assertEquals(2, target.getNotFoundExpCount());
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
				true// -l
		);
		target.exec();
		Assert.assertEquals(26, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtsYukoKwhLEigyoExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/bts_yuko_kwh_l_eigyo_ext/exp", // -e
				"src/test/resources/com/hoge/bts_yuko_kwh_l_eigyo_ext/res", // -r
				"btsb004dp", // -b
				"BTS_YUKO_KWH_L_EIGYO_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(2414, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
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
				true// -l
		);
		target.exec();
		Assert.assertEquals(24, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
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
				true// -l
		);
		target.exec();
		Assert.assertEquals(24, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testIf0514() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0514/exp", // -e
				"src/test/resources/com/hoge/if0514/res", // -r
				"btsb004dp", // -b
				"IF0514", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(7, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKeiyakuErrorInfo() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_error_info/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_error_info/res", // -r
				"btsb004dp", // -b
				"BTSC_KEIYAKU_ERROR_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(102, target.getUnMatchCount());
		Assert.assertEquals(764, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	//	btscKSijisuJLExt
	@Test
	public void testBtscKSijisuJLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_k_sijisu_j_l_ext/exp", // -e
				"src/test/resources/com/hoge/btsc_k_sijisu_j_l_ext/res", // -r
				"btsb004dp", // -b
				"BTSC_K_SIJISU_J_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(8, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(318, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

}
