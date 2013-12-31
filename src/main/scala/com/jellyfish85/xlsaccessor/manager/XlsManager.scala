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

  var inputStream: FileInputStream = null

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
    this.inputStream = new FileInputStream(new File(filePath))
    WorkbookFactory.create(this.inputStream)
  }

  def closeStream {
    this.inputStream.close()
  }


}