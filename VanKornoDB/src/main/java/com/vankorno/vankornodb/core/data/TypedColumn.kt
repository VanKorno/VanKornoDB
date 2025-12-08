package com.vankorno.vankornodb.core.data

sealed class TypedColumn<T>(
                                   val name: String,
                             val defaultVal: T,
                                   val size: Int = 1, // default 1 for scalar columns, >1 for lists
                               val nullable: Boolean = false, // Not used yet
)


interface EntityColumns {
    val columns: List<TypedColumn<*>>
}


class IntCol(name: String, defaultVal: Int = 0) : TypedColumn<Int>(name, defaultVal)
class StrCol(name: String, defaultVal: String = "") : TypedColumn<String>(name, defaultVal)
class BoolCol(name: String, defaultVal: Boolean = false) : TypedColumn<Boolean>(name, defaultVal)
class LongCol(name: String, defaultVal: Long = 0L) : TypedColumn<Long>(name, defaultVal)
class FloatCol(name: String, defaultVal: Float = 0F) : TypedColumn<Float>(name, defaultVal)
class BlobCol(name: String, defaultVal: ByteArray = ByteArray(0)) : TypedColumn<ByteArray>(name, defaultVal)


fun iCol(name: String, defaultVal: Int = 0) = IntCol(name, defaultVal)
fun sCol(name: String, defaultVal: String = "") = StrCol(name, defaultVal)
fun bCol(name: String, defaultVal: Boolean = false) = BoolCol(name, defaultVal)
fun lCol(name: String, defaultVal: Long = 0L) = LongCol(name, defaultVal)
fun fCol(name: String, defaultVal: Float = 0F) = FloatCol(name, defaultVal)
fun pCol(name: String, defaultVal: ByteArray = ByteArray(0)) = BlobCol(name, defaultVal)


class IntListCol(name: String, size: Int, defaultVal: Int = 0) : TypedColumn<List<Int>>(name, List(size) { defaultVal }, size)
class StrListCol(name: String, size: Int, defaultVal: String = "") : TypedColumn<List<String>>(name, List(size) { defaultVal }, size)
class BoolListCol(name: String, size: Int, defaultVal: Boolean = false) : TypedColumn<List<Boolean>>(name, List(size) { defaultVal }, size)
class LongListCol(name: String, size: Int, defaultVal: Long = 0L) : TypedColumn<List<Long>>(name, List(size) { defaultVal }, size)
class FloatListCol(name: String, size: Int, defaultVal: Float = 0F) : TypedColumn<List<Float>>(name, List(size) { defaultVal }, size)
class BlobListCol(name: String, size: Int, defaultVal: ByteArray = ByteArray(0)) : TypedColumn<List<ByteArray>>(name, List(size) { defaultVal }, size)


fun iListCol(name: String, size: Int, defaultVal: Int = 0) = IntListCol(name, size, defaultVal)
fun sListCol(name: String, size: Int, defaultVal: String = "") = StrListCol(name, size, defaultVal)
fun bListCol(name: String, size: Int, defaultVal: Boolean = false) = BoolListCol(name, size, defaultVal)
fun lListCol(name: String, size: Int, defaultVal: Long = 0L) = LongListCol(name, size, defaultVal)
fun fListCol(name: String, size: Int, defaultVal: Float = 0F) = FloatListCol(name, size, defaultVal)
fun pListCol(name: String, size: Int, defaultVal: ByteArray = ByteArray(0)) = BlobListCol(name, size, defaultVal)





