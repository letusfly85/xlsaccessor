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


}
