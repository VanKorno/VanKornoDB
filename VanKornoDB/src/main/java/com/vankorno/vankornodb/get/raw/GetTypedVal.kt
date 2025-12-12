package com.vankorno.vankornodb.get.raw

import android.database.Cursor
import com.vankorno.vankornodb.misc.getBool

inline fun <reified V> Cursor.getTypedValAt(                                         idx: Int
): V = when (V::class) {
    Boolean::class -> getBool(idx)
    Int::class -> getInt(idx)
    Long::class -> getLong(idx)
    Float::class -> getFloat(idx)
    Double::class -> getDouble(idx)
    String::class -> getString(idx)
    ByteArray::class -> getBlob(idx)
    else -> error("Unsupported column type: ${V::class}")
} as V