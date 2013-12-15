package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import org.apache.poi.ss.usermodel.{Row, Sheet, WorkbookFactory, Workbook}
import java.io.{File, FileInputStream}
import org.apache.commons.lang.StringUtils

import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralCodeBean
import com.jellyfish85.xlsaccessor.utils.{XlsAccessUtils, AppProp}


/**
 * == GeneralCodeDao ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 * @todo change column number to configuration values
 *
 */
class GeneralCodeDao {

  val prop: AppProp = new AppProp

  def getGeneralCodeConstInfo(
          codeId: String,
          filePath: String,
          ticketNumber: String
         ): List[GeneralCodeBean] = {

    var list: List[GeneralCodeBean] = List()

    val utils:    XlsAccessUtils = new XlsAccessUtils
    var workBook: Workbook       = null

    try{
      workBook      = WorkbookFactory.create(new FileInputStream(new File(filePath)))
      val evaluator = workBook.getCreationHelper.createFormulaEvaluator

      val sheet: Sheet = workBook.getSheet(prop.generalCodeDefineSheetName)

      var flg:    Boolean = true
      var switch: Boolean = false
      var preCodeId: String = ""
      var idx: Int = 1
      var checkVal: String = ""
      var row: Row = sheet.getRow(idx)

      var constUMCode: String =
        prop.generalCodeDefineConstExistsFalse

      while (flg) {
        row = sheet.getRow(idx)
        if (row == null) {
          flg = false
        }

        idx += 1
        if (flg) {
          if (row.getCell(1) == null) {
            flg = false
          }

          checkVal =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("checkVal"))

          if (StringUtils.isBlank(checkVal)){
            flg = false
          }

          if (!preCodeId.equals(checkVal) && switch.equals(true) && idx > 1) {
            flg    = false
            switch = false
          }

          if (checkVal == codeId) {
            switch = true
          }

          if (switch){
            constUMCode =
              utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("constExists"))

            if (constUMCode == prop.generalCodeDefineConstExistsTrue) {
              val bean: GeneralCodeBean = new GeneralCodeBean

              bean.filePath = filePath
              bean.fileName = (new File(filePath)).getName

              bean.codeId   = checkVal
              bean.subsystemCd =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("subsystemCd"))
              bean.logicalTableName  = prop.generalCodeLogicalTableName
              bean.physicalTableName = prop.generalCodePhysicalTableName

              bean.logicalCodeName = row.
                getCell(prop.generalCodeDefineColumnMap("logicalCodeName")).getStringCellValue()
              bean.physicalCodeName = utils.
                convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("physicalCodeName"))
              bean.codeValue       =
                row.getCell(prop.generalCodeDefineColumnMap("codeValue")).getStringCellValue()
              bean.logicalKeyName  =
                row.getCell(prop.generalCodeDefineColumnMap("logicalKeyName")).getStringCellValue()

              list ::= bean
            }
          }

          preCodeId = checkVal
        }
      }
    } catch {
      case e:Exception =>
        println("[ERROR]" + filePath)
        e.printStackTrace

    } finally {
      workBook = null
    }

    list
  }

}