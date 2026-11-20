/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.data.DbConstants._Position

class TableInfo<T : DbEntity>(val name: String, val schema: SchemaBundle<T>)


infix fun <T : DbEntity> String.using(schema: SchemaBundle<T>) = TableInfo(this, schema)


internal fun TableInfo<*>.requirePositionColumn() {
    schema.columns?.let { columns ->
        require(columns.any { it.name == _Position && it is LongCol }) {
            "Table '$name' must declare a Long column named '$_Position'"
        }
    }
}
