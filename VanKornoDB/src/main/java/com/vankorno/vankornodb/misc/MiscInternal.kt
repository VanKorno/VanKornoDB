package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.dbManagement.data.TypedColumn


internal fun getColNames(columns: Array<out TypedColumn<*>>): Array<String> = columns.map { it.name }.toTypedArray()
