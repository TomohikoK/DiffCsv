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
	static boolean SHOW_DETAIL = true;
	static String DELIMITTER = "\t";
	String expect;
	String result;
	String batch;
	String data;

	// 結果カウント
	private int unMatchCount;
	private int notFoundCount;
	private int matchCount;
	private HashMap<String, DataAttr> dataAttrMap;

	public static void main(String[] args) throws ParseException {
		logger.info("start...");
		long startTime = (new Date()).getTime();
//		String[] btse001Param = { //
//				"-e", "/Users/nakazawasugio/qd-bts/ee/btse001dp/OUT", //
//				"-r", "/Users/nakazawasugio/qd-bts/ee/btse001dp/btse001dp/result", //
//				"-b", "btse001dp", //
//				"-d", "BTSC_YUKO_KWH_L_EXT" };
//		args = btse001Param;
		// command line
		Option expDirOption = Option.builder("e").longOpt("expect").hasArg().required(true).type(String.class)
				.desc("expect direcotory base path(upper on OUT)").build();
		Option resDirOption = Option.builder("r").longOpt("result").hasArg().required(true).type(String.class)
				.desc("result direcotory").build();
		Option batchNameOption = Option.builder("b").longOpt("batch").hasArg().required(true).type(String.class)
				.desc("batch name").build();
		Option dataNameOption = Option.builder("d").longOpt("data").hasArg().required(true).type(String.class)
				.desc("data name").build();

		Options options = new Options();
		options.addOption(expDirOption);
		options.addOption(resDirOption);
		options.addOption(batchNameOption);
		options.addOption(dataNameOption);

		try {
			CommandLineParser parser = new DefaultParser();
			System.out.println(String.join(",", args));
			CommandLine commandLine = parser.parse(options, args);
	
			DiffCsv diffCsv = new DiffCsv();
			diffCsv.expect = (String) commandLine.getParsedOptionValue("expect");
			diffCsv.result = (String) commandLine.getParsedOptionValue("result");
			diffCsv.batch = (String) commandLine.getParsedOptionValue("batch");
			diffCsv.data = (String) commandLine.getParsedOptionValue("data");

			diffCsv.exec();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}catch(Exception e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DiffCsv", options);
		}finally {
			logger.info("complete " + (((new Date()).getTime()) - startTime) + " ms");
		}
	}

	public void exec() throws IOException {
		// 期待するデータ
		List<String[]> expList = readCsvToList(this.expect + "/" + this.data + ".csv");
		// 更新者:BTS-E001 以外を削除
		if(dataAttrMap.get(data).targetUpdater != null) {
			Iterator<String[]> it = expList.iterator();
			while (it.hasNext()) {
				if (!dataAttrMap.get(data).targetUpdater.equalsIgnoreCase(it.next()[dataAttrMap.get(data).updaterCol])) {
					it.remove();
				}
			}
		}
//		expList.stream().forEach(s->System.out.println(String.join(",",s)));
		logger.debug("exp " + expList.size());

		// 出力結果データ
		List<String[]> resList = generateResultData();
//		for(String[] resList) {
//		System.out.println(String.join("-", dat));
//	}
//		resList.stream().forEach(s->System.out.println(String.join(",",s)));
		logger.debug("res " + resList.size());

		compareExpToResList(expList, resList);
		logger.info("match  " + matchCount);
		logger.info("notFound " + notFoundCount);
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
						matchCount++;
					} else {
						unMatchCount++;
					}
					break;
				}
			}
			if (!found) {
				notFoundCount++;
				if (SHOW_DETAIL)
					System.out.println("not found exp -> res " + String.join(DELIMITTER, exp));
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
		List<Integer> excludeCols = dataAttrMap.get(data).excludeCols;
		for (int i = 0; i < exp.length; i++) {
			if(!excludeCols.contains(i)) {
				if (exp[i].equalsIgnoreCase(res[i])) {
					// OK
				} else {
					if (dataAttrMap.get(data).floatCheckSet.contains(i)) {
						if (Float.parseFloat(exp[i]) == Float.parseFloat(res[i])) {
							// OK
						} else {
							match = false;
						}
					} else {
						// NG
						match = false;
					}
				}
			}
		}
		if (!match) {
			if (SHOW_DETAIL) {
				System.out.println("not match exp " + String.join(DELIMITTER, exp));
				System.out.println("not match res " + String.join(DELIMITTER, res));
				System.out.println();
			}
		}
		return match;
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
		stack.add(new File(path + "/" + data));
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
		dataAttrMap = new HashMap<String,DataAttr>();
		int[] key1 = { 0,1,2,3,4 };
		Integer[] exclude1 = { 109,110,111 };
		dataAttrMap.put("BTSC_YUKO_KWH_L_EXT", new DataAttr(key1,8,56,110,"BTS-E001",exclude1));

		int[] key2 = { 1,2,3 };
		Integer[] exclude2 = { 100 };
		dataAttrMap.put("BTS_YUKO_KWH_L_EXT", new DataAttr(key2,4,51,110,null,exclude2));
	}
	public class DataAttr{
		//	data属性
		// キー項目数。先頭から連続。
		int[] keyCols;
		HashSet<Integer> floatCheckSet;
		// float比較開始カラム
		int floatCheckStartCol;
		// float比較終了カラム
		int floatCheckEndCol;
		// 結果の更新者カラム番号
		int updaterCol;
		// 結果の対象とする更新者
		String targetUpdater;
		// チェックしない最終カラムNo
		List<Integer> excludeCols;
		/**
		 * 
		 * @param keyColNum キー項目カラムColNoリスト。
		 * @param floatCheckStartCol float比較開始カラムNo
		 * @param floatCheckEndCol float比較終了カラムNo
		 * @param updaterCol 更新者カラムNo。
		 * @param targetUpdater 対象更新者。
		 * @param excludeCols 比較除外カラム。最後から連続。
		 */
		public DataAttr(int[] keyCols,int floatCheckStartCol,int floatCheckEndCol,int updaterCol,String targetUpdater,Integer[] excludeCols) {
			floatCheckSet = new HashSet<Integer>();
			this.floatCheckStartCol = floatCheckStartCol;
			// float比較終了カラム
			this.floatCheckEndCol = floatCheckEndCol;
			for (int i = this.floatCheckStartCol; i < this.floatCheckEndCol; i++) {
				floatCheckSet.add(i);
			}
			// キー項目ColNo
			this.keyCols = keyCols;
			// 結果の更新者カラム番号
			this.updaterCol = updaterCol;
			// 結果の対象とする更新者
			this.targetUpdater = targetUpdater;
			// チェックしない最終カラムの数
			this.excludeCols = Arrays.asList(excludeCols);
		}
	}
}
