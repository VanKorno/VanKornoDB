/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import android.database.Cursor
import com.vankorno.vankornodb.core.dsl.ExtraDsl

/**
 * The missing getter for Booleans
 */
fun Cursor.getBoolean(col: Int) = this.getInt(col) == 1


/**
 * A convenience fun to pass limit for the db query
 */
fun limit(quant: Int): ExtraDsl<*>.()->Unit = { limit = quant }


