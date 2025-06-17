package com.vankorno.vankornodb.dbManagement.data


enum class ColumnType(                                                           val sql: String
) {
    ID(" INTEGER NOT NULL PRIMARY KEY"),

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


val PrimeId = ColumnType.ID
val IntCol = ColumnType.INT
val StrCol = ColumnType.STR
val BoolCol = ColumnType.BOOL
val LongCol = ColumnType.LONG
val FloatCol = ColumnType.FLOAT
val BlobCol = ColumnType.BLOB


val IntColNullable  = ColumnType.INT_NULLABLE
val StrColNullable  = ColumnType.STR_NULLABLE
val BoolColNullable = ColumnType.BOOL_NULLABLE
val LongColNullable = ColumnType.LONG_NULLABLE
val FloatColNullable = ColumnType.FLOAT_NULLABLE
val BlobColNullable = ColumnType.BLOB_NULLABLE