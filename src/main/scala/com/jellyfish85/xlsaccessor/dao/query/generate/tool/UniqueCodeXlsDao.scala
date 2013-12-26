package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import com.jellyfish85.xlsaccessor.constant.AppConst
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.UniqueCodeXlsBean

import org.apache.poi.ss.usermodel.{FormulaEvaluator, Row, Cell, Sheet, Workbook}
import org.apache.commons.lang.StringUtils


/**
 * == UniqueCodeXlsDao ==
 *
 * find unique code xls data and return its bean list
 *
 * @author wada shunsuke
 * @since  2013/12/24
 *
 */
class UniqueCodeXlsDao extends GeneralXlsDao {

  /**
   * get unique code's header information
   *
   * @param path
   * @return
   */
  def getHeaderInfo(path: String) :UniqueCodeXlsBean = {

    val xlsBean: UniqueCodeXlsBean = new UniqueCodeXlsBean
    xlsBean.path     = path

    var workBook: Workbook = null
    try{
      workBook         = manager.workbook(path)
      val evaluator    = workBook.getCreationHelper.createFormulaEvaluator
      val sheet: Sheet = workBook.getSheet(prop.generalCodeDefineSheetName)
      val row: Row     = sheet.getRow(prop.uniqueCodeDefineRowHeader)

      xlsBean.fileName = workBook.getName(path).toString
      xlsBean.codeId =
        utils.convertCellValue2String(row, evaluator, prop.uniqueCodeDefineColumnMap("codeId"))
      xlsBean.logicalCodeName   =
        utils.convertCellValue2String(row, evaluator, prop.uniqueCodeDefineColumnMap("logicalCodeName"))
      xlsBean.logicalTableName  =
        utils.convertCellValue2String(row, evaluator, prop.uniqueCodeDefineColumnMap("logicalTableName"))
      xlsBean.physicalTableName =
        utils.convertCellValue2String(row, evaluator, prop.uniqueCodeDefineColumnMap("physicalTableName"))

      if (StringUtils.isBlank(xlsBean.physicalTableName)) {
        println("[ERROR]" + xlsBean.fileName)
        println("[ERROR]" + "there is no code's physical table name")
        throw new RuntimeException()
      }

      //generate delete query
      xlsBean.deleteQuery  = "DELETE FROM SCHEMA_NAME." + xlsBean.physicalTableName

    } catch {
      case e =>
        xlsBean.errorMessage = "EXCEL FORMAT"
        println("[ERROR]" + xlsBean.path)
        e.printStackTrace()

    } finally {
      workBook = null
    }

    xlsBean
  }

  /**
   * == specifySpan ==
   *
   * specify code define data span
   *
   * @param xlsBean
   * @param evaluator
   */
  def specifySpan(row: Row ,xlsBean: UniqueCodeXlsBean, evaluator: FormulaEvaluator): Unit = {
    var flg = true

    var idx = 1
    var cell: Cell = null
    var checkVal :String = AppConst.STRING_BLANK
    while(flg) {
      cell = row.getCell(idx)
      checkVal = utils.convertCellValue2String(cell, evaluator)

      if (checkVal == prop.uniqueCodeDefineColumnStopper) {
        xlsBean.endPos = idx
        flg = false
        return

      } else if (idx > 1000) {
        xlsBean.endPos = 0
        println("the code define format is wrong, please add column xxxxxxxx.")
        return
      }

      idx += 1
    }
  }

  /**
   * == getDataEntry ==
   * 
   * get data entry
   *
   * @param path
   * @return
   */
  def getDataEntry(path: String): List[Map[Int, String]] = {
    var list: List[Map[Int, String]] = List()

    val uniqueCodeInfo: UniqueCodeXlsBean = getHeaderInfo(path)

    var workBook: Workbook = null
    try{
      workBook         = manager.workbook(path)
      val evaluator    = workBook.getCreationHelper.createFormulaEvaluator
      val sheet: Sheet = workBook.getSheet(prop.generalCodeDefineSheetName)

      var checkVal :String   = AppConst.STRING_BLANK
      var row      :Row      = null
      var flg      :Boolean  = true

      var idx = prop.uniqueCodeDefineRowDataStartPos
      while(flg) {
        row = sheet.getRow(idx)

        if (row == null) {
          flg = false
          return list
        }
        if (row.getCell(prop.uniqueCodeDefineColumnMap("codeId")) == null) {
          flg = false
          return list
        }

        checkVal =
          utils.convertCellValue2String(row, evaluator, prop.uniqueCodeDefineColumnMap("codeId"))
        if (StringUtils.isBlank(checkVal)) {
          flg = false
          return list
        }

        var map: Map[Int, String]  = Map()
        for (i <- uniqueCodeInfo.startPos until uniqueCodeInfo.endPos) {
          map +=
            (i -> utils.convertCellValue2String(row, evaluator, i))
        }

        list ::= map
        idx += 1
      }
    }
    list
  }
}