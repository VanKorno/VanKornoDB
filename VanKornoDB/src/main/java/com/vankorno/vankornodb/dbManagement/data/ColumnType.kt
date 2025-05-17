package com.vankorno.vankornodb.dbManagement.data


enum class ColumnType(                                                           val sql: String
) {
    AUTO_ID(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT"),
    AUTO_ID_NULLABLE(" INTEGER PRIMARY KEY AUTOINCREMENT"),

    INT(" INT NOT NULL"),
    INT_NULLABLE(" INT"),

    STR(" TEXT NOT NULL"),
    STR_NULLABLE(" TEXT"),

    BOOL(" BOOL NOT NULL"),
    BOOL_NULLABLE(" BOOL"),

    LONG(" BIGINT NOT NULL"),
    LONG_NULLABLE(" BIGINT"),

    FLOAT(" REAL NOT NULL"),
    FLOAT_NULLABLE(" REAL"),

    BLOB(" BLOB NOT NULL"),
    BLOB_NULLABLE(" BLOB")
}


val AutoId = ColumnType.AUTO_ID
val IntCol = ColumnType.INT
val StrCol = ColumnType.STR
val BoolCol = ColumnType.BOOL
val LongCol = ColumnType.LONG
val FloatCol = ColumnType.FLOAT
val BlobCol = ColumnType.BLOB


val AutoIdNullable  = ColumnType.AUTO_ID_NULLABLE
val IntColNullable  = ColumnType.INT_NULLABLE
val StrColNullable  = ColumnType.STR_NULLABLE
val BoolColNullable = ColumnType.BOOL_NULLABLE
val LongColNullable = ColumnType.LONG_NULLABLE
val FloatColNullable = ColumnType.FLOAT_NULLABLE
val BlobColNullable = ColumnType.BLOB_NULLABLE