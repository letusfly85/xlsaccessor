package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.io.{IOException, FileNotFoundException}
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.{XlsColumnAttribute, TemplateRecordXlsBean}
import org.apache.poi.ss.usermodel.{Row, Sheet}
import java.math.BigDecimal
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.xlsaccessor.constant.AppConst
import org.apache.commons.lang.StringUtils
import java.util

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

  val sheet: Sheet        = workBook.getSheet(prop.templateRecordDefineSheetName)

  var beanRecordId: XlsColumnAttribute = _

  def getHeader(): util.ArrayList[XlsColumnAttribute] = {
    val xlsBeans: util.ArrayList[XlsColumnAttribute] = new util.ArrayList[XlsColumnAttribute]()

    val row: Row = sheet.getRow(prop.templateRecordDefineRowColumnHeader)

    val recordId:      String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordId"))
    val recordKind:    String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordKind"))
    val templateName:  String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templateName"))
    val displayName:   String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("displayName"))
    val templatePath:  String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templatePath"))
    val customerId:    String =
      utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("customerId"))
    
    this.beanRecordId =
      new XlsColumnAttribute(
        prop.templateRecordDefineLogicalTableName,
        prop.templateRecordDefinePhysicalTableName,
        AppConst.STRING_BLANK,
        recordId,
        AppConst.STRING_BLANK,
        AppConst.INT_ZERO
      )

    val beanRecordKind:    XlsColumnAttribute = beanRecordId.copy(recordKind)
    val beanTemplateName:  XlsColumnAttribute = beanRecordId.copy(templateName)
    val beanDisplayName:   XlsColumnAttribute = beanRecordId.copy(displayName)
    val beanTemplatePath:  XlsColumnAttribute = beanRecordId.copy(templatePath)
    val beanCustomerId:    XlsColumnAttribute = beanRecordId.copy(customerId)

    xlsBeans.add(beanRecordId)
    xlsBeans.add(beanRecordKind)
    xlsBeans.add(beanTemplateName)
    xlsBeans.add(beanDisplayName)
    xlsBeans.add(beanTemplatePath)
    xlsBeans.add(beanCustomerId)

    xlsBeans
  }

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
        idx += 1
        row = sheet.getRow(idx)
        if (row == null) {
          flg = false
        }

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

          bean.logicalTableName  = prop.templateRecordDefineLogicalTableName
          bean.physicalTableName = prop.templateRecordDefinePhysicalTableName

          bean.recordId       =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordId"))
          bean.recordKind     =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("recordKind"))
          bean.templateName   =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templateName"))
          bean.displayName    =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("displayName"))
          bean.templatePath   =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("templatePath"))
          bean.customerId     =
            utils.convertCellValue2String(row, evaluator, prop.templateRecordDefineColumnMap("customerId"))

          results ::= bean
        }
      }
    }

    results
  }

}
