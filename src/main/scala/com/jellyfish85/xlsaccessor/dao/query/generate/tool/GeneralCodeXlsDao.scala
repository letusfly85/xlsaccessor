package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.math.BigDecimal
import org.apache.poi.ss.usermodel.{Row, Sheet}
import org.apache.commons.lang.StringUtils

import com.jellyfish85.xlsaccessor.bean.query.generate.tool.GeneralCodeXlsBean
import com.jellyfish85.xlsaccessor.constant.AppConst
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import org.apache.commons.lang.math.NumberUtils


/**
 * == GeneralCodeDao ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 */
class GeneralCodeXlsDao(path: String) extends GeneralXlsDao[GeneralCodeXlsBean](path: String) {

  val sheet: Sheet  = workBook.getSheet(prop.generalCodeDefineSheetName)

  /**
   * == findAll ==
   *
   * find all code from excel file excluding ignored codes
   *
   *
   * @author wada shunsuke
   * @since  2013/12/16
   * @param ticketNumber
   * @param svnRequestBean
   * @return
   */
  def findAll(
               ticketNumber:   BigDecimal,
               svnRequestBean: SVNRequestBean
             ): List[GeneralCodeXlsBean] = {

    var resultSets: List[GeneralCodeXlsBean] = List()

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
          if (row.getCell(prop.generalCodeDefineColumnMap("checkVal")) == null) {
            flg = false
          }

          checkVal = row.getCell(prop.generalCodeDefineColumnMap("checkVal")).getStringCellValue()
          if (StringUtils.isBlank(checkVal)){
            flg = false
          }

          val bean: GeneralCodeXlsBean = new GeneralCodeXlsBean

          bean.ticketNumber = ticketNumber
          bean.codeId       = checkVal

          bean.path         = svnRequestBean.path
          bean.fileName     = svnRequestBean.fileName
          bean.revision     = svnRequestBean.revision
          bean.author       = svnRequestBean.author
          bean.commitYmd    = svnRequestBean.commitYmd
          bean.commitHms    = svnRequestBean.commitHms

          bean.logicalTableName  = prop.generalCodeLogicalTableName
          bean.physicalTableName = prop.generalCodePhysicalTableName

          bean.subsystemCd       =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("subsystemCd"))
          bean.logicalCodeName   =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("logicalCodeName"))
          bean.physicalCodeName  =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("physicalCodeName"))
          bean.codeValue         =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("codeValue"))
          bean.logicalKeyName    =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("logicalKeyName"))
          bean.shortName         =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("shortName"))

          bean.delFlg            =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("delFlg"))
          if (StringUtils.isBlank(bean.delFlg)) {
            bean.delFlg = AppConst.STRING_ZERO
          }

          bean.ignoreFlg         =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("ignoreFlg"))
          if (StringUtils.isBlank(bean.ignoreFlg)){
            bean.ignoreFlg = AppConst.STRING_ZERO
          } else {
            bean.ignoreFlg = AppConst.STRING_ONE
          }

          val _displayOrder      =
              utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("displayOrder"))
          if (NumberUtils.isNumber(_displayOrder)) {
            bean.displayOrder = Integer.parseInt(_displayOrder)
          }

          if (bean.ignoreFlg.equals(AppConst.STRING_ZERO)){
            resultSets ::= bean
          }
        }
      }

    } catch {
      case e:Exception =>
        println("[ERROR]" + path)
        e.printStackTrace
    }

    resultSets
  }


  /**
   * == findConst ==
   *
   * find code having statistic field for Java
   *
   * @author wada shunsuke
   * @since  2013/12/16
   * @param  codeId
   * @param  path
   * @param  ticketNumber
   * @param  svnRequestBean
   * @return
   */
  def findConst(
          codeId:         String,
          path:           String,
          ticketNumber:   BigDecimal,
          svnRequestBean: SVNRequestBean
         ): List[GeneralCodeXlsBean] = {

    var list: List[GeneralCodeXlsBean] = List()

    try{
      // init fields
      var flg:       Boolean = true
      var switch:    Boolean = false
      var preCodeId: String  = AppConst.STRING_BLANK
      var checkVal:  String  = AppConst.STRING_BLANK
      var idx:       Int     = AppConst.INT_ONE
      var row:       Row     = sheet.getRow(idx)

      // give const init value to false
      var constExists: String =
        prop.generalCodeDefineConstExistsFalse

      // loop until row becomes blank or gets all data identified by argument codeId
      while (flg) {
        row = sheet.getRow(idx)
        if (row == null) {
          flg = false
        }

        idx += 1
        if (flg) {
          if (row.getCell(prop.generalCodeDefineColumnMap("checkVal")) == null) {
            flg = false
          }

          checkVal =
            utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("checkVal"))

          if (StringUtils.isBlank(checkVal)){
            flg = false
          }

          if (!preCodeId.equals(checkVal) && switch.equals(true) && idx > AppConst.INT_ONE) {
            flg    = false
            switch = false
          }

          if (checkVal.equals(codeId)) {
            switch = true
          }

          if (switch){
            constExists =
              utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("constExists"))

            if (constExists.equals(prop.generalCodeDefineConstExistsTrue)) {
              // generate new bean of GeneralCodeXlsBean
              val bean: GeneralCodeXlsBean = new GeneralCodeXlsBean

              bean.ticketNumber = ticketNumber
              bean.codeId       = checkVal

              bean.path         = svnRequestBean.path
              bean.fileName     = svnRequestBean.fileName
              bean.revision     = svnRequestBean.revision
              bean.author       = svnRequestBean.author
              bean.commitYmd    = svnRequestBean.commitYmd
              bean.commitHms    = svnRequestBean.commitHms

              bean.logicalTableName  = prop.generalCodeLogicalTableName
              bean.physicalTableName = prop.generalCodePhysicalTableName

              bean.subsystemCd       =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("subsystemCd"))
              bean.logicalCodeName   =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("logicalCodeName"))
              bean.physicalCodeName  =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("physicalCodeName"))
              bean.codeValue         =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("codeValue"))
              bean.logicalKeyName    =
                utils.convertCellValue2String(row, evaluator, prop.generalCodeDefineColumnMap("logicalKeyName"))

              list ::= bean
            }
          }

          preCodeId = checkVal
        }
      }
    } catch {
      case e:Exception =>
        println("[ERROR]" + path)
        e.printStackTrace
    }

    list
  }



}