// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.get.raw

import android.database.Cursor
import com.vankorno.vankornodb.misc.getBoolean

inline fun <reified V> Cursor.getTypedValAt(                                         idx: Int
): V = when (V::class) {
    Boolean::class -> getBoolean(idx)
    Int::class -> getInt(idx)
    Long::class -> getLong(idx)
    Float::class -> getFloat(idx)
    Double::class -> getDouble(idx)
    String::class -> getString(idx)
    ByteArray::class -> getBlob(idx)
    else -> error("Unsupported column type: ${V::class}")
} as V