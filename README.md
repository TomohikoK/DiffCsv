# DiffCsv
## 引数
* -e  期待するファイルパス。ここで指定した場所に存在する[データ名]で始まるcsvファイルが対象となる。
* -r  実行結果パス。指定したパスから下位に存在するcsvファイルを全て対象とする。
* -b  バッチ名。（廃止予定）
* -d  データ名。データの比較方法を指定するためのデータ名。及び期待するファイルを検索するときにここで指定した文字列で始まるファイルを対象とする。
* -targetCol  期待するデータの抽出条件カラムNo。期待するデータに余分なものが入っている場合に特定項目の一致条件にて対象から外すことが可能。（非推奨）
* -targetValue  期待するデータの抽出条件のカラムの値。（非推奨）
* -l  比較結果明細出力可否。デフォルトは　true
## 対応データ
* BTS_YUKO_KWH_L_EXT key:1,2,3

* BTSC_BEFORE_CALC_CHK_L key:0, 1, 2, 3, 4, 7, 8, 9, 10, 11, 12, 13
* BTSC_BEFORE_CALC_CHK_L_DETAIL key:0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
* BTSC_CALC_CHK_CNT key:0,1,2,3,4
* BTSC_YUKO_KWH_L_EXT  key:0,1,2,3,4
* IF0511 key:0,1,2
10/30追加
* BTSC_BP_EXT_STATUS key:0,1,2,3,4
* BTS_K_KAKUTEI_J_L_EXT key:1,3,4
11/1追加
* BTSC_IF0101_RENKEI_DATA key:25
* BTSC_IF0102_RENKEI_DATA key:2,4,6,9
* BTSC_IF0209_RENKEI_DATA key:7
* BTSC_IF0404_RENKEI_DATA key:9
* BTSC_KEIYAKU_CHECK key:3,4,5,6
11/2追加
* BTS_YUKO_KWH_L_EIGYO_EXT key:0,1,2,3
* BTSC_SHIJISU_CHK_RES key:0,1,2,3,4,5,6,7,8
* BTSC_SHIJISU_IF key:0,1,2,3,4,5,6,7
* BTSC_YUKO_KWH_MONTH_L_EXT key:0,1,2,3,4
* IF05-06 key:0,1,2,3,4,5,6
* IF05-14 key:0,1,2,3,4,5,6
11/6追加
* BTSC_KEIYAKU_ERROR_INFO key:3, 4
* BTSC_K_SIJISU_J_L_EXT key:0, 1, 2, 3, 4, 5, 6
11/9追加
* BTSC_DOUJIDORYO_FILE_STATUS key:0, 2, 3
