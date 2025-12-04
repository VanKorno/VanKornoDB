package com.vankorno.vankornodb.core.data

sealed class TypedColumn<T>(val name: String)

class IntCol(name: String) : TypedColumn<Int>(name)
class StrCol(name: String) : TypedColumn<String>(name)
class BoolCol(name: String) : TypedColumn<Boolean>(name)
class LongCol(name: String) : TypedColumn<Long>(name)
class FloatCol(name: String) : TypedColumn<Float>(name)
class BlobCol(name: String) : TypedColumn<ByteArray>(name)

fun intCol(name: String) = IntCol(name)
fun strCol(name: String) = StrCol(name)
fun boolCol(name: String) = BoolCol(name)
fun longCol(name: String) = LongCol(name)
fun floatCol(name: String) = FloatCol(name)
fun blobCol(name: String) = BlobCol(name)


class IntListCol(name: String) : TypedColumn<List<Int>>(name)
class StrListCol(name: String) : TypedColumn<List<String>>(name)
class BoolListCol(name: String) : TypedColumn<List<Boolean>>(name)
class LongListCol(name: String) : TypedColumn<List<Long>>(name)
class FloatListCol(name: String) : TypedColumn<List<Float>>(name)
class BlobListCol(name: String) : TypedColumn<List<ByteArray>>(name)

fun intListCol(name: String) = IntListCol(name)
fun strListCol(name: String) = StrListCol(name)
fun boolListCol(name: String) = BoolListCol(name)
fun longListCol(name: String) = LongListCol(name)
fun floatListCol(name: String) = FloatListCol(name)
fun blobListCol(name: String) = BlobListCol(name)
