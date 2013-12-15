package com.jellyfish85.xlsaccessor.utils

import org.apache.poi.ss.usermodel.{CellValue, Cell, FormulaEvaluator, Row}

/**
 * == XlsAccessUtils ==
 *
 *
 * @author wada shunsuke
 * @since
 *        2013/12/15
 *
 */
class XlsAccessUtils {
  /**
   * == convertCellValue2String ==
   *
   * @param row
   * @param evaluator
   * @param idx
   * @return
   */
  def convertCellValue2String(row: Row, evaluator: FormulaEvaluator, idx: Int): String = {
    var res: String = ""

    if (row.getCell(idx) == null) {
      return res
    }

    row.getCell(idx).getCellType match {
      case Cell.CELL_TYPE_STRING =>
        res = row.getCell(idx).getStringCellValue()

      case Cell.CELL_TYPE_NUMERIC =>
        res = String.valueOf(row.getCell(idx).getNumericCellValue().asInstanceOf[Long])
        res = res.replace(".0", "")

      case Cell.CELL_TYPE_FORMULA =>
        val cellValue: CellValue = evaluator.evaluate(row.getCell(idx))

        cellValue.getCellType() match {
          case Cell.CELL_TYPE_STRING =>
            res = cellValue.getStringValue()

          case Cell.CELL_TYPE_NUMERIC =>
            res = String.valueOf(cellValue.getNumberValue())
            res = res.replace(".0", "")

        }

      case Cell.CELL_TYPE_BLANK =>
        res = ""
    }

    res
  }

  /**
   * == convertCellValue2String ==
   *
   * @param cell
   * @param evaluator
   * @return
   */
  def convertCellValue2String(cell: Cell, evaluator: FormulaEvaluator): String = {
    var res: String = ""

    if (cell == null) {
      return res
    }

    cell.getCellType match {
      case Cell.CELL_TYPE_STRING =>
        res = cell.getStringCellValue()

      case Cell.CELL_TYPE_NUMERIC =>
        res = String.valueOf(cell.getNumericCellValue())
        res = res.replace(".0", "")

      case Cell.CELL_TYPE_FORMULA =>
        val cellValue: CellValue = evaluator.evaluate(cell)

        cellValue.getCellType() match {
          case Cell.CELL_TYPE_STRING =>
            res = cellValue.getStringValue()

          case Cell.CELL_TYPE_NUMERIC =>
            res = String.valueOf(cellValue.getNumberValue())
            res = res.replace(".0", "")

        }

      case Cell.CELL_TYPE_BLANK =>
        res = ""
    }

    res
  }
}