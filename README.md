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
11/13
* 出力結果に結果(res)のキー重複件数を追加。
* BTSC_KEIYAKU_CHECK key:0,1 のキー項目を変更
* BTSC_KEIYAKU_CHECK_NEW key:3,4,5,6,7,8 修正。新規の採番項目は一致できないため内容項目をキーとして比較。
11/14
* BTSC_IDO_INFO_LIST key:1, 2, 3, 4, 5, 6, 7
* BTS_HIKINUKI_L key:0, 1, 2
* IF0101 key:0,1,2
* IF0101_WATARI key:0,1,2s
* IF0102 key:0,1,2
* IF0209 key:0,1,2
* IF0404 key:0,1,2
* IF0404_WATARI key:0,1,2
11/15
* IF0505 key:0,1,2,3,4
* 変更：BTSC_KEIYAKU_ERROR_INFO key:2, 3, 4, 5, 6, 7
11/20
* 変更 BTSC_IDO_INFO_LIST key:1 - 17
11/22
* 追加 BTSC_PF_LIST key:0, 2, 3, 4, 6, 7, 8, 9, 11
11/27
* 変更 BTS_K_KAKUTEI_J_L_EXT key:0,1,2,3,4
11/28
* 変更 BTSC_KEIYAKU_CHECK_NEW key:3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13　BTSC_KEIYAKU_CHECKデータを自動採番以外全項目キーとして使用するときにはデータ名を「BTSC_KEIYAKU_CHECK_NEW」で使用してくだい。
12/1
* BTSC_DOUJIDORYO_FILE_STATUS 更新日次を比較除外に変更。
* BTSH001のupdateファイル比較プログラム別途追加。DiffTxt
12/3
* BTSC_BP_EXT_STATUS キー１項目追加。key = { 0, 1, 2, 3, 4 };