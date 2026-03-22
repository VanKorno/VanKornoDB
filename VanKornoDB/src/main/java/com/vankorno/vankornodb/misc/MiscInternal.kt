/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.dbManagement.data.TypedColumn


internal fun getColNames(columns: Array<out TypedColumn<*>>): Array<String> = columns.map { it.name }.toTypedArray()
