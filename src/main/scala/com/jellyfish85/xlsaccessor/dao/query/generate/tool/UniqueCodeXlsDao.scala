package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import com.jellyfish85.xlsaccessor.constant.AppConst
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.{XlsColumnAttribute, UniqueCodeXlsBean}

import org.apache.poi.ss.usermodel.{FormulaEvaluator, Row, Cell, Sheet, Workbook}
import org.apache.commons.lang.StringUtils
import java.util
import java.io.{IOException, FileNotFoundException}
import org.apache.commons.io.FilenameUtils


/**
 * == UniqueCodeXlsDao ==
 *
 * find unique code xls data and return its bean list
 *
 * @author wada shunsuke
 * @since  2013/12/24
 *
 */
@throws(classOf[NullPointerException])
@throws(classOf[FileNotFoundException])
@throws(classOf[IOException])
class UniqueCodeXlsDao(path: String) extends GeneralXlsDao {

  val workBook:  Workbook          = manager.workbook(path)
  val evaluator: FormulaEvaluator  = workBook.getCreationHelper.createFormulaEvaluator
  val sheet: Sheet                 = workBook.getSheet(prop.generalCodeDefineSheetName)

  /**
   * get unique code's header information
   *
   * @return
   */
  def getHeaderInfo :UniqueCodeXlsBean = {

    val xlsBean: UniqueCodeXlsBean = new UniqueCodeXlsBean
    xlsBean.path     = this.path

    try{
      val row: Row     = sheet.getRow(prop.uniqueCodeDefineRowHeader)

      xlsBean.fileName = FilenameUtils.getName(this.path)
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

    }
    xlsBean
  }

  /**
   * == getCodeDefine ==
   *
   * @return
   */
  def getCodeDefine :util.ArrayList[XlsColumnAttribute] = {
    val xlsBean: UniqueCodeXlsBean = getHeaderInfo

    val resultSets: util.ArrayList[XlsColumnAttribute] = new util.ArrayList[XlsColumnAttribute]()

    var flg = true
    val row: Row      = this.sheet.getRow(prop.uniqueCodeDefineRowColumnsDefine)
    val rowOfDataType = this.sheet.getRow(prop.uniqueCodeDefineRowColumnsDataType)
    val rowOfStopper  = this.sheet.getRow(prop.uniqueCodeDefineRowColumnsDataLength)

    var idx = 1
    var checkVal :String = AppConst.STRING_BLANK
    while(flg) {
      if (rowOfStopper.getCell(idx) == null) {
        flg = false

      } else {
            checkVal =
              utils.convertCellValue2String(row, evaluator, idx)
      }

      if (checkVal.equals(prop.uniqueCodeDefineColumnStopper) || idx > 1000) {
          flg = false
      }

      if (!StringUtils.isBlank(checkVal)) {
        try {
          val physicalColumnName = checkVal
          val dataType           =
              utils.convertCellValue2String(rowOfDataType, evaluator, idx)
          var dataLength: Int = AppConst.INT_ZERO

          //todo
          if (dataType.equals("VARCHAR2") || dataType.equals("VARCHAR") || dataType.equals("CHAR")) {
            val _numericCell = rowOfStopper.getCell(idx)
            _numericCell.getCellType match {
              case Cell.CELL_TYPE_NUMERIC =>
                dataLength = rowOfStopper.getCell(idx).getNumericCellValue.asInstanceOf[Int]

              case Cell.CELL_TYPE_STRING =>
                dataLength = Integer.parseInt(rowOfStopper.getCell(idx).getStringCellValue.replace("バイト",""))
            }
          }

          val attr: XlsColumnAttribute = new XlsColumnAttribute(
            xlsBean.logicalTableName, xlsBean.physicalTableName,
            AppConst.STRING_BLANK, physicalColumnName, dataType, dataLength
          )

          resultSets.add(attr)
        }
      }
      idx += 1
    }

    resultSets
  }

  /**
   * == specifySpan ==
   *
   * specify code define data span
   *
   * @param xlsBean
   */
  def specifySpan(row: Row ,xlsBean: UniqueCodeXlsBean): Unit = {
    var flg = true

    var idx = 1
    var cell: Cell = null
    var checkVal :String = AppConst.STRING_BLANK
    while(flg) {
      cell = row.getCell(idx)
      checkVal = utils.convertCellValue2String(cell, evaluator)

      if (checkVal.equals(prop.uniqueCodeDefineColumnStopper)) {
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
   * @return
   */
  def getDataEntry: List[Map[Int, String]] = {
    var list: List[Map[Int, String]] = List()

    val uniqueCodeInfo: UniqueCodeXlsBean = getHeaderInfo

    try{
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