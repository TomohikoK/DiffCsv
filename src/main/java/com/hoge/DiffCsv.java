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
//				"-r", "/Volumes/GoogleDrive/マイドライブ/九電託送/Phase3/現新比較/BTS-E001/comp_test/btse001dp/result/btsc_yuko_kwh_l_ext", //
//				"-b", "btse001dp", //
//				"-d", "BTSC_YUKO_KWH_L_EXT", //
//				"-targetCol", "110", //
//				"-targetValue", "BTS-E001", //
//				"-l", "false", //
//		};
//		args = testParam;
//		String[] testParam = { //
//				"-e", "/Users/nakazawasugio/qd-bts/ee/btsh002dp/OUT", //
//				"-r", "/Users/nakazawasugio/qd-bts/ee/btsh002dp/btsh002dp/result/btsc_yuko_kwh_l_ext", //
//				"-b", "btsh002dp", //
//				"-d", "BTSC_YUKO_KWH_L_EXT", //
//				"-targetCol", "4", //
//				"-targetValue", "2018-08-30", //
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

			DiffCsv diffCsv = new DiffCsv();
			diffCsv.expect = (String) commandLine.getParsedOptionValue("expect");
			diffCsv.result = (String) commandLine.getParsedOptionValue("result");
			diffCsv.batch = (String) commandLine.getParsedOptionValue("batch");
			diffCsv.data = (String) commandLine.getParsedOptionValue("data");
			diffCsv.excludeTargetCol = commandLine.hasOption("targetCol")
					? Integer.parseInt((String) commandLine.getParsedOptionValue("targetCol"))
					: -1;
			diffCsv.excludeTargetValue = commandLine.hasOption("targetValue")
					? (String) commandLine.getParsedOptionValue("targetValue")
					: null;
			diffCsv.showDetail = commandLine.hasOption("log")
					? Boolean.parseBoolean((String) commandLine.getParsedOptionValue("log"))
					: true;
//			diffCsv.showDetail = Boolean.parseBoolean((String) commandLine.getOptionValue("log", "true"));

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
		logger.info("notFoundRes " + notFoundResCount);
		logger.info("notFoundExp " + notFoundExpCount);
		logger.info("unmatch  " + unMatchCount);
	}

	/**
	 * リストの比較。期待値ー＞結果
	 * 
	 * @param expList
	 * @param resList
	 */
	private void compareExpToResList(List<String[]> expList, List<String[]> resList) {
		// expList -> resList
		int errorCounter = 1;
		for (String[] exp : expList) {
			boolean found = false;
			String expKey = getKeyStr(exp);
			for (String[] res : resList) {
				// 存在するか？キーで比較
				if (expKey.equalsIgnoreCase(getKeyStr(res))) {
					found = true;
					if (compareData(exp, res)) {
//						System.out.println("match\t" + String.join(DELIMITTER, exp));
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
					System.out.println("not found res only exp\t" + String.join(DELIMITTER, exp));
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
		int errorCounter = 1;
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
					System.out.println("not found exp only res\t" + String.join(DELIMITTER, res));
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
		List<Integer> targetCols = dataAttrMap.get(data).targetCols;
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
//				System.out.println("not match exp\t" + String.join(DELIMITTER, exp));
//				System.out.println("not match res\t" + String.join(DELIMITTER, res));
				System.out.println("not match exp\t" + getDifString(exp, unmatchCol));
				System.out.println("not match res\t" + getDifString(res, unmatchCol));
				System.out.println();
			}
		}
		return match;
	}

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
		int counter = 0;
		while ((nextLine = reader.readNext()) != null) {
			// target updater is BTS-E001
//			if ("BTS-E001".equalsIgnoreCase(nextLine[110])) {
			expList.add(nextLine);
//			}
//			if (counter++ < 10) {
//				logger.debug(nextLine[110]);
//			}
		}
		logger.debug(fileName + " read exp list " + expList.size() + " lines.");
		return expList;
	}

	public DiffCsv() {
		dataAttrMap = new HashMap<String, DataAttr>();

//		btse001dpで使うBTSC_YUKO_KWH_L_EXT
		int[] key1 = { 0, 1, 2, 3, 4 };
		Integer[] exclude1 = { 109, 110, 111 };
		dataAttrMap.put("BTSC_YUKO_KWH_L_EXT", new DataAttr(key1, 8, 56, 110, "BTS-E001", exclude1));
//		int[] key1 = { 0,1,2,3,4 };
//		Integer[] exclude1 = {  };
//		dataAttrMap.put("BTSC_YUKO_KWH_L_EXT", new DataAttr(key1,8,56,0,null,exclude1));

//		btsh002dpで使うBTSC_YUKO_KWH_L_EXT
		int[] key2 = { 1, 2, 3, 4 };
		Integer[] exclude2 = { 100 };
		dataAttrMap.put("BTS_YUKO_KWH_L_EXT", new DataAttr(key2, 4, 51, 110, null, exclude2));
	}

	public class DataAttr {
		// data属性
		// キー項目数。先頭から連続。
		int[] keyCols;
		HashSet<Integer> floatCheckSet;
		// float比較開始カラム
		int floatCheckStartCol;
		// float比較終了カラム
		int floatCheckEndCol;
		// チェックしない最終カラムNo
		List<Integer> targetCols;

		/**
		 * 
		 * @param keyColNum          キー項目カラムColNoリスト。
		 * @param floatCheckStartCol float比較開始カラムNo
		 * @param floatCheckEndCol   float比較終了カラムNo
		 * @param targetCols         比較除外カラム。最後から連続。
		 */
		public DataAttr(int[] keyCols, int floatCheckStartCol, int floatCheckEndCol, int updaterCol,
				String targetUpdater, Integer[] targetCols) {
			floatCheckSet = new HashSet<Integer>();
			this.floatCheckStartCol = floatCheckStartCol;
			// float比較終了カラム
			this.floatCheckEndCol = floatCheckEndCol;
			for (int i = this.floatCheckStartCol; i < this.floatCheckEndCol; i++) {
				floatCheckSet.add(i);
			}
			// キー項目ColNo
			this.keyCols = keyCols;
			// チェックしない最終カラムの数
			this.targetCols = Arrays.asList(targetCols);
		}
	}
}
