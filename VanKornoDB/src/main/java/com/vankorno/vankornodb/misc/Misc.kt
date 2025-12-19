// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.misc

import android.database.Cursor
import com.vankorno.vankornodb.api.WhereBuilder
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


/**
 * A convenience fun to avoid tons of ById fun overloads. Used like this:
 * getInt(TableName, Column, whereId(id))
 */
fun whereId(id: Int): WhereBuilder.()->Unit = { ID = id }

/**
 * A convenience fun to avoid tons of ById fun overloads. Used like this:
 * getInt(TableName, Column, whereName(name))
 */
fun whereName(name: String): WhereBuilder.()->Unit = { Name = name }





