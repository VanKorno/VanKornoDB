package com.vankorno.vankornodb.misc
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor

/**
 * The missing getter for Booleans
 */
fun Cursor.getBool(col: Int) = this.getInt(col) == 1


/**
 * Simplifies passing columns where named param isn't needed:
 *     columns(Column1, Column2)
 * instead of:
 *     columns = arrayOf(Column1, Column2)
 */
fun columns(vararg columns: String): Array<out String> = columns



