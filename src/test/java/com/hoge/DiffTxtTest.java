/**
 *
 */
package com.hoge;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.commons.cli.ParseException;
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
public class DiffTxtTest {

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
	 * Test method for {@link com.hoge.DiffTxt#main(java.lang.String[])}.
	 */
	@Test
	public final void testMain() {
		String[] testParam = { //
				"-e", "src/test/resources/com/hoge/DiffTxtData/update/exp/", //
				"-r", "src/test/resources/com/hoge/DiffTxtData/update/res", //
				"-d", "update", //
				"-l", "false", //
		};
		try {
			DiffTxt.main(testParam);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link com.hoge.DiffTxt#exec()}.
	 */
	@Test
	public final void testUpdate() {
		DiffTxt target = new DiffTxt(
				"src/test/resources/com/hoge/DiffTxtData/update/exp", //
				"src/test/resources/com/hoge/DiffTxtData/update/res", //
				"update", //
				true
				);
		try {
			target.exec();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		Assert.assertEquals(1, target.getExpMap().size());
		Assert.assertEquals(6,target.getResMap().get("20180830").size());
		Assert.assertEquals(167, target.getMatchCount());
		Assert.assertEquals(1, target.getUnMatchCount());
	}
}