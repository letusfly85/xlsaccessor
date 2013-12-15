package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import org.apache.poi.ss.usermodel.{Row, Sheet, WorkbookFactory, Workbook}
import java.io.{File, FileInputStream}

import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralCodeBean
import com.jellyfish85.xlsaccessor.utils.{XlsAccessUtils, AppProp}

/**
 * == GeneralCodeDao ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 * @todo change column number to configurational values
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

      var constUMCode: String = "無"
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

          checkVal = utils.convertCellValue2String(row, evaluator, 1)

          if (checkVal == "" || checkVal == null){
            flg = false
          }

          if (preCodeId != checkVal && switch == true && idx > 1) {
            flg    = false
            switch = false
          }

          if (checkVal == codeId) {
            switch = true
          }

          if (switch){
            constUMCode = utils.convertCellValue2String(row, evaluator, 8)

            if (constUMCode == "有") {
              val entity: GeneralCodeBean = new GeneralCodeBean

              entity.filePath = filePath
              entity.fileName = (new File(filePath)).getName

              entity.codeId   = checkVal
              entity.subsystemCd = utils.convertCellValue2String(row, evaluator, 0)
              entity.logicalTableName  = prop.generalCodeLogicalTableName
              entity.physicalTableName = prop.generalCodePhysicalTableName

              entity.logicalCodeName = row.getCell(2).getStringCellValue()
              entity.physicalCodeName = utils.convertCellValue2String(row, evaluator, 3)
              entity.codeValue       = row.getCell(5).getStringCellValue()
              entity.logicalKeyName  = row.getCell(6).getStringCellValue()

              list ::= entity
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