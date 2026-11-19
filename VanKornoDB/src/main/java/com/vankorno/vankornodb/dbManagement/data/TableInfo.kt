/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.DbEntity

class TableInfo<T : DbEntity>(val name: String, val schema: SchemaBundle<T>)

infix fun <T : DbEntity> String.using(schema: SchemaBundle<T>) = TableInfo(this, schema)
