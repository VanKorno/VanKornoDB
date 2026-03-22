/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.data

enum class ColumnTypeSql(                                                           val sql: String
) {
    ID(" INTEGER NOT NULL PRIMARY KEY"),

    INT(" INT NOT NULL"),
    INT_NULLABLE(" INT"),

    STR(" TEXT NOT NULL"),
    STR_NULLABLE(" TEXT"),

    BOOL(" BOOL NOT NULL"),
    BOOL_NULLABLE(" BOOL"),

    LONG(" INTEGER NOT NULL"),
    LONG_NULLABLE(" INTEGER"),

    FLOAT(" REAL NOT NULL"),
    FLOAT_NULLABLE(" REAL"),

    BLOB(" BLOB NOT NULL"),
    BLOB_NULLABLE(" BLOB")
}


val IntColNullable  = ColumnTypeSql.INT_NULLABLE
val StrColNullable  = ColumnTypeSql.STR_NULLABLE
val BoolColNullable = ColumnTypeSql.BOOL_NULLABLE
val LongColNullable = ColumnTypeSql.LONG_NULLABLE
val FloatColNullable = ColumnTypeSql.FLOAT_NULLABLE
val BlobColNullable = ColumnTypeSql.BLOB_NULLABLE