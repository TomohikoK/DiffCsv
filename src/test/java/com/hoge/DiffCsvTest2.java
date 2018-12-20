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

	@Test
	public void btsb0880dp() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_moshikomi_info/exp", // -e
				"src/test/resources/com/hoge/btsc_moshikomi_info/res", // -r
				"btsb0880dp", // -b
				"BTSC_MOSHIKOMI_INFO", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(3, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void btsh080dp() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsh080dp/exp", // -e
				"src/test/resources/com/hoge/btsh080dp/res", // -r
				"btsh080dp", // -b
				"BTSC_YUKO_KWH_L_EXT", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(6, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void btscDoujidoryoFileTransfer() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_doujidoryo_file_transfer/exp", // -e
				"src/test/resources/com/hoge/btsc_doujidoryo_file_transfer/res", // -r
				"btsh080dp", // -b
				"BTSC_DOUJIDORYO_FILE_TRANSFER", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(168, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}

	@Test
	public void btscDoujidoryoStatusNotice() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_doujidoryo_status_notice/exp", // -e
				"src/test/resources/com/hoge/btsc_doujidoryo_status_notice/res", // -r
				"btsh003dp", // -b
				"BTSC_DOUJIDORYO_STATUS_NOTICE", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(168, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getResDup());
	}
	@Test
	public void testBtscDoujidoryoFileStatus2() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status2/exp", // -e
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status2/res", // -r
				"btsh001dp", // -b
				"BTSC_DOUJIDORYO_FILE_STATUS", // -d
				0, // -targetCol
				null, // -targetValue
				true// -l
		);
		target.exec();
		Assert.assertEquals(168, target.getMatchCount());
		Assert.assertEquals(0, target.getUnMatchCount());
		Assert.assertEquals(0, target.getNotFoundResCount());
		Assert.assertEquals(0, target.getNotFoundExpCount());
		Assert.assertEquals(0, target.getExpDup());
		Assert.assertEquals(0, target.getResDup());
	}
	@Test
	public void testBtscDoujidoryoFileStatus() throws IOException {
		DiffCsv target = new DiffCsv( //
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status/exp", // -e
				"src/test/resources/com/hoge/btsc_doujidoryo_file_status/res", // -r
				"btsh003dp", // -b
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

}
