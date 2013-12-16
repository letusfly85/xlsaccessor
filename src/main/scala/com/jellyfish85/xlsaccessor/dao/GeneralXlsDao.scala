package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.util

/**
 *
 *
 *
 */
class GeneralXlsDao[A] {

  def convert(list: List[A]): util.ArrayList[A] = {

    val resultList: util.ArrayList[A] = new util.ArrayList[A]()
    list.foreach {entry: A => resultList.add(entry)}

    resultList
  }

}
