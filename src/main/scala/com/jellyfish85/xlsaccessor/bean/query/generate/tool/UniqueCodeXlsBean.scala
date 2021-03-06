package com.jellyfish85.xlsaccessor.bean.query.generate.tool

/**
 * == UniqueCodeXlsBean ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/25
 *
 */
class UniqueCodeXlsBean extends GeneralXlsBean {

  var codeId  : String = _
  var logicalCodeName :String = _
  var physicalCodeName :String = _

  var subsystemCd: String = _

  var logicalKeyName:    String = _
  var physicalKeyName:   String = _

  var codeValue: String = _

  var shortName: String = _
  var displayOrder: Int = _

  var insertSQLHeader :String = _

  var startPos :Int = _
  var endPos   :Int = _

  var insertQuery :String = _
  var deleteQuery :String = _

  var errorMessage :String = ""

  var delFlg: String = "0"

  var ignoreFlg: String = "0"

}
