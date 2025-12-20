// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.dsl

open class TransformColDslInternal {
    private val overrides = mutableMapOf<String, FieldOverride>()
    
    fun modify(                                                   fieldName: String,
                                                                      block: FieldOverride.()->Unit,
    ) {
        val override = FieldOverride()
        block.invoke(override)
        overrides[fieldName] = override
    }
    
    fun getOverride(fieldName: String): FieldOverride? = overrides[fieldName]
    
    
    class FieldOverride {
        var fallback: (() -> Any?)? = null // default value when missing
        var fromInt: ((Int) -> Any)? = null
        var fromLong: ((Long) -> Any)? = null
        var fromFloat: ((Float) -> Any)? = null
        var fromDouble: ((Double) -> Any)? = null
        var fromBoolean: ((Boolean) -> Any)? = null
        var fromString: ((String) -> Any)? = null
        var fromOther: ((Any) -> Any)? = null
        
        // Called last after type conversion
        var finalTransform: ((Any) -> Any)? = null
        
        
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






