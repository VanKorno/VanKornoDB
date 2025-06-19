package com.vankorno.vankornodb.dbManagement.migration

class MigrationDSL {
    private val overrides = mutableMapOf<String, FieldOverride>()

    // Register override for a field
    fun modify(fieldName: String, block: FieldOverride.() -> Unit) {
        overrides[fieldName] = FieldOverride().apply(block) as FieldOverride
    }

    fun getOverride(fieldName: String): FieldOverride? = overrides[fieldName]

    class FieldOverride {
        var onMissing: (() -> Any?)? = null // default value when missing
        var onFromInt: ((Int) -> Any)? = null
        var onFromLong: ((Long) -> Any)? = null
        var onFromFloat: ((Float) -> Any)? = null
        var onFromDouble: ((Double) -> Any)? = null
        var onFromBoolean: ((Boolean) -> Any)? = null
        var onFromString: ((String) -> Any)? = null
        var onFromOther: ((Any) -> Any)? = null

        // Called last after type conversion
        var finalTransform: ((Any) -> Any)? = null

        fun apply(value: Any?): Any? {
            if (value == null) return onMissing?.invoke()
            return when (value) {
                is Int -> onFromInt?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                is Long -> onFromLong?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                is Float -> onFromFloat?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                is Double -> onFromDouble?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                is Boolean -> onFromBoolean?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                is String -> onFromString?.invoke(value)
                    ?: onFromOther?.invoke(value)
                    ?: value
                else -> onFromOther?.invoke(value) ?: value
            }.let { finalTransform?.invoke(it) ?: it }
        }
    }
}