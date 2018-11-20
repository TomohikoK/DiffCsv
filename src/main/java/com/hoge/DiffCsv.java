package com.hoge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

	static private Logger logger = LoggerFactory.getLogger(DiffCsv.class);
	static private String DELIMITTER = "\t";
	static private SimpleDateFormat SDF10 = new SimpleDateFormat("yyyy-MM-dd");
	static private SimpleDateFormat SDF19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	static private SimpleDateFormat SDF26 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.ssssss");
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
	private int resDupCount;
	private int expDupCount;

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
		logger.info("expect file = " + this.expect + "/" + this.data + ".csv");
		logger.info("result path = " + this.result + " /*.csv");
		logger.info("data name   = " + this.data);
		// 期待するデータ
		File expDir = new File(this.expect);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				String fileName = data + ".csv";
				if ((fileName.equalsIgnoreCase(name))) {
//				if (name.startsWith(data) && name.endsWith(".csv")) {
					return true;
				}
				return false;
			}
		};
		String[] expFileList = expDir.list(filter);
		List<String[]> expList = new ArrayList<String[]>();
		for (String fileName : expFileList) {
			List<String[]> tmpList = readCsvToList(this.expect + "/" + fileName);
			// 指定した更新者以外を削除
			if (this.excludeTargetValue != null) {
				Iterator<String[]> it = tmpList.iterator();
				while (it.hasNext()) {
					if (!this.excludeTargetValue.equalsIgnoreCase(it.next()[this.excludeTargetCol])) {
						it.remove();
					}
				}
			}
			// special rule
			if ("BTSC_KEIYAKU_ERROR_INFO".equalsIgnoreCase(data)) {
				tmpList = specialconvbtscKeiyakuErrorInfo(tmpList);
			}
			expList.addAll(tmpList);
		}
		// key順にソート
		Collections.sort(expList, new RecComparator());
		// 重複キーチェック
		checkDuplicate(expList);
		// 出力結果データ
		List<String[]> resList = generateResultData();
//		resList.stream().forEach(s->System.out.println(String.join(",",s)));

		compareExpToResList(expList, resList);
		compareResToExpList(resList, expList);

		logger.info(this.data);
		logger.info("exp lines = " + expList.size());
		logger.info("res lines = " + resList.size());
		logger.info("match  " + matchCount);
		logger.info("unmatch  " + unMatchCount);
		logger.info("notFoundRes " + notFoundResCount);
		logger.info("notFoundExp " + notFoundExpCount);
		logger.info("expDup " + expDupCount);
		logger.info("resDup " + resDupCount);
	}

	private void checkDuplicate(List<String[]> recList) {
		Set<String> keySet = new HashSet<String>();
		for (String[] rec : recList) {
			if (!keySet.contains(getKeyStr(rec))) {
				keySet.add(getKeyStr(rec));
			} else {
				System.out.println("expDup\t" + String.join(DELIMITTER, rec));
				expDupCount++;
			}
		}
	}

	private List<String[]> specialconvbtscKeiyakuErrorInfo(List<String[]> tmpList) {
		Iterator<String[]> it = tmpList.iterator();
		while (it.hasNext()) {
			String[] rec = it.next();
			rec[7] = rec[7].replaceAll("\\s", "");
			if (rec[7].contains("[")) {
				rec[7] = rec[7].replace("[", "");
				rec[7] = rec[7].replace("]", "");
			}
//			if(rec[7].contains("idoKbn")) {
//				rec[7] = rec[7].replaceAll("idoKbn", "異動区分");
//			}
//			if(rec[7].contains("supplyPointNumber")) {
//				rec[7] = rec[7].replaceAll("supplyPointNumber", "供給地点特定番号");
//			}
//			if(rec[16].startsWith("2018-09-06 16")) {
//				it.remove();
//			}
		}
		return tmpList;
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
					if (found) {
						// 重複
						System.out.println("dup res\t" + String.join(DELIMITTER, res));
						this.resDup++;
					} else {
						found = true;
						if (compareData(exp, res)) {
							if (showDetail)
								System.out.println("match\t" + String.join(DELIMITTER, exp));
							matchCount++;
						} else {
//							unMatchCount++;
						}
					}
//					break;
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
						if (!compareFloat(exp[i], res[i])) {
							// not same
							match = false;
							unmatchCol.add(i);
						}
					} else if (dataAttrMap.get(data).dateCheckSet.contains(i)) {
						if (!compareDate(exp[i], res[i])) {
							// not same
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
			this.unMatchCount++;
			if (showDetail) {
				System.out.println("unmatch exp\t" + getDifString(exp, unmatchCol));
				System.out.println("unmatch res\t" + getDifString(res, unmatchCol));
			}
		}
		return match;
	}

	private boolean compareFloat(String exp, String res) {
		if (parseFloat(exp) == parseFloat(res)) {
			return true;
		} else {
			return false;
		}
	}

	private float parseFloat(String dat) {
		try {
			return Float.parseFloat(dat);
		} catch (NumberFormatException nfe) {
			return 0f;
		}
	}

	private boolean compareDate(String exp, String res) {
		try {
			if (parseDate(exp).compareTo(parseDate(res)) == 0) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
			return false;
		}
	}

	private Date parseDate(String dat) {
		try {
			// 2018-08-23 10:54:26.000000
			switch (dat.length()) {
			case 10:
				return SDF10.parse(dat);
			case 19:
				return SDF19.parse(dat);
			case 26:
				return SDF26.parse(dat);
			default:
				return null;
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return null;
		}
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

		List<File> resFiles = getNestedFileList(this.result);
		List<String[]> tmpList = null;
		for (File file : resFiles) {
			if (file.getName().endsWith(".csv")) {
				tmpList = readCsvToList(file.getAbsolutePath());
				// special rule
				if ("BTSC_KEIYAKU_ERROR_INFO".equalsIgnoreCase(data)) {
					tmpList = specialconvbtscKeiyakuErrorInfo(tmpList);
				}
				resList.addAll(tmpList);
			}
		}
		// 除外
//		Iterator<String[]> it = resList.iterator();
//		while (it.hasNext()) {
//			String[] data = it.next();
//			if (!"BTS-B003DP".equalsIgnoreCase(data[16])) {
//				it.remove();
//			}
//		}
		return resList;
	}

	/**
	 * 指定したパス＋データ以下のサブディレクトリから再帰的にファイルを取得。
	 *
	 * @param path
	 * @return
	 */
	private List<File> getNestedFileList(String path) {
		List<File> files = new ArrayList<>();
		Stack<File> stack = new Stack<>();
		stack.add(new File(path));
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
	 *
	 * @return record list
	 * @throws IOException expect file
	 */
	private List<String[]> readCsvToList(String fileName) throws IOException {
//		String fileName = this.expect + "/" + this.data + ".csv";
		ArrayList<String[]> retList = new ArrayList<String[]>();
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
		CSVReader reader = new CSVReader(isr);
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			// スラッシュ日付をハイフン日付に変換
			for (int i = 0; i < nextLine.length; i++) {
				if (nextLine[i].matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")) {
					nextLine[i] = nextLine[i].replaceAll("([0-9]{4})/([0-9]{2})/([0-9]{2})", "$1-$2-$3");
				}
			}
			// 読み込んだCSVレコードを戻り値リストへ追加。
			retList.add(nextLine);
		}
		reader.close();
		logger.info(fileName + " read list " + retList.size() + " lines.");
		return retList;
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

	public int getExpDup() {
		return expDupCount;
	}

	public int getResDup() {
		return resDupCount;
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
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude1 = { 109, 110, 111 };
		dataAttrMap.put("BTSC_YUKO_KWH_L_EXT", new DataAttr(key1, floatSet1, dateSet, exclude1));
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
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude2 = { 0, 100 };
		dataAttrMap.put("BTS_YUKO_KWH_L_EXT", new DataAttr(key2, floatSet2, dateSet, exclude2));
	}

	/**
	 * IF0511
	 */
	private void initializeIf0511() {
		int[] key = { 0, 1, 2 };
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 5 + 96; i += 2) {
			floatSet.add(i);
		}
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = {};
		dataAttrMap.put("IF0511", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_BEFORE_CALC_CHK_L
	 */
	private void initializeBtscBeforeCalcChkL() {
		int[] key = { 0, 1, 2, 3, 4, 7, 8, 9, 10, 11, 12, 13 };
		Set<Integer> floatSet = new HashSet<Integer>();
		floatSet.add(15);
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = { 21, 22 };
		dataAttrMap.put("BTSC_BEFORE_CALC_CHK_L", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_BEFORE_CALC_CHK_L_DETAIL
	 */
	private void initializeBtscBeforeCalcChkLDetail() {
		int[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		Set<Integer> floatSet = new HashSet<Integer>();
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = { 21, 22 };
		dataAttrMap.put("BTSC_BEFORE_CALC_CHK_L_DETAIL", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_CALC_CHK_CNT
	 */
	private void initializeBtscCalcChkCnt() {
		int[] key = { 0, 1, 2, 3, 4 };
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 10; i++) {
			floatSet.add(i);
		}
		for (int i = 11; i < 17; i++) {
			floatSet.add(i);
		}
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = { 10 };
		dataAttrMap.put("BTSC_CALC_CHK_CNT", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_BP_EXT_STATUS
	 */
	private void initializeBtscBpExtStatus() {
		int[] key = { 0, 1, 2, 3 };
		Set<Integer> floatSet = new HashSet<Integer>();
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = { 5, 6, 7, 8 };
		dataAttrMap.put("BTSC_BP_EXT_STATUS", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTS_K_KAKUTEI_J_L_EXT
	 */
	private void initializeBtsKKakuteiJLExt() {
		int[] key = { 1, 3, 4 };
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 27; i++) {
			floatSet.add(i);
		}
		Set<Integer> dateSet = new HashSet<Integer>();
		Integer[] exclude = { 28 };
		dataAttrMap.put("BTS_K_KAKUTEI_J_L_EXT", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IF0101_RENKEI_DATA
	 */
	private void initializeBtscIf0101RenkeiData() {
		// キーカラム
		int[] key = { 2, 3, 4, 5, 6 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 10; i < 12; i++) {
			floatSet.add(i);
		}
		for (int i = 16; i < 25; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(1);
		dateSet.add(13);
		dateSet.add(14);
		// 比較除外カラム
		Integer[] exclude = { 0, 26, 28, 29, 30, 31 };
		dataAttrMap.put("BTSC_IF0101_RENKEI_DATA", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IF0102_RENKEI_DATA
	 */
	private void initializeBtscIf0102RenkeiData() {
		// キーカラム
		int[] key = { 2, 3, 4, 6, 9 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(7);
		dateSet.add(8);
		// 比較除外カラム
		Integer[] exclude = { 0, 1, 2, 10, 12, 13, 14, 15 };
		dataAttrMap.put("BTSC_IF0102_RENKEI_DATA", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IF0209_RENKEI_DATA
	 */
	private void initializeBtscIf0209RenkeiData() {
		// キーカラム
		int[] key = { 7 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(5);
		dateSet.add(6);
		// 比較除外カラム
		Integer[] exclude = { 0, 1, 8, 10, 11, 12, 13 };
		dataAttrMap.put("BTSC_IF0209_RENKEI_DATA", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IF0404_RENKEI_DATA
	 */
	private void initializeBtscIf0404RenkeiData() {
		// キーカラム
		int[] key = { 9 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(7);
		dateSet.add(8);
		// 比較除外カラム
		Integer[] exclude = { 0, 1, 10, 12, 13, 14, 15 };
		dataAttrMap.put("BTSC_IF0404_RENKEI_DATA", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_KEIYAKU_CHECK
	 */
	private void initializeBtscKeiyakuCheck() {
		// キーカラム
		int[] key = { 0 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(9);
		dateSet.add(10);
		// 比較除外カラム
		Integer[] exclude = { 1, 2, 14, 15, 16, 17 };
		dataAttrMap.put("BTSC_KEIYAKU_CHECK", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_KEIYAKU_CHECK_NEW
	 */
	private void initializeBtscKeiyakuCheckNew() {
		// キーカラム
		int[] key = { 3, 4, 5, 6, 7, 8 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(9);
		dateSet.add(10);
		// 比較除外カラム
		Integer[] exclude = { 0, 1, 2, 14, 15, 16, 17 };
		dataAttrMap.put("BTSC_KEIYAKU_CHECK_NEW", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTS_YUKO_KWH_L_EIGYO_EXT
	 */
	private void initializeBtsYukoKwhLEigyoExt() {
		// キーカラム
		int[] key = { 0, 1, 2, 3 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 4; i < 52; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = { 52 };
		dataAttrMap.put("BTS_YUKO_KWH_L_EIGYO_EXT", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_YUKO_KWH_MONTH_L_EXT
	 */
	private void initializeBtscYukoKwhMonthLExt() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 14; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(1);
		dateSet.add(18);
		dateSet.add(19);
		// 比較除外カラム
		Integer[] exclude = { 23, 24, 25, 26 };
		dataAttrMap.put("BTSC_YUKO_KWH_MONTH_L_EXT", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_SHIJISU_IF
	 */
	private void initializeBtscShijisuIf() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4, 5, 6, 7 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 8; i < 12; i++) {
			floatSet.add(i);
		}
		for (int i = 13; i < 17; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(19);
		dateSet.add(20);
		// 比較除外カラム
		Integer[] exclude = { 22, 23, 24, 25
//				,26,27,28,29
		};
		dataAttrMap.put("BTSC_SHIJISU_IF", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_SHIJISU_CHK_RES
	 */
	private void initializeBtscShijisuChkRes() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = { 21, 22, 23, 24 };
		dataAttrMap.put("BTSC_SHIJISU_CHK_RES", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0506
	 */
	private void initializeIf0506() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4, 5, 6 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 7; i < 55; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0506", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0514
	 */
	private void initializeIf0514() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4, 5, 6 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 7; i < 55; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0514", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_KEIYAKU_ERROR_INFO
	 */
	private void initializeBtscKeiyakuErrorInfo() {
		// キーカラム
		int[] key = { 2, 3, 4, 5, 6, 7 };
//		int[] key = { 2, 3, 7 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = { 0, 1, 4, 13, 14, 15, 16 };
		dataAttrMap.put("BTSC_KEIYAKU_ERROR_INFO", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_K_SIJISU_J_L_EXT
	 */
	private void initializeBtscKSijisuJLExt() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4, 5, 6 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 7; i < 15; i++) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(17);
		dateSet.add(18);
		// 比較除外カラム
		Integer[] exclude = { 21, 22, 23, 24 };
		dataAttrMap.put("BTSC_K_SIJISU_J_L_EXT", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_DOUJIDORYO_FILE_STATUS
	 */
	private void initializeBtscDoujidoryoFileStatus() {
		// キーカラム
		int[] key = { 0, 1, 2, 3 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 4; i < 8; i++) {
			floatSet.add(i);
		}
		floatSet.add(10);
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(1);
		dateSet.add(8);
		// 比較除外カラム
		Integer[] exclude = { 11, 12, 13, 14, 15 };
		dataAttrMap.put("BTSC_DOUJIDORYO_FILE_STATUS", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IDO_INFO_LIST key=1,2,3,4,5,6,7 col=0 がシーケンスで一致しないときに使用。
	 */
	private void initializeBtscIdoInfoList() {
		// キーカラム
		int[] key = { 1, 2, 3, 4, 5, 6, 7 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		floatSet.add(8);
		floatSet.add(12);
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(9);
		dateSet.add(10);
		dateSet.add(11);
		dateSet.add(17);
		// 比較除外カラム
		Integer[] exclude = { 0, 18, 19, 20, 21 };
		dataAttrMap.put("BTSC_IDO_INFO_LIST", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_IDO_INFO_LIST2 key=0 col=0 がシーケンスで一致するときに使用。
	 */
	private void initializeBtscIdoInfoList2() {
		// キーカラム
		int[] key = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		floatSet.add(8);
		floatSet.add(12);
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		dateSet.add(9);
		dateSet.add(10);
		dateSet.add(11);
		dateSet.add(17);
		// 比較除外カラム
		Integer[] exclude = { 0, 18, 19, 20, 21 };
		dataAttrMap.put("BTSC_IDO_INFO_LIST2", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTS_HIKINUKI_L
	 */
	private void initializeBtsHikinukiL() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("BTS_HIKINUKI_L", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0101 IF0101_WATARI ２ファイル対応
	 */
	private void initializeIf0101() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0101", new DataAttr(key, floatSet, dateSet, exclude));
		dataAttrMap.put("IF0101_WATARI", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0102
	 */
	private void initializeIf0102() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0102", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0209
	 */
	private void initializeIf0209() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0209", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0404
	 */
	private void initializeIf0404() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0404", new DataAttr(key, floatSet, dateSet, exclude));
		dataAttrMap.put("IF0404_WATARI", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * IF0505
	 */
	private void initializeIf0505() {
		// キーカラム
		int[] key = { 0, 1, 2, 3, 4 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		for (int i = 5; i < 5 + 48 * 2; i = i + 2) {
			floatSet.add(i);
		}
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = {};
		dataAttrMap.put("IF0505", new DataAttr(key, floatSet, dateSet, exclude));
	}

	/**
	 * BTSC_INFOMATION btscInfomation btsc_infomation
	 */
	private void initializeBtscInfomation() {
		// キーカラム
		int[] key = { 0, 1, 2 };
		// 数値型カラム
		Set<Integer> floatSet = new HashSet<Integer>();
		// 日付型カラム指定
		Set<Integer> dateSet = new HashSet<Integer>();
		// 比較除外カラム
		Integer[] exclude = { 3, 4, 5, 6 };
		dataAttrMap.put("BTSC_INFOMATION", new DataAttr(key, floatSet, dateSet, exclude));
	}

	public DiffCsv() {
		dataAttrMap = new HashMap<String, DataAttr>();

		initializeBtscYukoKwhLExt();
		initializeBtsYukoKwhLExt();
		initializeIf0511();
		initializeBtscBeforeCalcChkL();
		initializeBtscBeforeCalcChkLDetail();
		initializeBtscCalcChkCnt();
		initializeBtscBpExtStatus();
		initializeBtsKKakuteiJLExt();
		initializeBtscIf0101RenkeiData();
		initializeBtscIf0102RenkeiData();
		initializeBtscIf0209RenkeiData();
		initializeBtscIf0404RenkeiData();
		initializeBtscKeiyakuCheck();
		initializeBtscKeiyakuCheckNew();
		initializeBtsYukoKwhLEigyoExt();
		initializeBtscYukoKwhMonthLExt();
		initializeBtscShijisuIf();
		initializeBtscShijisuChkRes();
		initializeIf0506();
		initializeIf0514();
		initializeBtscKeiyakuErrorInfo();
		initializeBtscKSijisuJLExt();
		initializeBtscDoujidoryoFileStatus();
		initializeBtscIdoInfoList();
		initializeBtscIdoInfoList2();
		initializeBtsHikinukiL();
		initializeIf0101();
		initializeIf0102();
		initializeIf0209();
		initializeIf0404();
		initializeIf0505();
		initializeBtscInfomation();
	}

	public class DataAttr {
		// data属性
		/** キーカラム。 */
		int[] keyCols;
		/** float比較カラムNo */
		Set<Integer> floatCheckSet;
		/** Date比較カラムNo */
		Set<Integer> dateCheckSet;
		/** チェック除外カラムNo。更新日、ファイルパスなど。 */
		List<Integer> excludeCols;

		/**
		 * データ属性コンストラクタ。
		 *
		 * @param keyCols      キー項目カラム。
		 * @param floatSet     数値カラム。
		 * @param dateCheckSet 日付カラム。
		 * @param excludeCols  比較除外カラム。
		 */
		public DataAttr(int[] keyCols, Set<Integer> floatCheckSet, Set<Integer> dateCheckSet, Integer[] excludeCols) {
			this.keyCols = keyCols;
			this.floatCheckSet = floatCheckSet;
			this.dateCheckSet = dateCheckSet;
			this.excludeCols = Arrays.asList(excludeCols);
		}
	}

	public class RecComparator implements Comparator<String[]> {

		@Override
		public int compare(String[] rec1, String[] rec2) {
			return getKeyStr(rec1).compareTo(getKeyStr(rec1));
		}
	}

}
