/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.dsl

/**
 * DSL for defining column-level value transformations during milestone migration steps.
 *
 * Used for conversions that are not handled automatically (such as String, ByteArray, or complex types)
 * or when custom transformation logic is required between versions.
 */
open class TransformColDslInternal {
    private val overrides = mutableMapOf<String, ColOverride>()
    
    /**
     * Configures value transformation rules for the specified field.
     *
     * @param colName The name of the column or property to transform.
     * @param block Configuration block setting type-specific mapping lambdas or a fallback.
     */
    fun modify(                                                       colName: String,
                                                                        block: ColOverride.()->Unit,
    ) {
        val override = ColOverride()
        block.invoke(override)
        overrides[colName] = override
    }
    
    /**
     * Returns the [ColOverride] registered for the given field, or null if none exists.
     */
    fun getOverride(colName: String): ColOverride? = overrides[colName]
    
    
    /**
     * Defines type-specific transformation lambdas and fallback values for a single field during migration.
     */
    class ColOverride {
        /** Fallback supplier invoked when the source value is null. */
        var fallback: (() -> Any?)? = null // default value when missing
        /** Transform lambda called when the source value is an [Int]. */
        var fromInt: ((Int) -> Any)? = null
        /** Transform lambda called when the source value is a [Long]. */
        var fromLong: ((Long) -> Any)? = null
        /** Transform lambda called when the source value is a [Float]. */
        var fromFloat: ((Float) -> Any)? = null
        /** Transform lambda called when the source value is a [Double]. */
        var fromDouble: ((Double) -> Any)? = null
        /** Transform lambda called when the source value is a [Boolean]. */
        var fromBoolean: ((Boolean) -> Any)? = null
        /** Transform lambda called when the source value is a [String]. */
        var fromString: ((String) -> Any)? = null
        /** Transform lambda called when the source value does not match any specific type handler. */
        var fromOther: ((Any) -> Any)? = null
        
        /** Optional transformation applied last after type conversion. */
        var finalTransform: ((Any) -> Any)? = null
        
        /**
         * Applies the configured transformation rules to [value], resolving fallbacks and type lambdas.
         */
        
        
        fun apply(value: Any?): Any? {
            if (value == null) return fallback?.invoke()
            
            return when (value) {
                is Int -> fromInt?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                is Long -> fromLong?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                is Float -> fromFloat?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                is Double -> fromDouble?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                is Boolean -> fromBoolean?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                is String -> fromString?.invoke(value) ?: fromOther?.invoke(value) ?: value
                
                else -> fromOther?.invoke(value) ?: value
                
            }.let { finalTransform?.invoke(it) ?: it }
        }
    }
}






