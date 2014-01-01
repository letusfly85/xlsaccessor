package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.util
import com.jellyfish85.xlsaccessor.utils.{XlsAccessUtils, XlsAppProp}
import com.jellyfish85.xlsaccessor.manager.XlsManager
import org.apache.poi.ss.usermodel.{FormulaEvaluator, Workbook}
import com.jellyfish85.xlsaccessor.constant.AppConst

/**
 *
 *
 *
 */
class GeneralXlsDao[A](path: String) {

  val manager:  XlsManager         = new XlsManager

  val workBook:  Workbook          = manager.workbook(path)
  val evaluator: FormulaEvaluator  = workBook.getCreationHelper.createFormulaEvaluator

  val prop:     XlsAppProp         = new XlsAppProp
  val utils:    XlsAccessUtils     = new XlsAccessUtils

  def convert(list: List[A]): util.ArrayList[A] = {
    val resultList: util.ArrayList[A] = new util.ArrayList[A]()
    list.foreach {entry: A => resultList.add(entry)}

    resultList
  }

  def closeStream4Path(): Int = {
    this.manager.closeStream

    AppConst.INT_ZERO
  }

}
