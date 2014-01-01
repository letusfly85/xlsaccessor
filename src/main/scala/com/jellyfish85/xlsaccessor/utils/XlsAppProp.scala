package com.jellyfish85.xlsaccessor.utils

import org.apache.commons.configuration.{PropertiesConfiguration}
import java.io.InputStream


/**
 * == AppProp ==
 *
 * application property sets
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 */
class XlsAppProp {

  val inputStream: InputStream =
    getClass.getResourceAsStream("/com/jellyfish85/xlsaccessor/query/generate/tool/code.properties")

  val configuration: PropertiesConfiguration =
    new PropertiesConfiguration()

  configuration.load(inputStream, "UTF8")

  //todo search how to configurate UTF8 by using constructor
  //val configuration: Configuration = new PropertiesConfiguration("com/jellyfish85/xlsaccessor/query/generate/tool/code.properties")

  /********************************************************************************
   * general code attribute
   *
   *
   *
   *********************************************************************************/
  val generalCodeBookParentPath                    = configuration.getString("general.code.book.parentPath")

  val generalCodeBookPath                          = configuration.getString("general.code.book.path")

  val generalCodeDefineSheetName                   = configuration.getString("general.code.define.sheet.name")

  val generalCodePhysicalTableName                 = configuration.getString("general.code.table.name.physical")

  val generalCodeLogicalTableName                  = configuration.getString("general.code.table.name.logical")

  val generalCodeDefineConstExistsTrue             = configuration.getString("general.code.define.const.exists.true")

  val generalCodeDefineConstExistsFalse            = configuration.getString("general.code.define.const.exists.false")

  var generalCodeDefineColumnMap: Map[String, Int] = Map()
  val keys: java.util.Iterator[String]             = configuration.getKeys("general.code.define.column")
  while (keys.hasNext) {
    val key: String = keys.next()
    val _key = key.replaceAll("general.code.define.column.","")
    generalCodeDefineColumnMap +=
      (_key -> configuration.getInt(key))
  }

  /********************************************************************************
   * unique code attribute
   *
   *
   *
   *********************************************************************************/

  val uniqueCodeBookParentPath: String             = configuration.getString("unique.code.book.parentPath")

  val uniqueCodeDefineRowHeader: Int               = configuration.getInt("unique.code.define.row.header")

  val uniqueCodeDefineRowDataStartPos: Int         = configuration.getInt("unique.code.define.row.data.start.pos")

  val uniqueCodeDefineRowStopper: String           = configuration.getString("unique.code.define.stopper.row")

  val uniqueCodeDefineColumnStopper: String        = configuration.getString("unique.code.define.stopper.column")

  val uniqueCodeDefineRowColumnsName: Int          = configuration.getInt("unique.code.define.row.columns.name")

  val uniqueCodeDefineRowColumnsDefine: Int        = configuration.getInt("unique.code.define.row.columns.define")

  val uniqueCodeDefineRowColumnsDataType: Int      = configuration.getInt("unique.code.define.row.columns.data.type")

  val uniqueCodeDefineRowColumnsDataLength: Int    = configuration.getInt("unique.code.define.row.columns.data.length")

  var uniqueCodeDefineColumnMap: Map[String, Int]  = Map()
  val uniqueCodeKeys: java.util.Iterator[String]   = configuration.getKeys("unique.code.define.column")
  while (uniqueCodeKeys.hasNext) {
    val key: String = uniqueCodeKeys.next()
    val _key = key.replaceAll("unique.code.define.column.", "")
    uniqueCodeDefineColumnMap +=
      (_key -> configuration.getInt(key))
  }

  /********************************************************************************
   * template record attribute
   *
   *
   *
   *********************************************************************************/

  val templateRecordDefineSheetName: String        =  configuration.getString("template.record.define.sheet.name")

  var templateRecordDefineColumnMap: Map[String, Int] = Map()
  val templateRecordKeys: java.util.Iterator[String]
                                                   = configuration.getKeys("template.record.define.column.name")
  while (templateRecordKeys.hasNext) {
    val key: String = templateRecordKeys.next()
    val _key = key.replaceAll("template.record.define.column.namen.","")
    templateRecordDefineColumnMap +=
      (_key -> configuration.getInt(key))
  }

}
