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
public class CreateMergeSql {

	static Logger logger = LoggerFactory.getLogger(DiffCsv.class);
	static String DB_USER = "usr1";
	static String DB_PASS = "pass1";
	private static String LF = "\n";

//	private Map<String, String> typeMap;
//	private String[][] typeData = { //
//			{ "VARCHAR2", "string" }, //
//			{ "CHAR", "string" }, //
//			{ "DATE", "timestamp, format: '%Y-%m-%d' " }, //
//			{ "TIMESTAMP(6)", "timestamp, format: '%Y-%m-%d %k:%M:%S' " }, //
//			{ "NUMBER", "long" }, //
//	};

	private String batchId;
	private String procName;
	private String tableName;
	private List<String[]> colList;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateMergeSql target = new CreateMergeSql();
		if (args.length < 1) {
			logger.info("引数なしのため、テスト用に以下の内容にて実行します。");
			logger.info("BTSE001, DPS_BTSE001_001, BTS_YUKO_KWH_L_EXT");
			String[] a = { "BTSE001", "DPS_BTSE001_001", "BTS_YUKO_KWH_L_EXT" };
			args = a;
		}
		target.batchId = args[0];
		target.procName = args[1];
		target.tableName = args[2];
		try {
			target.exec();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CreateMergeSql() {
//		typeMap = new HashMap<String, String>();
//		for (String[] dat : typeData) {
//			typeMap.put(dat[0], dat[1]);
//		}
		colList = new ArrayList<String[]>();
	}

	private void exec() throws Exception {
		getMeta(tableName.toUpperCase());
		if (colList.size() < 1) {
			logger.info("no data table=" + tableName);
			System.exit(0);
		}
		// output
		System.out.println(outputMergeSql());
	}

	private String outputMergeSql() {
		StringBuffer sb = new StringBuffer();
		// テーブル名
//			sb.append(tableName + "\n");
//			sb.append(getPreTableName() + "\n");
//			sb.append(getCoreTableName() + "\n");
		// キー項目
//			sb.append("key:" + String.join(",", getKey()) + "\n");
		// キー以外
//			sb.append("col:" + String.join(",", getNotKey()) + "\n");

		// SQL MERGE 文
		// コメント
		sb.append("-- batch id : " + batchId + LF);
		sb.append("-- source table : " + getPreDpdTableName() + "_" + getCoreTableName() + LF);
		sb.append("-- target table : " + tableName + LF);
		sb.append("-- re-write pattern : A" + LF);
		sb.append("CREATE OR REPLACE PROCEDURE " + procName + LF);
		sb.append("IS" + LF);
		sb.append("BEGIN" + LF);
		sb.append("  MERGE INTO " + tableName + " T1" + LF);
		sb.append("  USING " + getPreDpdTableName() + "_" + getCoreTableName() + " T2" + LF);
		sb.append("    ON (");

		sb.append(createKeySql());

		sb.append(" )" + LF);
		sb.append("  WHEN MATCHED THEN" + LF);
		sb.append("    UPDATE SET" + LF);

		sb.append("    " + setUpdateSql() + LF);

		sb.append("  WHEN NOT MATCHED THEN" + LF);
		sb.append("    INSERT (");
		sb.append(insertKeysSql());
		sb.append(")" + LF);
		sb.append("    VALUES (");
		sb.append(insertColsSql());
		sb.append(");" + LF);

		sb.append("DBMS_OUTPUT.PUT_LINE('更新した件数は' || SQL%ROWCOUNT || '件です。');" + LF);
		sb.append("END;" + LF);
		sb.append("/");

		return sb.toString();
	}

	private String createKeySql() {
		StringBuffer sb = new StringBuffer();
		for (String key : getKey()) {
			if (sb.length() > 0) {
				sb.append(" AND");
			}
			sb.append(" T1." + key + " = T2." + key);
			if (sb.length() > 100) {
				sb.append(LF);
			}
		}
		return sb.toString();
	}

	private String setUpdateSql() {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String key : getNotKey()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			if (count > 100) {
				sb.append(LF);
				count = 0;
			}
			sb.append(" T1." + key + " = T2." + key);
			count += key.length() * 2 + 10;
		}
		return sb.toString();
	}

	private String insertKeysSql() {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String key : getAllCol()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			if (count > 100) {
				sb.append(LF);
				count = 0;
			}
			sb.append(key);
			count += key.length();
		}
		return sb.toString();
	}

	private String insertColsSql() {
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String key : getAllCol()) {
			if (sb.length() > 0) {
				sb.append(",");
			}
			if (count > 100) {
				sb.append(LF);
				count = 0;
			}
			sb.append("T2." + key);
			count += key.length() + 3;
		}
		return sb.toString();
	}

	private String getPreDpdTableName() {
		if (tableName.startsWith("BTS_")) {
			return "DPDB";
		}
		return "DPDC";
	}

	private String getCoreTableName() {
		return tableName.substring(tableName.indexOf("_") + 1, tableName.length());
	}

	private List<String> getAllCol() {
		List<String> retList = new ArrayList<String>();
		retList.addAll(getKey());
		retList.addAll(getNotKey());
		return retList;
	}

	private List<String> getKey() {
		List<String> retList = new ArrayList<String>();
		for (String[] dat : colList) {
			if (!dat[3].equals("0")) {
				retList.add(dat[1]);
			}
		}
		return retList;
	}

	private List<String> getNotKey() {
		List<String> retList = new ArrayList<String>();
		for (String[] dat : colList) {
			if (dat[3].equals("0")) {
				retList.add(dat[1]);
			}
		}
		return retList;
	}

	private void getMeta(String tableName) throws Exception {
		Connection connection = null;

		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection("jdbc:oracle:thin:@54.64.155.66:1521:orcl", DB_USER, DB_PASS);

		String[] COLNAMES = { "TABLE_NAME", "COLUMN_NAME", "ORDINAL_POSITION", "IS_PRIMARY_KEY", "PK_CONSTRAINT_NAME",
				"PK_KEY_ORDINAL", "IS_UNIQUE_KEY", "UQ_CONSTRAINT_NAME", "UQ_KEY_ORDINAL", "COLUMN_DATA_TYPE",
				"COLUMN_DEFAULT", "IS_NULLABLE", "MAX_LENGTH", "PRECISION", "SCALE", "COLUMN_COMMENT" };
		try {
			Statement statement = (Statement) connection.createStatement();
			ResultSet result = null;
			result = statement.executeQuery(getMetaSql());
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

	private String getMetaSql() {
		String sql = "SELECT"
//							+"--      SYS_CONTEXT('USERENV', 'DB_NAME') AS db_name"
//							+"--     ,USER                              AS schema_name"
				+ "    cols.TABLE_NAME                   AS table_name"
				+ "    ,cols.COLUMN_NAME                  AS column_name"
				+ "    ,cols.COLUMN_ID                    AS ordinal_position"
//							+"    /* PKか否かの判別フラグ */"
				+ "    ,(CASE" + "        WHEN pk_cols.CONSTRAINT_NAME IS NULL THEN" + "            0" + "        ELSE"
				+ "            1" + "     END)                              AS is_primary_key"
//							+"    /* PK制約名 */"
				+ "    ,pk_cols.CONSTRAINT_NAME           AS pk_constraint_name"
//							+"    /* PK制約カラム位置 */"
				+ "    ,pk_cols.KEY_ORDINAL               AS pk_key_ordinal"
//							+"    /* UQか否かの判別フラグ */"
				+ "    ,(CASE" + "        WHEN uq_cols.CONSTRAINT_NAME IS NULL THEN" + "            0" + "        ELSE"
				+ "            1" + "     END)                              AS is_unique_key"
//							+"    /* UQ制約名 */"
				+ "    ,uq_cols.CONSTRAINT_NAME           AS uq_constraint_name"
//							+"    /* UQ制約カラム位置 */"
				+ "    ,uq_cols.KEY_ORDINAL               AS uq_key_ordinal"
				+ "    ,cols.DATA_TYPE                    AS column_data_type"
				+ "    ,cols.DATA_DEFAULT                 AS column_default"
				+ "    ,cols.NULLABLE                     AS is_nullable"
				+ "    ,cols.DATA_LENGTH                  AS max_length"
				+ "    ,cols.DATA_PRECISION               AS precision"
				+ "    ,cols.DATA_SCALE                   AS scale"
				+ "    ,comments.COMMENTS                 AS column_comment" + " FROM" + "    USER_TABLES tbls"
				+ "        INNER JOIN USER_TAB_COLUMNS cols" + "        ON"
				+ "            cols.TABLE_NAME = tbls.TABLE_NAME" + "        LEFT OUTER JOIN USER_COL_COMMENTS comments"
				+ "        ON" + "                comments.TABLE_NAME  = cols.TABLE_NAME"
				+ "            AND comments.COLUMN_NAME = cols.COLUMN_NAME"
//							+"        /* PK情報を結合 */"
				+ "        LEFT OUTER JOIN (" + "            SELECT"
				+ "                 cons_inner.CONSTRAINT_NAME  AS constraint_name"
				+ "                ,cons_columns_inner.POSITION AS key_ordinal"
				+ "                ,tbls_inner.TABLE_NAME       AS table_name"
				+ "                ,cols_inner.COLUMN_NAME      AS col_name"
				+ "                ,cols_inner.COLUMN_ID        AS col_id" + "            FROM"
				+ "                USER_TABLES tbls_inner"
				+ "                    INNER JOIN USER_TAB_COLUMNS cols_inner" + "                    ON"
				+ "                        cols_inner.TABLE_NAME = tbls_inner.TABLE_NAME"
				+ "                    INNER JOIN USER_CONSTRAINTS cons_inner" + "                    ON"
				+ "                            cons_inner.OWNER           = USER"
				+ "                        AND cons_inner.CONSTRAINT_TYPE = 'P'"
				+ "                        AND cons_inner.TABLE_NAME      = tbls_inner.TABLE_NAME"
				+ "                    INNER JOIN USER_CONS_COLUMNS cons_columns_inner" + "                    ON"
				+ "                            cons_columns_inner.OWNER           = cons_inner.OWNER"
				+ "                        AND cons_columns_inner.CONSTRAINT_NAME = cons_inner.CONSTRAINT_NAME"
				+ "                        AND cons_columns_inner.TABLE_NAME      = cons_inner.TABLE_NAME"
				+ "                        AND cons_columns_inner.COLUMN_NAME     = cols_inner.COLUMN_NAME"
				+ "        ) pk_cols" + "        ON" + "                pk_cols.TABLE_NAME = cols.TABLE_NAME"
				+ "            AND pk_cols.COL_NAME   = cols.COLUMN_NAME"
				+ "            AND pk_cols.COL_ID     = cols.COLUMN_ID"
//							+"        /* UQ情報を結合 */"
				+ "        LEFT OUTER JOIN (" + "            SELECT"
				+ "                 cons_inner.CONSTRAINT_NAME  AS constraint_name"
				+ "                ,cons_columns_inner.POSITION AS key_ordinal"
				+ "                ,tbls_inner.TABLE_NAME       AS table_name"
				+ "                ,cols_inner.COLUMN_NAME      AS col_name"
				+ "                ,cols_inner.COLUMN_ID        AS col_id" + "            FROM"
				+ "                USER_TABLES tbls_inner"
				+ "                    INNER JOIN USER_TAB_COLUMNS cols_inner" + "                    ON"
				+ "                        cols_inner.TABLE_NAME = tbls_inner.TABLE_NAME"
				+ "                    INNER JOIN USER_CONSTRAINTS cons_inner" + "                    ON"
				+ "                            cons_inner.OWNER           = USER"
				+ "                        AND cons_inner.CONSTRAINT_TYPE = 'U'"
				+ "                        AND cons_inner.TABLE_NAME      = tbls_inner.TABLE_NAME"
				+ "                    INNER JOIN USER_CONS_COLUMNS cons_columns_inner" + "                    ON"
				+ "                            cons_columns_inner.OWNER           = cons_inner.OWNER"
				+ "                        AND cons_columns_inner.CONSTRAINT_NAME = cons_inner.CONSTRAINT_NAME"
				+ "                        AND cons_columns_inner.TABLE_NAME      = cons_inner.TABLE_NAME"
				+ "                        AND cons_columns_inner.COLUMN_NAME     = cols_inner.COLUMN_NAME"
				+ "        ) uq_cols" + "        ON" + "                uq_cols.TABLE_NAME = cols.TABLE_NAME"
				+ "            AND uq_cols.COL_NAME   = cols.COLUMN_NAME"
				+ "            AND uq_cols.COL_ID     = cols.COLUMN_ID" + " where tbls.TABLE_NAME='" + tableName + "'"
				+ " ORDER BY" + "    ordinal_position";

		return sql;
	}
}
