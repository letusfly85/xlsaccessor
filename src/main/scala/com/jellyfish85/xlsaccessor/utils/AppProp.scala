package com.jellyfish85.xlsaccessor.utils

import org.apache.commons.configuration.{PropertiesConfiguration, Configuration}


/**
 * == AppProp ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 */
class AppProp {

  val configuration: Configuration =
    new PropertiesConfiguration("com/jellyfish85/xlsaccessor/query/generate/tool/code.properties")

  val generalCodeBookParentPath    = configuration.getString("general.code.book.parentPath")

  val generalCodeDefineSheetName   = configuration.getString("general.code.define.sheet.name")

  val generalCodePhysicalTableName = configuration.getString("general.code.table.name.physical")

  val generalCodeLogicalTableName  = configuration.getString("general.code.table.name.logical")

  val generalCodeDefineConstExistsTrue  = configuration.getString("general.code.define.const.exists.true")

  val generalCodeDefineConstExistsFalse = configuration.getString("general.code.define.const.exists.false")

  var generalCodeDefineColumnMap: Map[String, Int] = Map()
    val keys: java.util.Iterator[String] = configuration.getKeys("general.code.define.column")

    while (keys.hasNext) {
      val key: String = keys.next()
      println(key)
      println(configuration.getInt(key))
      val _key = key.replaceAll("general.code.define.column.","")
      generalCodeDefineColumnMap +=
        (_key -> configuration.getInt(key))
    }
}
