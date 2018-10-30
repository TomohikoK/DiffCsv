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
* BTS_YUKO_KWH_L_EXT
* BTSC_BEFORE_CALC_CHK_L
* BTSC_BEFORE_CALC_CHK_L_DETAIL
* BTSC_CALC_CHK_CNT
* BTSC_YUKO_KWH_L_EXT
* IF0511
10/30追加
* BTSC_BP_EXT_STATUS
* BTS_K_KAKUTEI_J_L_EXT