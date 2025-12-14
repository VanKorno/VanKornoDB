package com.vankorno.vankornodb.dbManagement.data

@Suppress("unused")
sealed class TypedColumn<T>(
                                   val name: String,
                             val defaultVal: T,
                               val nullable: Boolean = false, // Not used yet
)


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


fun iListCol(name: String, size: Int, defaultVal: Int = 0) = List(size) { iCol(name + (it + 1), defaultVal) }
fun sListCol(name: String, size: Int, defaultVal: String = "") = List(size) { sCol(name + (it + 1), defaultVal) }
fun bListCol(name: String, size: Int, defaultVal: Boolean = false) = List(size) { bCol(name + (it + 1), defaultVal) }
fun lListCol(name: String, size: Int, defaultVal: Long = 0L) = List(size) { lCol(name + (it + 1), defaultVal) }
fun fListCol(name: String, size: Int, defaultVal: Float = 0F) = List(size) { fCol(name + (it + 1), defaultVal) }
fun pListCol(name: String, size: Int) = List(size) { pCol(name + (it + 1)) }





class DescendingIntCol(name: String, defaultVal: Int = 0) : TypedColumn<Int>(name, defaultVal)
class DescendingStrCol(name: String, defaultVal: String = "") : TypedColumn<String>(name, defaultVal)
class DescendingBoolCol(name: String, defaultVal: Boolean = false) : TypedColumn<Boolean>(name, defaultVal)
class DescendingLongCol(name: String, defaultVal: Long = 0L) : TypedColumn<Long>(name, defaultVal)
class DescendingFloatCol(name: String, defaultVal: Float = 0F) : TypedColumn<Float>(name, defaultVal)

