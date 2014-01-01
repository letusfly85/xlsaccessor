package com.jellyfish85.xlsaccessor.bean.query.generate.tool

import java.math.BigDecimal

/**
 * == GeneralXlsBean ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/16
 *
 */
class GeneralXlsBean {

  var logicalTableName: String  = _
  var physicalTableName: String = _

  var path:     String = _
  var fileName: String = _

  var ticketNumber: BigDecimal = _
  var revision    : Long   = _
  var author      :   String = _

  var commitYmd: String = _
  var commitHms: String = _

  var sheetName: String = _

}
