// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.data

// TODO use in TypedColumns

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


val IntColNullable  = ColumnType.INT_NULLABLE
val StrColNullable  = ColumnType.STR_NULLABLE
val BoolColNullable = ColumnType.BOOL_NULLABLE
val LongColNullable = ColumnType.LONG_NULLABLE
val FloatColNullable = ColumnType.FLOAT_NULLABLE
val BlobColNullable = ColumnType.BLOB_NULLABLE