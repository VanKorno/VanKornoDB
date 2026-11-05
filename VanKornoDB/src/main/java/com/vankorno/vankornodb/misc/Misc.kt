/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import android.database.Cursor
import com.vankorno.vankornodb.dbManagement.data.TypedColumn

/**
 * The missing getter for Booleans
 */
fun Cursor.getBoolean(col: Int) = this.getInt(col) == 1


/**
 * Simplifies passing columns where named param isn't needed:
 *     columns(Column1, Column2)
 * instead of:
 *     columns = arrayOf(Column1, Column2)
 */
fun columns(vararg columns: String): Array<out String> = columns

fun columns(vararg columns: TypedColumn<*>): Array<out TypedColumn<*>> = columns

fun <T : TypedColumn<*>> typedColumns(vararg columns: T): Array<out T> = columns






