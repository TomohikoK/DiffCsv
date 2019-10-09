package com.hoge;

import java.util.Calendar;

public class Main {

	public static void main(String[] args) {
		Main target = new Main();
//		System.out.println(target.getSQL());
		
		Calendar cal = Calendar.getInstance();
		System.out.println("today = " + cal.getTime().toString());
		cal.add(Calendar.MONTH, 1);
		System.out.println("+1 month = " + cal.getTime().toString());
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		System.out.println("-1 month = " + cal.getTime().toString());
	}
	/**
	 * E003がE009を呼ぶ条件のSQL
	 * @return
	 */
	public static String getSQL(){
		StringBuffer sb = new StringBuffer();

		sb.append("SELECT ");
		sb.append("   (CASE WHEN SUBSTR(CHZ.JIGYOSHA_ID,1,1) = 'D' THEN JNI.NITTEI ");
		sb.append("    ELSE HNI.NITTEI END) AS nittei ");
		sb.append("  , YKM.CHITEN_ID AS chitenId ");
		sb.append("  , YKM.MDMID AS mdmId ");
		sb.append("  , YKM.KEIKI_ID AS keikiId ");
		sb.append("  , YKM.TARGET_MONTH AS targetMonth ");
		sb.append("  , YKM.KEIRYO_DATE AS keiryoDate ");
		sb.append("  , YKM.START_KENSHIN_DATE AS kenshinStartDate ");
		sb.append("  , YKM.END_KENSHIN_DATE AS kenshinEndDate ");
		sb.append("  , YKM.SHIYORYO_KEISAN_HOHO_CD AS shiyoryoKeisanHohoCd ");
		sb.append("  , YKM.END_KENSHIN_DATE - YKM.START_KENSHIN_DATE + 1 AS kikan ");
		sb.append("  , MRC.FROM_DAY AS zengetsuKenshinbi ");
		sb.append("  , MRC.FROM_DAY - YKM.START_KENSHIN_DATE AS zengetsuKeiryobiNissu ");
		sb.append("  ,NVL((SELECT YKM11.SHIJI1 FROM BTSC_YUKO_KWH_MONTH_L_EXT YKM11 WHERE ");
		sb.append("         YKM11.TARGET_MONTH = TO_CHAR(ADD_MONTHS(CONCAT(MRC.TARGET_MONTH,'01'),-1),'YYYYMM') ");
		sb.append("         AND YKM11.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM11.MDMID = YKM.MDMID ");
		sb.append("         AND YKM11.KEIKI_ID = YKM.KEIKI_ID), ");
		sb.append("       (SELECT YKM12.SHIJI1 FROM BTSC_K_SIJISU_J_L_EXT YKM12 WHERE ");
		sb.append("         YKM12.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM12.MDMID = YKM.MDMID ");
		sb.append("         AND YKM12.KEIKI_ID = YKM.KEIKI_ID ");
		sb.append("         AND YKM12.SHIJI_KBN = '4' ");
		sb.append("         AND YKM12.TARGET_DATE BETWEEN YKM.START_KENSHIN_DATE AND YKM.END_KENSHIN_DATE) ");
		sb.append("       ) AS kaishiSiji1 ");
		sb.append("  ,NVL((SELECT YKM21.SHIJI2 FROM BTSC_YUKO_KWH_MONTH_L_EXT YKM21 WHERE ");
		sb.append("         YKM21.TARGET_MONTH = TO_CHAR(ADD_MONTHS(CONCAT(MRC.TARGET_MONTH,'01'),-1),'YYYYMM') ");
		sb.append("         AND YKM21.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM21.MDMID = YKM.MDMID ");
		sb.append("         AND YKM21.KEIKI_ID = YKM.KEIKI_ID), ");
		sb.append("       (SELECT YKM22.SHIJI2 FROM BTSC_K_SIJISU_J_L_EXT YKM22 WHERE ");
		sb.append("         YKM22.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM22.MDMID = YKM.MDMID ");
		sb.append("         AND YKM22.KEIKI_ID = YKM.KEIKI_ID ");
		sb.append("         AND YKM22.SHIJI_KBN = '4' ");
		sb.append("         AND YKM22.TARGET_DATE BETWEEN YKM.START_KENSHIN_DATE AND YKM.END_KENSHIN_DATE) ");
		sb.append("       ) AS kaishiSiji2 ");
		sb.append("  ,NVL((SELECT YKM31.SHIJI3 FROM BTSC_YUKO_KWH_MONTH_L_EXT YKM31 WHERE ");
		sb.append("         YKM31.TARGET_MONTH = TO_CHAR(ADD_MONTHS(CONCAT(MRC.TARGET_MONTH,'01'),-1),'YYYYMM') ");
		sb.append("         AND YKM31.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM31.MDMID = YKM.MDMID ");
		sb.append("         AND YKM31.KEIKI_ID = YKM.KEIKI_ID), ");
		sb.append("       (SELECT YKM32.SHIJI3 FROM BTSC_K_SIJISU_J_L_EXT YKM32 WHERE ");
		sb.append("         YKM32.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM32.MDMID = YKM.MDMID ");
		sb.append("         AND YKM32.KEIKI_ID = YKM.KEIKI_ID ");
		sb.append("         AND YKM32.SHIJI_KBN = '4' ");
		sb.append("         AND YKM32.TARGET_DATE BETWEEN YKM.START_KENSHIN_DATE AND YKM.END_KENSHIN_DATE) ");
		sb.append("       ) AS kaishiSiji3 ");
		sb.append("  ,NVL((SELECT YKM41.SHIJI4 FROM BTSC_YUKO_KWH_MONTH_L_EXT YKM41 WHERE ");
		sb.append("         YKM41.TARGET_MONTH = TO_CHAR(ADD_MONTHS(CONCAT(MRC.TARGET_MONTH,'01'),-1),'YYYYMM') ");
		sb.append("         AND YKM41.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM41.MDMID = YKM.MDMID ");
		sb.append("         AND YKM41.KEIKI_ID = YKM.KEIKI_ID), ");
		sb.append("       (SELECT YKM42.SHIJI4 FROM BTSC_K_SIJISU_J_L_EXT YKM42 WHERE ");
		sb.append("         YKM42.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("         AND YKM42.MDMID = YKM.MDMID ");
		sb.append("         AND YKM42.KEIKI_ID = YKM.KEIKI_ID ");
		sb.append("         AND YKM42.SHIJI_KBN = '4' ");
		sb.append("         AND YKM42.TARGET_DATE BETWEEN YKM.START_KENSHIN_DATE AND YKM.END_KENSHIN_DATE) ");
		sb.append("       ) AS kaishiSiji4 ");
		sb.append("  , YKM.SHIJI1 AS endSiji1 ");
		sb.append("  , YKM.SHIJI2 AS endSiji2 ");
		sb.append("  , YKM.SHIJI3 AS endSiji3 ");
		sb.append("  , YKM.SHIJI4 AS endSiji4 ");
		sb.append("  , KIKAI_KEIKI.JYO_RATE AS jyoRate ");
		sb.append("  , SIJ.TARGET_DATE AS toritsukeDate ");
		sb.append("  , SIJ.TARGET_TIME AS toritsukeTime ");
		sb.append("  , TORITUKE_KEIKI.NINSHIKI_DATE AS ninshikiDate ");
		sb.append("  , TORITUKE_KEIKI.METER_KIND AS toritukeKeikiKind ");
		sb.append("  , CHZA.HAISHI_DATE AS haishiDate ");
		sb.append("  , CHZA.HAISHI_TIME_CODE AS haishiTimeCode ");
		sb.append("FROM ");
		sb.append("  BTSC_YUKO_KWH_MONTH_L_EXT YKM ");
		sb.append("  INNER JOIN BTS_CHITEN_HIMOZUKE CHZ ");
		sb.append("    ON CHZ.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("    AND YKM.KEIRYO_DATE BETWEEN CHZ.START_DATE AND CHZ.END_DATE ");
		sb.append("  INNER JOIN BTSC_CHITEN_HIMOZUKE CHZA ");
		sb.append("    ON CHZA.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("    AND CHZA.JIGYOSHA_ID = CHZ.JIGYOSHA_ID ");
		sb.append("  INNER JOIN BTSC_KEIKI KIKAI_KEIKI ");
		sb.append("    ON KIKAI_KEIKI.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("    AND KIKAI_KEIKI.MDM_ID = YKM.MDMID ");
		sb.append("    AND KIKAI_KEIKI.KEIKI_ID = YKM.KEIKI_ID ");
		sb.append("  LEFT JOIN QV_JYUYOSHA_NITTEI_L JNI ");
		sb.append("    ON JNI.JYUYOSHA_ID = CHZ.JIGYOSHA_ID ");
		sb.append("  LEFT JOIN QV_HATSUDENSHA_NITTEI_L HNI ");
		sb.append("    ON HNI.HATSUDENSHA_ID = CHZ.JIGYOSHA_ID ");
		sb.append("  INNER JOIN QV_METER_RANGE_CALENDAR MRC ");
		sb.append("    ON MRC.TARGET_MONTH = YKM.TARGET_MONTH ");
		sb.append("    AND MRC.FROM_DAY > YKM.START_KENSHIN_DATE ");
		sb.append("    AND MRC.NITTEI = ");
		sb.append("    (CASE WHEN SUBSTR(CHZ.JIGYOSHA_ID,1,1) = 'D' THEN JNI.NITTEI ");
		sb.append("     ELSE HNI.NITTEI END) ");
		sb.append("    AND MRC.CALENDAR_TYPE = '3' ");
		sb.append("  LEFT JOIN BTSC_K_SIJISU_J_L_EXT SIJ ");
		sb.append("    ON SIJ.CHITEN_ID = YKM.CHITEN_ID ");
		sb.append("    AND SIJ.MDMID = YKM.MDMID ");
		sb.append("    AND SIJ.TARGET_DATE >= YKM.END_KENSHIN_DATE ");
		sb.append("    AND SIJ.CONT_TYPE_ID = '1' ");
		sb.append("    AND SIJ.SHIJI_KBN = '2' ");
		sb.append("  LEFT JOIN BTSC_KEIKI TORITUKE_KEIKI ");
		sb.append("    ON TORITUKE_KEIKI.CHITEN_ID = SIJ.CHITEN_ID ");
		sb.append("    AND TORITUKE_KEIKI.MDM_ID = SIJ.MDMID ");
		sb.append("    AND TORITUKE_KEIKI.KEIKI_ID = SIJ.KEIKI_ID ");
		sb.append("WHERE ");
		sb.append("    YKM.START_KENSHIN_DATE >= '20160401' ");
		sb.append("ORDER BY ");
		sb.append("  targetMonth ");
		sb.append("  , nittei ");
		sb.append("  , chitenId ");
		sb.append("  , toritsukeDate ");
		sb.append("  , toritsukeTime ");

		return sb.toString();
	}
}
