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
public class CreateDmdl {

	static Logger logger = LoggerFactory.getLogger(DiffCsv.class);
	private Map<String, String> typeMap;
	private String[][] typeData = { //
			{ "VARCHAR2", "TEXT" }, //
			{ "CHAR", "TEXT" }, //
			{ "DATE", "DATE" }, //
			{ "TIMESTAMP(6)", "DATETIME" }, //
			{ "NUMBER", "DECIMAL" }, //
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateDmdl target = new CreateDmdl();
		if (args.length < 1) {
			String[] a = { "QV_HATSUDENSHA_KEIRYO_L" };
			args = a;
		}
		target.exec(args[0]);

	}

	public CreateDmdl() {
		typeMap = new HashMap<String, String>();
		for (String[] dat : typeData) {
			typeMap.put(dat[0], dat[1]);
		}
	}

	private void exec(String tableName) {
		Connection connection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@54.64.155.66:1521:orcl", "usr1", "pass1");
			logger.info("connect OK");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String[]> colList = new ArrayList<String[]>();
		String[] COLNAMES = { "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE" };
		try {
			Statement statement = (Statement) connection.createStatement();
			ResultSet result = null;
			result = statement
					.executeQuery("select t.TABLE_NAME,t.COLUMN_NAME,DATA_TYPE,COMMENTS from USER_TAB_COLUMNS t "
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
		// output
		System.out.println("\"" + tableName + "\"");
		System.out.println("@namespace(value = db.qv)");
		System.out.println("@windgate.jdbc.table(name = \"" + tableName + "\")");
		System.out.println("@directio.csv");
		System.out.println(tableName.toLowerCase() + " = {");
		for (String[] dat : colList) {
			System.out.println( //
					"\t\t\"" + dat[1] + "\"\n" + //
							"\t\t@windgate.jdbc.column(name = \"" + dat[1] + "\")\n" + //
							"\t\t" + dat[1].toLowerCase() + " : " + convType(dat[2]) + ";\n");
		}
		System.out.println("};");
	}

	private String convType(String org) {
		if (!typeMap.containsKey(org)) {
			throw new IllegalArgumentException("type:" + org + " cant mapping");
		}
		return typeMap.get(org);
	}
}
