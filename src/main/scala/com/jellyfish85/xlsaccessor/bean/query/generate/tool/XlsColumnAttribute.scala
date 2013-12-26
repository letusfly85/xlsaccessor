package com.jellyfish85.xlsaccessor.bean.query.generate.tool

/**
 * == ColumnAttribute ==
 *
 *
 */
class XlsColumnAttribute(
        _logicalTableName:   String,
        _physicalTableName:  String,
        _logicalColumnName:  String,
        _physicalColumnName: String,
        _dataType:           String,
        _dataLength:         Int
        ) {

  val logicalTableName:   String = _logicalTableName
  val physicalTableName:  String = _physicalTableName
  val	logicalColumnName:  String = _logicalColumnName
  val	physicalColumnName: String = _physicalColumnName
  val	dataType:           String = _dataType
  val	dataLength:         Int    = _dataLength

}