package com.jellyfish85.xlsaccessor.manager

import org.apache.poi.ss.usermodel.{WorkbookFactory, Workbook}
import java.io.{IOException, FileNotFoundException, File, FileInputStream}

/**
 * == XlsManager ==
 *
 *
 * @author wada shunsuke
 * @since  2013/12/15
 *
 */
class XlsManager {

  /**
   * == workbook ==
   *
   *
   * @author wada shunsuke
   * @since  2013/12/15
   * @param filePath
   * @return
   */
  @throws(classOf[FileNotFoundException])
  @throws(classOf[IOException])
  def workbook(filePath: String): Workbook = {
    WorkbookFactory.create(new FileInputStream(new File(filePath)))
  }

}