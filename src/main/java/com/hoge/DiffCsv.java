package com.hoge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opencsv.CSVReader;

public class DiffCsv {

	static Logger logger = LoggerFactory.getLogger(DiffCsv.class);
	static String DELIMITTER = "\t";
	private boolean showDetail;
	private String expect;
	private String result;
	private String batch;
	private String data;
	private int excludeTargetCol;
	private String excludeTargetValue;
	/** データ属性保管マップ **/
	private HashMap<String, DataAttr> dataAttrMap;
	// 結果カウント
	private int unMatchCount;
	private int notFoundResCount;
	private int matchCount;
	private int notFoundExpCount;

	public static void main(String[] args) throws ParseException {
		logger.info("start...");
		long startTime = (new Date()).getTime();
//		String[] testParam = { //
//				"-e", "/Volumes/GoogleDrive/マイドライブ/九電託送/Phase3/現新比較/BTS-E001/OUT", //
//				"-r",
//				"/Volumes/GoogleDrive/マイドライブ/九電託送/Phase3/現新比較/BTS-E001/comp_test/btse001dp/result/bts_yuko_kwh_l_ext", //
//				"-b", "btse001dp", //
//				"-d", "BTS_YUKO_KWH_L_EXT", //
//				"-targetCol", "110", //
//				"-targetValue", "BTS-E001", //
//				"-l", "false", //
//		};
//		args = testParam;
//		String[] testParam = { //
//				"-e", "/Users/nakazawasugio/qd-bts/ee/btse001dp/OUT", //
//				"-r", "/Users/nakazawasugio/qd-bts/ee/btse001dp/btse001dp/result/btsc_yuko_kwh_l_ext",
//				"-b", "btse001dp", //
//				"-d", "BTSC_YUKO_KWH_L_EXT", //
//				"-targetCol", "110", //
//				"-targetValue", "BTS-E001", //
//				"-l", "false", //
//		};
//		args = testParam;
		// command line
		Option expDirOption = Option.builder("e").longOpt("expect").hasArg().required(true).type(String.class)
				.desc("expect direcotory base path(upper on OUT)").build();
		Option resDirOption = Option.builder("r").longOpt("result").hasArg().required(true).type(String.class)
				.desc("result direcotory").build();
		Option batchNameOption = Option.builder("b").longOpt("batch").hasArg().required(true).type(String.class)
				.desc("batch name").build();
		Option dataNameOption = Option.builder("d").longOpt("data").hasArg().required(true).type(String.class)
				.desc("data name").build();
		Option targetColOption = Option.builder("targetCol").longOpt("targetCol").hasArg().required(false)
				.type(String.class).desc("exclude column id").build();
		Option targetValueOption = Option.builder("targetValue").longOpt("targetValue").hasArg().required(false)
				.type(String.class).desc("exclude value").build();
		Option detailOption = Option.builder("l").longOpt("log").hasArg().required(false).type(Boolean.class)
				.desc("show detail log").build();

		Options options = new Options();
		options.addOption(expDirOption);
		options.addOption(resDirOption);
		options.addOption(batchNameOption);
		options.addOption(dataNameOption);
		options.addOption(targetColOption);
		options.addOption(targetValueOption);
		options.addOption(detailOption);

		try {
			CommandLineParser parser = new DefaultParser();
			System.out.println(String.join(",", args));
			CommandLine commandLine = parser.parse(options, args);

			String expect = (String) commandLine.getParsedOptionValue("expect");
			String result = (String) commandLine.getParsedOptionValue("result");
			String batch = (String) commandLine.getParsedOptionValue("batch");
			String data = (String) commandLine.getParsedOptionValue("data");
			int excludeTargetCol = commandLine.hasOption("targetCol")
					? Integer.parseInt((String) commandLine.getParsedOptionValue("targetCol"))
					: -1;
			String excludeTargetValue = commandLine.hasOption("targetValue")
					? (String) commandLine.getParsedOptionValue("targetValue")
					: null;
			boolean showDetail = commandLine.hasOption("log")
					? Boolean.parseBoolean((String) commandLine.getParsedOptionValue("log"))
					: true;
//			diffCsv.showDetail = Boolean.parseBoolean((String) commandLine.getOptionValue("log", "true"));

			DiffCsv diffCsv = new DiffCsv(expect, result, batch, data, excludeTargetCol, excludeTargetValue,
					showDetail);
			diffCsv.exec();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DiffCsv", options);
			e.printStackTrace();
		} finally {
			logger.info("complete " + (((new Date()).getTime()) - startTime) + " ms");
		}
	}

	public void exec() throws IOException {
		// 期待するデータ入力
		List<String[]> expList = readCsvToList(this.expect + "/" + this.data + ".csv");
		// 指定した更新者以外を削除
		if (this.excludeTargetValue != null) {
			Iterator<String[]> it = expList.iterator();
			while (it.hasNext()) {
				if (!this.excludeTargetValue.equalsIgnoreCase(it.next()[this.excludeTargetCol])) {
					it.remove();
				}
			}
		}
//		expList.stream().forEach(s->System.out.println(String.join(",",s)));
		logger.debug("exp liens = " + expList.size());

		// 出力結果データ
		List<String[]> resList = generateResultData();
//		resList.stream().forEach(s->System.out.println(String.join(",",s)));
		logger.debug("res lines = " + resList.size());

		compareExpToResList(expList, resList);
		compareResToExpList(resList, expList);
		logger.info("match  " + matchCount);
		logger.info("unmatch  " + unMatchCount);
		logger.info("notFoundRes " + notFoundResCount);
		logger.info("notFoundExp " + notFoundExpCount);
	}

	/**
	 * リストの比較。期待値ー＞結果
	 * 
	 * @param expList
	 * @param resList
	 */
	private void compareExpToResList(List<String[]> expList, List<String[]> resList) {
		// expList -> resList
		for (String[] exp : expList) {
			boolean found = false;
			String expKey = getKeyStr(exp);
			for (String[] res : resList) {
				// 存在するか？キーで比較
				if (expKey.equalsIgnoreCase(getKeyStr(res))) {
					found = true;
					if (compareData(exp, res)) {
						if (showDetail)
							System.out.println("match\t" + String.join(DELIMITTER, exp));
						matchCount++;
					} else {
						unMatchCount++;
					}
					break;
				}
			}
			if (!found) {
				notFoundResCount++;
				if (showDetail)
					System.out.println("not found in res only exp\t" + String.join(DELIMITTER, exp));
			}
		}
	}

	/**
	 * リストの比較。結果ー＞期待値
	 * 
	 * @param resList
	 * @param expList
	 */
	private void compareResToExpList(List<String[]> resList, List<String[]> expList) {
		// expList -> resList
		for (String[] res : resList) {
			boolean found = false;
			String resKey = getKeyStr(res);
			for (String[] exp : expList) {
				// 存在するか？キーで比較
				if (resKey.equalsIgnoreCase(getKeyStr(exp))) {
					found = true;
					break;
				}
			}
			if (!found) {
				notFoundExpCount++;
				if (showDetail)
					System.out.println("not found in exp only res\t" + String.join(DELIMITTER, res));
			}
		}
	}

	/**
	 * レコード同士の比較
	 * 
	 * @param exp
	 * @param res
	 */
	private boolean compareData(String[] exp, String[] res) {
		boolean match = true;
		Set<Integer> unmatchCol = new HashSet<Integer>();
		List<Integer> targetCols = dataAttrMap.get(data).excludeCols;
		for (int i = 0; i < exp.length; i++) {
			if (!targetCols.contains(i)) {
				if (exp[i].equalsIgnoreCase(res[i])) {
					// OK
				} else {
					if (dataAttrMap.get(data).floatCheckSet.contains(i)) {
						float expf = 0f;
						float resf = 0f;
						try {
							expf = Float.parseFloat(exp[i]);
						} catch (NumberFormatException nfe) {
							// default 0
						}
						try {
							resf = Float.parseFloat(res[i]);
						} catch (NumberFormatException nfe) {
							// default 0
						}
						if (expf == resf) {
							// OK
						} else {
							match = false;
							unmatchCol.add(i);
						}
					} else {
						// NG
						match = false;
						unmatchCol.add(i);
					}
				}
			}
		}
		if (!match) {
			if (showDetail) {
				System.out.println("unmatch exp\t" + getDifString(exp, unmatchCol));
				System.out.println("unmatch res\t" + getDifString(res, unmatchCol));
			}
		}
		return match;
	}

	/**
	 * 差異のある項目を[]で括って１レコードの全項目を文字列化。
	 * 
	 * @param dats
	 * @param unmatchCol
	 * @return
	 */
	private String getDifString(String[] dats, Set<Integer> unmatchCol) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < dats.length; i++) {
			if (i > 0) {
				sb.append(DELIMITTER);
			}
			if (unmatchCol.contains(i)) {
				sb.append("[");
				sb.append(dats[i]);
				sb.append("]");
			} else {
				sb.append(dats[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * キー項目を１つの文字列に連結。
	 * 
	 * @param dat
	 * @return
	 */
	private String getKeyStr(String[] dat) {
		StringBuffer key = new StringBuffer();
		int[] keiCols = dataAttrMap.get(data).keyCols;
		for (int i = 0; i < keiCols.length; i++) {
			key.append(dat[keiCols[i]]);
		}
		return key.toString();
	}

	/**
	 * 複数の結果ファイルを１つのコレクションを作成。
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<String[]> generateResultData() throws IOException {
		ArrayList<String[]> resList = new ArrayList<String[]>();

		List<File> resFiles = getNestedFileList(this.result, this.data);
		for (File file : resFiles) {
			if (file.getName().endsWith(".csv")) {
				resList.addAll(readCsvToList(file.getAbsolutePath()));
			}
		}
		logger.debug(String.valueOf(resList.size()));
		return resList;
	}

	/**
	 * 指定したパス＋データ以下のサブディレクトリから再帰的にファイルを取得。
	 * 
	 * @param path
	 * @param data
	 * @return
	 */
	private List<File> getNestedFileList(String path, String data) {

		List<File> files = new ArrayList<>();

		Stack<File> stack = new Stack<>();
//		stack.add(new File(path + "/" + data));
		stack.add(new File(path));
		// nakazawa
		while (!stack.isEmpty()) {
			File item = stack.pop();
			if (item.isFile())
				files.add(item);
			if (item.isDirectory()) {
				for (File child : item.listFiles())
					stack.push(child);
			}
		}
		return files;
	}

	/**
	 * generate record list for expect data from csv.
	 * <p>
	 * file exclude updater is not BTS-E001.
	 * 
	 * @return record list(updater is BTS-E001)
	 * @throws IOException expect file
	 */
	private List<String[]> readCsvToList(String fileName) throws IOException {
//		String fileName = this.expect + "/" + this.data + ".csv";
		ArrayList<String[]> expList = new ArrayList<String[]>();
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
		CSVReader reader = new CSVReader(isr);
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			expList.add(nextLine);
		}
		reader.close();
		logger.debug(fileName + " read exp list " + expList.size() + " lines.");
		return expList;
	}

	public DiffCsv() {
		dataAttrMap = new HashMap<String, DataAttr>();

		initializeBtscYukoKwhLExt();
		initializeBtsYukoKwhLExt();
		initializeIf0511();
	}

	public DiffCsv(String expect, String result, String batch, String data, int excludeTargetCol,
			String excludeTargetValue, boolean showDetail) {
		this();
		this.expect = expect;
		this.result = result;
		this.batch = batch;
		this.data = data;
		this.excludeTargetCol = excludeTargetCol;
		this.excludeTargetValue = excludeTargetValue;
		this.showDetail = showDetail;
	}

	public int getUnMatchCount() {
		return unMatchCount;
	}

	public int getNotFoundResCount() {
		return notFoundResCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public int getNotFoundExpCount() {
		return notFoundExpCount;
	}

	/**
	 * BTSC_YUKO_KWH_L_EXT
	 */
	private void initializeBtscYukoKwhLExt() {
		/** BTSC_YUKO_KWH_L_EXT */
		int[] key1 = { 0, 1, 2, 3, 4 };
		Set<Integer> floatSet1 = new HashSet<Integer>();
		for (int i = 8; i < 57; i++) {
			floatSet1.add(i);
		}
		Integer[] exclude1 = { 109, 110, 111 };
		dataAttrMap.put("BTSC_YUKO_KWH_L_EXT", new DataAttr(key1, floatSet1, exclude1));
	}

	/**
	 * BTS_YUKO_KWH_L_EXT
	 */
	private void initializeBtsYukoKwhLExt() {
		int[] key2 = { 1, 2, 3 };
		Set<Integer> floatSet2 = new HashSet<Integer>();
		for (int i = 4; i < 52; i++) {
			floatSet2.add(i);
		}
		Integer[] exclude2 = { 0, 100 };
		dataAttrMap.put("BTS_YUKO_KWH_L_EXT", new DataAttr(key2, floatSet2, exclude2));
	}

	/**
	 * IF0511
	 */
	private void initializeIf0511() {
		int[] key = { 0, 1, 2 };
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 5 + 47; i++) {
			floatSet.add(i);
		}
		Integer[] exclude = {};
		dataAttrMap.put("IF0511", new DataAttr(key, floatSet, exclude));
	}

	public class DataAttr {
		// data属性
		/** キーカラム。 */
		int[] keyCols;
		/** float比較カラムNo */
		Set<Integer> floatCheckSet;
		/** チェック除外カラムNo。更新日、ファイルパスなど。 */
		List<Integer> excludeCols;

		/**
		 * データ属性コンストラクタ。
		 * 
		 * @param keyCols       キー項目カラム。
		 * @param floatSet      数値比較カラム。
		 * @param targetCol     対象抽出カラム１つのみ。
		 * @param targetUpdater 対象抽出項目１つのみ。
		 * @param excludeCols   比較除外カラム。
		 */
		public DataAttr(int[] keyCols, Set<Integer> floatCheckSet, Integer[] excludeCols) {
			this.keyCols = keyCols;
			this.floatCheckSet = floatCheckSet;
			this.excludeCols = Arrays.asList(excludeCols);
		}
	}
}
