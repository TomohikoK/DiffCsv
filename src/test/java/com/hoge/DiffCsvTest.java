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
	public void testIf0511() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0511/exp", // -e
				"src/test/resources/com/hoge/if0511/res", // -r
				"if0511dp", // -b
				"IF0511", // -d
				110, // -targetCol
				null, // -targetValue
				false// -l
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
				false// -l
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(50, target.getMatchCount());
		Assert.assertEquals(1, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				false// -l
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(1, target.getUnMatchCount());
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(569, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtsKKakuteiJLExt2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/bts_k_kakutei_j_l_ext2/exp", // -e
				"src/test/resources/com/hoge/bts_k_kakutei_j_l_ext2/res", // -r
				"btse004dp", // -b
				"BTS_K_KAKUTEI_J_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(606, target.getMatchCount());
		Assert.assertEquals(6, target.getUnMatchCount());
		Assert.assertEquals(69, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(2666, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(1, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscIf0101RenkeiData2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0101_renkei_data2/exp", // -e
				"src/test/resources/com/hoge/btsc_if0101_renkei_data2/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0101_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(327, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				false// -l
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
				false// -l
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(260, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscIf0404RenkeiData2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_if0404_renkei_data2/exp", // -e
				"src/test/resources/com/hoge/btsc_if0404_renkei_data2/res", // -r
				"btsb004dp", // -b
				"BTSC_IF0404_RENKEI_DATA", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(1421, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(5110, target.getMatchCount());
		Assert.assertEquals(718, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKeiyakuCheckNew() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_keiyaku_check_NEW/exp", // -e
				"src/test/resources/com/hoge/btsc_keiyaku_check_NEW/res", // -r
				"btsb004dp", // -b
				"BTSC_KEIYAKU_CHECK_NEW", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(705, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(1107, target.getNotFoundResCount());
		Assert.assertEquals(99, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(2414, target.getMatchCount());
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
				false// -l
		);
		target.exec();
		Assert.assertEquals(7, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscKSijisuJLExt() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_k_sijisu_j_l_ext/exp", // -e
				"src/test/resources/com/hoge/btsc_k_sijisu_j_l_ext/res", // -r
				"btsb004dp", // -b
				"BTSC_K_SIJISU_J_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(8, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(318, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
	}

	@Test
	public void testBtscDoujidoryoFileStatus() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status/exp", // -e
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status/res", // -r
				"btsb004dp", // -b
				"BTSC_DOUJIDORYO_FILE_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(6, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscIdoInfoList() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_ido_info_list/exp", // -e
				"src/test/resources/com/hoge/btsc_ido_info_list/res", // -r
				"btsb004dp", // -b
				"BTSC_IDO_INFO_LIST", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(0, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(30, target.getNotFoundResCount());
		Assert.assertEquals(14, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscIdoInfoList2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_ido_info_list2/exp", // -e
				"src/test/resources/com/hoge/btsc_ido_info_list2/res", // -r
				"btsb004dp", // -b
				"BTSC_IDO_INFO_LIST", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(29, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(1, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtsHikinukiL() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/bts_hikinuki_l/exp", // -e
				"src/test/resources/com/hoge/bts_hikinuki_l/res", // -r
				"btsb004dp", // -b
				"BTS_HIKINUKI_L", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(31, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(714, target.getNotFoundResCount());
		Assert.assertEquals(7, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0101() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0101/exp", // -e
				"src/test/resources/com/hoge/if0101/res", // -r
				"btsb004dp", // -b
				"IF0101", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(21, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0101Watari() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0101_watari/exp", // -e
				"src/test/resources/com/hoge/if0101_watari/res", // -r
				"btsb004dp", // -b
				"IF0101_WATARI", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(6, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0102() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0102/exp", // -e
				"src/test/resources/com/hoge/if0102/res", // -r
				"btsb004dp", // -b
				"IF0102", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(5, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(8, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0209() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0209/exp", // -e
				"src/test/resources/com/hoge/if0209/res", // -r
				"btsb004dp", // -b
				"IF0209", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0404() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0404/exp", // -e
				"src/test/resources/com/hoge/if0404/res", // -r
				"btsb004dp", // -b
				"IF0404", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(7, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(2, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0404Watari() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0404_watari/exp", // -e
				"src/test/resources/com/hoge/if0404_watari/res", // -r
				"btsb004dp", // -b
				"IF0404_WATARI", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(2, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(4, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testIf0505() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/if0505/exp", // -e
				"src/test/resources/com/hoge/if0505/res", // -r
				"btsb004dp", // -b
				"IF0505", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(1, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void testBtscPfList() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_pf_list/exp", // -e
				"src/test/resources/com/hoge/btsc_pf_list/res", // -r
				"btse003dp", // -b
				"BTSC_PF_LIST", // -d
				0, // -targetCol
				null, // -targetValue
				false// -l
		);
		target.exec();
		Assert.assertEquals(4, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(42, target.getNotFoundResCount());
		Assert.assertEquals(4, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
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
				"btse004dp", // -b
				"BTSC_BP_EXT_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(570, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(102, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

}
