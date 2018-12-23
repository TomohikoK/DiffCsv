/**
 *
 */
package com.hoge;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nakazawasugio
 *
 */
public class CreateEmbulkScr {

	static Logger logger = LoggerFactory.getLogger(DiffCsv.class);
	static String DB_USER="usr1";
	static String DB_PASS="pass1";

	private Map<String, String> typeMap;
	private String[][] typeData = { //
			{ "VARCHAR2", "string" }, //
			{ "CHAR", "string" }, //
			{ "DATE", "timestamp, format: '%Y-%m-%d' " }, //
			{ "TIMESTAMP(6)", "timestamp, format: '%Y-%m-%d %k:%M:%S' " }, //
			{ "NUMBER", "long" }, //
	};

	private String tableName;
	private List<String[]> colList;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateEmbulkScr target = new CreateEmbulkScr();
		if (args.length < 1) {
//			string[] a = { "qv_hatsudensha_keiryo_l", "dmdl" };
//			String[] a = { "bts_chiten_himozuke_eigyo", "tocsv" };
			String[] a = { "BTS_CHITEN_HIMOZUKE", "todb" };
			args = a;
		}
		target.tableName = args[0];
		try {
			target.exec(args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public CreateEmbulkScr() {
		typeMap = new HashMap<String, String>();
		for (String[] dat : typeData) {
			typeMap.put(dat[0], dat[1]);
		}
		colList = new ArrayList<String[]>();
	}

	private void exec(String type) throws Exception {
		getMeta(tableName.toUpperCase());
		if (colList.size() < 1) {
			logger.info("no data table=" + tableName);
			System.exit(0);
		}
		// output
		String ret = "";
		if ("dmdl".equalsIgnoreCase(type)) {
			ret = outputDmdl();
		} else if ("tocsv".equalsIgnoreCase(type)) {
			ret = outputToCsv();
		} else if ("todb".equalsIgnoreCase(type)) {
			ret = outputDpToDb();
		}
		System.out.println(ret);
	}

	private String outputToCsv() {
		StringBuffer sb = new StringBuffer();

		sb.append("{% include 'myenv' %}\n");
		sb.append("\n");
		sb.append("in:\n");
		sb.append("  type: oracle\n");
		sb.append("  driver_path: {{my_driver_path}}\n");
		sb.append("  url: {{my_url}}\n");
		sb.append("  user: {{my_usr}}\n");
		sb.append("  password: {{my_pass}}\n");
		sb.append("  table: " + tableName + "\n");
		sb.append("  select: \"*\"\n");
		sb.append(convWhere());
		sb.append("  columns:\n");

		for (String[] dat : colList) {
			sb.append("  - {name: " + dat[1].toUpperCase() + ", type: " + convType(dat[2], dat[3]) + "}\n");
		}

		sb.append("out:\n");
		sb.append("  type: file\n");
		sb.append("  path_prefix: {{env.FILE_BASE_PATH}}/" + getDirectory() + "/" + tableName.toLowerCase()
				+ "/{{env.NEW_DATETIME}}/" + tableName.toLowerCase() + "\n");
		sb.append("  file_ext: csv\n");
		sb.append("  formatter:\n");
		sb.append("    type: csv\n");
		sb.append("    charset: UTF-8\n");
		sb.append("    newline: LF\n");
		sb.append("    delimiter: ','\n");
		sb.append("    quote: '\"'\n");
		sb.append("    escape: '\"'\n");
		sb.append("    header_line: false\n");
		sb.append("    trim_if_not_quoted: false\n");
		sb.append("    allow_extra_columns: false\n");
		sb.append("    allow_optional_columns: false\n");
		sb.append("    default_timezone: 'Asia/Tokyo'\n");

		return sb.toString();
	}

	private String outputDpToDb() {
		StringBuffer sb = new StringBuffer();

		sb.append("{% include 'myenv' %}\n");
		sb.append("\n");
		sb.append("in:\n");
		sb.append("  type: file\n");
		sb.append("  path_prefix: {{my_in_file}}/" + tableName.toLowerCase() + "/" + tableName.toLowerCase() + "\n");
		sb.append("  parser:\n");
		sb.append("    charset: UTF-8\n");
		sb.append("    newline: LF\n");
		sb.append("    type: csv\n");
		sb.append("    delimiter: ','\n");
		sb.append("    quote: '\"'\n");
		sb.append("    escape: '\"'\n");
		sb.append("    trim_if_not_quoted: false\n");
		sb.append("    skip_header_lines: 0\n");
		sb.append("    allow_extra_columns: false\n");
		sb.append("    allow_optional_columns: false\n");
		sb.append("    columns:\n");
		for (String[] dat : colList) {
			sb.append("    - {name: " + dat[1].toUpperCase() + ", type: " + convType(dat[2], dat[3]) + "}\n");
		}
		sb.append("out:\n");
		sb.append("  type: oracle\n");
		sb.append("  driver_path: {{my_driver_path}}\n");
		sb.append("  url: {{my_url}}\n");
		sb.append("  user: {{my_usr}}\n");
		sb.append("  password: {{my_pass}}\n");
		sb.append("  table: " + convDpTableName().toUpperCase() + "\n");
		sb.append("  options: {LoginTimeout: 20000}\n");
		sb.append("  mode: truncate_insert\n");
		sb.append("  insert_method: direct\n");
		sb.append("\n");

		return sb.toString();
	}

	private String convDpTableName() {
		if (tableName.startsWith("BTS_")) {
			return "dpdb_" + tableName.substring(4, tableName.length());
		}
		return "dpdc_" + tableName.substring(5, tableName.length());
	}

	private String getDirectory() {
		if (this.tableName.startsWith("BTS_")) {
			return "blenderts";
		}
		return "btscustom";
	}

	private String convWhere() {
		if (colList.size() > 0) {
			if ("LAST_MODIFIED".equalsIgnoreCase(this.colList.get(colList.size() - 1)[1])) {
				return "  where: to_char(LAST_MODIFIED,'YYYYMMDDHH24MISS')>'{{ env.LAST_UPD }}'\n";
			}
		}
		return "";
	}

	private String outputDmdl() {
		StringBuffer sb = new StringBuffer();
		sb.append("\"" + tableName + "\"");
		sb.append("@namespace(value = db.qv)");
		sb.append("@windgate.jdbc.table(name = \"" + tableName + "\")");
		sb.append("@directio.csv");
		sb.append(tableName.toLowerCase() + " = {");
		for (String[] dat : colList) {
			sb.append( //
					"\t\t\"" + dat[1] + "\"\n" + //
							"\t\t@windgate.jdbc.column(name = \"" + dat[1] + "\")\n" + //
							"\t\t" + dat[1].toLowerCase() + " : " + convType(dat[2], dat[3]) + ";\n");
		}
		sb.append("};");
		return sb.toString();
	}

	private void getMeta(String tableName) throws Exception {
		Connection connection = null;

		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection("jdbc:oracle:thin:@54.64.155.66:1521:orcl", DB_USER, DB_PASS);

		String[] COLNAMES = { "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "DATA_SCALE" };
		try {
			Statement statement = (Statement) connection.createStatement();
			ResultSet result = null;
			result = statement.executeQuery(
					"select t.TABLE_NAME,t.COLUMN_NAME,DATA_TYPE,DATA_SCALE,COMMENTS " + "from USER_TAB_COLUMNS t "
							+ "join USER_COL_COMMENTS c on t.TABLE_NAME=c.TABLE_NAME and t.COLUMN_NAME=c.COLUMN_NAME "
							+ "where t.TABLE_NAME='" + tableName + "' " + "order by COLUMN_ID ");
			while (result.next()) {
				String[] dat = new String[COLNAMES.length];
				int i = 0;
				for (String col : COLNAMES) {
					dat[i++] = result.getString(col);
				}
				colList.add(dat);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String convType(String org, String scale) {
		if (!typeMap.containsKey(org)) {
			throw new IllegalArgumentException("table:" + tableName + " type:" + org + " cant mapping");
		}
		if (scale != null) {
			if (Integer.parseInt(scale) > 0) {
				return "double";
			} else {
				return "long";
			}
		}
		return typeMap.get(org);
	}
}
