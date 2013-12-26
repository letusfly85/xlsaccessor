package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.util
import com.jellyfish85.xlsaccessor.utils.{XlsAccessUtils, XlsAppProp}
import com.jellyfish85.xlsaccessor.manager.XlsManager

/**
 *
 *
 *
 */
class GeneralXlsDao[A] {

  val prop:     XlsAppProp        = new XlsAppProp

  val manager:  XlsManager     = new XlsManager

  val utils:    XlsAccessUtils = new XlsAccessUtils

  def convert(list: List[A]): util.ArrayList[A] = {

    val resultList: util.ArrayList[A] = new util.ArrayList[A]()
    list.foreach {entry: A => resultList.add(entry)}

    resultList
  }

}
