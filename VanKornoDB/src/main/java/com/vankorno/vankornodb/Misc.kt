package com.vankorno.vankornodb
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import android.database.Cursor

/**
 * The missing getter for Booleans
 */
fun Cursor.getBool(col: Int) = this.getInt(col) == 1


internal infix fun String.c(str2: String = "") = this + ", " + str2 // for pre-DSL syntax


