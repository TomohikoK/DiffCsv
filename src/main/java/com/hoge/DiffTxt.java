package com.hoge;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

/**
 * 同時同量（BTS-H001）で出力するファイルの現新比較。<br>
 *
 * @author nakazawasugio
 *
 */
public class DiffTxt {

	static private Logger logger = LoggerFactory.getLogger(DiffTxt.class);
//	static private String DELIMITTER = "\t";
//	static private SimpleDateFormat SDF10 = new SimpleDateFormat("yyyy-MM-dd");
//	static private SimpleDateFormat SDF14 = new SimpleDateFormat("yyyyMMddHHmmSS");
//	static private SimpleDateFormat SDF19 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
//	static private SimpleDateFormat SDF26 = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.ssssss");
	private String expect;
	private String result;
	private String data;
	private boolean showLog;
	// 結果カウント
	private int matchCount;
	private int unMatchCount;
	private int remainJgCount;
	Map<String, Map<String, List<String[]>>> expMap;
	Map<String, Map<String, List<String[]>>> resMap;

	public Map<String, Map<String, List<String[]>>> getExpMap() {
		return expMap;
	}

	public Map<String, Map<String, List<String[]>>> getResMap() {
		return resMap;
	}

	public static void main(String[] args) throws ParseException {
		logger.info("start...");
		long startTime = (new Date()).getTime();
//		String[] testParam = { //
//				"-e", "/Users/nakazawasugio/qd-bts/tmp/btsh001dp/OUT/halfTime", //
//				"-r", "/Users/nakazawasugio/qd-bts/tmp/btsh001dp/tmp/btsh_hulft_update_file_source", //
//				"-d", "update", //
//				"-l", "false", //
//		};
//		args = testParam;
		// command line
		Option expDirOption = Option.builder("e").longOpt("expect").hasArg().required(true).type(String.class)
				.desc("expect direcotory base path(upper on OUT)").build();
		Option resDirOption = Option.builder("r").longOpt("result").hasArg().required(true).type(String.class)
				.desc("result direcotory").build();
		Option dataNameOption = Option.builder("d").longOpt("data").hasArg().required(true).type(String.class)
				.desc("data name").build();
		Option detailOption = Option.builder("l").longOpt("log").hasArg().required(false).type(Boolean.class)
				.desc("show detail log").build();

		Options options = new Options();
		options.addOption(expDirOption);
		options.addOption(resDirOption);
		options.addOption(dataNameOption);
		options.addOption(detailOption);

		DiffTxt diffCsv = null;
		try {
			CommandLineParser parser = new DefaultParser();
			System.out.println(String.join(",", args));
			CommandLine commandLine = parser.parse(options, args);

			String expect = (String) commandLine.getParsedOptionValue("expect");
			String result = (String) commandLine.getParsedOptionValue("result");
			String data = (String) commandLine.getParsedOptionValue("data");
			boolean showDetail = commandLine.hasOption("log")
					? Boolean.parseBoolean((String) commandLine.getParsedOptionValue("log"))
					: true;

			diffCsv = new DiffTxt(expect, result, data, showDetail);
		} catch (Exception e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("DiffCsv", options);
			e.printStackTrace();
			throw (e);
		}
		logger.info("complete " + (((new Date()).getTime()) - startTime) + " ms");
		try {
			diffCsv.exec();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exec() throws IOException {
		logger.info("data name   = " + this.data);
		logger.info("expect file = " + this.expect);
		logger.info("result path = " + this.result);

		expMap = getRecList(expect);
		resMap = getRecList(result);
		logger.info(this.data);
		logger.info("exp stat");
		logoutStat(expMap);
		logger.info("res stat");
		logoutStat(resMap);

		// 比較 exp -> res
		compareToRes();

//		logger.info("exp lines = " + expList.size());
//		logger.info("res lines = " + resList.size());
		logger.info("match  " + matchCount);
		logger.info("unmatch  " + unMatchCount);
		logger.info("remainJgCount	" + remainJgCount);
//		logger.info("notFoundRes " + notFoundResCount);
//		logger.info("notFoundExp " + notFoundExpCount);
//		logger.info("expDup " + expDupCount);
//		logger.info("resDup " + resDupCount);
	}

	/**
	 * 期待から結果を比較
	 */
	private void compareToRes() {
		for (String date : expMap.keySet()) {
			for (String jg : expMap.get(date).keySet()) {
				searchResRec(expMap.get(date).get(jg), resMap.get(date).get(jg));
			}
		}
	}

	private void searchResRec(List<String[]> expRecList, List<String[]> resList) {
		List<String[]> tmpList = new ArrayList<String[]>(resList);
		for (String[] expRec : expRecList) {
			boolean found = false;
			for (String[] resRec : tmpList) {
				if (compareCols(resRec, expRec)) {
					matchCount++;
					found = true;
					tmpList.remove(resRec);
					if (showLog) {
						logger.info("match exp=" + String.join(",", expRec));
						logger.info("match res=" + String.join(",", resRec));
					}
					break;
				}
//				logger.info("unmatch res=" + String.join(",", resRec));
			}
			if (!found) {
				unMatchCount++;
				if (showLog) {
					logger.info("unmatch exp=" + String.join(",", expRec));
				}
			}
		}
		if (tmpList.size() > 0) {
			remainJgCount++;
		}
	}

	private boolean compareCols(String[] resRec, String[] expRec) {
		// 対象日
		if (expRec[0].equalsIgnoreCase(resRec[0].replaceAll("-", ""))) {
			// 情報区分コード
			if (expRec[1].equalsIgnoreCase(resRec[1])) {
				// 事業者ID
				if (expRec[2].equalsIgnoreCase(resRec[2])) {
					// ファイル名[6]
					String asksFileName = expRec[0] + "/" + expRec[2] + "/" + expRec[3];
					if (resRec[3].startsWith(expRec[3])) {
						// ファイル存在確認
						return true;
//						File tmp = new File(this.result + "/../doujidoryo_half_time/" + asksFileName);
//						if (tmp.isFile()) {
//							return true;
//						}
					}
				}
			}
		}
		return false;
	}
	private boolean compareColsTmp(String[] resRec, String[] expRec) {
		// 対象日
		if (expRec[0].equalsIgnoreCase(resRec[0].replaceAll("-", ""))) {
			// 情報区分コード
			if (expRec[1].equalsIgnoreCase(resRec[1])) {
				// 事業者ID
				if (expRec[2].equalsIgnoreCase(resRec[2])) {
					// ファイル名[3]
					String asksFileName = expRec[3] + "/" + expRec[2] + "/" + expRec[3].substring(0, 6);
					if (resRec[3].startsWith(asksFileName)) {
						// ファイル存在確認
						File tmp = new File(this.result + "/../res_xml/" + resRec[6].replaceAll(".zip", ".xml"));
						if (tmp.isFile()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void logoutStat(Map<String, Map<String, List<String[]>>> rootMap) {
		for (String dateStr : rootMap.keySet()) {
			// 日付ごと件数表示
			StringBuffer sb = new StringBuffer("date=" + dateStr + " jigyosha - num={");
			for (String jgStr : rootMap.get(dateStr).keySet()) {
				sb.append(jgStr);
				sb.append(" - ");
				sb.append(rootMap.get(dateStr).get(jgStr).size());
				sb.append(",");
			}
			sb.append("}");
			logger.info(sb.toString());
		}
	}

	private Map<String, Map<String, List<String[]>>> getRecList(String expectDir) throws IOException {
		File rootDir = new File(expectDir);
		File[] dateDirs = rootDir.listFiles();
		Map<String, Map<String, List<String[]>>> rootMap = new HashMap<String, Map<String, List<String[]>>>();
		for (File dateDir : dateDirs) {
			// 日付ディレクトリのみ
			if (dateDir.isDirectory() && Pattern.matches("[0-9]{8}", dateDir.getName())) {
				Map<String, List<String[]>> dateMap = new HashMap<String, List<String[]>>();
				File[] jgDirs = dateDir.listFiles();
				for (File jgDir : jgDirs) {
					if (dateDir.isDirectory() && Pattern.matches("[0-9]{5}", jgDir.getName())) {
						File[] dataFiles = jgDir.listFiles();
						List<String[]> dataList = new ArrayList<String[]>();
						for (File dataFile : dataFiles) {
							if (dataFile.isFile() && dataFile.getName().endsWith(".txt")) {
								dataList.addAll(readCsvToList(dataFile.getAbsolutePath()));
//								logger.debug("date = " + dateDir.getName() + " jigyosho=" + jgDir.getName()
//										+ " dataFile=" + dataFile.getName());
							}
						}
						dateMap.put(jgDir.getName(), dataList);
					}
				}
				rootMap.put(dateDir.getName(), dateMap);
			}
		}
		return rootMap;
	}

	/**
	 * generate record list for expect data from csv.
	 * <p>
	 *
	 * @return record list
	 * @throws IOException expect file
	 */
	private List<String[]> readCsvToList(String fileName) throws IOException {
		ArrayList<String[]> retList = new ArrayList<String[]>();
		FileInputStream fis = new FileInputStream(fileName);
		InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
		CSVReader reader = new CSVReader(isr);
		String[] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			for (int i = 0; i < nextLine.length; i++) {
				nextLine[i] = nextLine[i].trim();
			}
			// 読み込んだCSVレコードを戻り値リストへ追加。
			retList.add(nextLine);
		}
		reader.close();
		logger.info(fileName + " read list " + retList.size() + " lines.");
		return retList;
	}

	public DiffTxt(String expect, String result, String data, boolean showLog) {
		this();
		this.expect = expect;
		this.result = result;
		this.data = data;
		this.showLog = showLog;
	}

	public int getUnMatchCount() {
		return unMatchCount;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public DiffTxt() {
	}
}
