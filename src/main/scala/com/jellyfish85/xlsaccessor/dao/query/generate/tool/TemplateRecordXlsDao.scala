package com.jellyfish85.xlsaccessor.dao.query.generate.tool

import java.io.{IOException, FileNotFoundException}
import com.jellyfish85.xlsaccessor.bean.query.generate.tool.TemplateRecordXlsBean
import org.apache.poi.ss.usermodel.Sheet

/**
 * == TemplateRecordXlsDao ==
 *
 * find template recode beans from excel file
 *
 * @author wada shunsuke
 * @since  2014/01/01
 *
 */
@throws(classOf[NullPointerException])
@throws(classOf[FileNotFoundException])
@throws(classOf[IOException])
class TemplateRecordXlsDao(path: String) extends GeneralXlsDao[TemplateRecordXlsBean](path: String) {

  val sheet: Sheet                 = workBook.getSheet(prop.templateRecordDefineSheetName)

}
