package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.io.{IOException, FileNotFoundException}
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import org.apache.poi.ss.usermodel.{Row, Sheet}
import java.math.BigDecimal
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.constant.AppConst
import org.apache.commons.lang.StringUtils

/**
 * == TemplateRecordXlsDao ==
 *
 * find template recode beans from excel file
 *
 * @author wada shunsuke
 * @since  2014/01/01
 *
 */
@throws(classOf[NullPointerException])
@throws(classOf[FileNotFoundException])
@throws(classOf[IOException])
class TemplateRecordXlsDao(path: String) extends GeneralXlsDao[TemplateRecordXlsBean](path: String) {

  val sheet: Sheet                 = workBook.getSheet(prop.templateRecordDefineSheetName)

  /**
   * == findAll ==
   *
   * find all code from excel file
   *
   *
   * @author wada shunsuke
   * @since  2014/01/01
   * @param ticketNumber
   * @param svnRequestBean
   * @return
   */
  def findAll(ticketNumber:   BigDecimal, svnRequestBean: SVNRequestBean): List[TemplateRecordXlsBean] = {
    var results: List[TemplateRecordXlsBean] = List()

    try{
      var flg: Boolean     = true
      var idx: Int         = AppConst.INT_ONE
      var checkVal: String = AppConst.STRING_BLANK
      var row: Row         = sheet.getRow(idx)

      while (flg) {
        row = sheet.getRow(idx)
        if (row == null) {
          flg = false
        }

        idx += 1
        if (flg) {
          if (row.getCell(prop.templateRecordDefineColumnMap("recordId")) == null) {
            flg = false
          }

          checkVal = row.getCell(prop.templateRecordDefineColumnMap("recordId")).getStringCellValue()
          if (StringUtils.isBlank(checkVal)){
            flg = false
          }

          val bean: TemplateRecordXlsBean = new TemplateRecordXlsBean

          bean.path              = svnRequestBean.path
          bean.fileName          = svnRequestBean.fileName
          bean.revision          = svnRequestBean.revision
          bean.author            = svnRequestBean.author
          bean.commitYmd         = svnRequestBean.commitYmd
          bean.commitHms         = svnRequestBean.commitHms

          //todo modify for template record table attribute
          bean.logicalTableName  = prop.generalCodeLogicalTableName
          bean.physicalTableName = prop.generalCodePhysicalTableName

          bean.recordId          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordId"))
          bean.recordKind          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordKind"))
          bean.templateName          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templateName"))
          bean.displayName          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("displayName"))
          bean.templatePath          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templatePath"))
          bean.customerId          =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("customerId"))

          results ::= bean
        }
      }
    }

    results
  }

}
