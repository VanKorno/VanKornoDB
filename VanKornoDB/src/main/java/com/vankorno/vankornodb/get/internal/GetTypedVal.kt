/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.get.internal

import android.database.Cursor

/**
 * Reads a typed value from the cursor at the given column index.
 *
 * @param idx Column index.
 * @return The value at the given index cast to type [T], or `null` if the value is SQL NULL.
 * @throws IllegalStateException if the type [T] is not supported.
 */
@PublishedApi
internal inline fun <reified T> Cursor.getTypedVal(                                  idx: Int
): T? {
    val value: Any? = when (getType(idx)) {
        Cursor.FIELD_TYPE_INTEGER -> getLong(idx)
        Cursor.FIELD_TYPE_FLOAT   -> getDouble(idx)
        Cursor.FIELD_TYPE_STRING  -> getString(idx)
        Cursor.FIELD_TYPE_BLOB    -> getBlob(idx)
        else                      -> null
    }
    
    @Suppress("UNCHECKED_CAST")
    return when (T::class) {
        Boolean::class   -> if (value == null) null else ((value as Long) != 0L) as T
        Int::class       -> (value as? Long)?.toInt() as T?
        Long::class      -> value as T?
        Float::class     -> (value as? Double)?.toFloat() as T?
        Double::class    -> value as? T
        String::class    -> value as T?
        ByteArray::class -> value as T?
        else             -> error("Unsupported type: ${T::class}")
    }
}

