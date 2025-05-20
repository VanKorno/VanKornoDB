package com.vankorno.vankornodb.dbManagement.migration
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/

import android.database.sqlite.SQLiteDatabase
import com.vankorno.vankornodb.getCursor
import com.vankorno.vankornodb.getSet.mapToEntity
import kotlin.collections.get
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


/**
 * Reads data from the given [tableName], maps each row to an instance of [OLD],
 * then converts it to [NEW] by copying matching properties with identical names and types.
 *
 * This is intended for use in database migrations. [OLD] should represent the *renamed* version
 * of the original entity — it must have all fields that were renamed in the database already
 * updated to their new names. Essentially, [OLD] is a transitional version: structurally
 * identical to the old data, but with names matching the new schema.
 * (if used with version grouping (migration tiers), then only the most recent [OLD] entities
 * will have to be updated that way...)
 *
 * @param tableName The name of the table to read from.
 * @return A list of [NEW] instances reconstructed from the old table data.
 */

inline fun <reified OLD : Any, reified NEW : Any> SQLiteDatabase.readAsMigrated(  tableName: String
): List<NEW> {
    val oldItems = mutableListOf<NEW>()

    this.getCursor(tableName).use { cursor ->
        if (cursor.moveToFirst()) {
            do {
                val old = cursor.mapToEntity(OLD::class)
                val new = old.mapIdenticals<OLD, NEW>()
                oldItems += new
            } while (cursor.moveToNext())
        }
    }
    return oldItems
}



/**
 * Maps the current instance of [FROM] to a new instance of [TO] by copying all properties
 * that have identical names and types in both classes.
 *
 * Optionally, allows customizing or overriding the result via the [overrides] lambda.
 *
 * @param overrides A lambda to apply additional transformation after initial mapping.
 * @return A new [TO] instance with matched property values from [FROM].
 */

inline fun <reified FROM : Any, reified TO : Any> FROM.mapIdenticals(
                                                                  overrides: TO.(FROM)->TO = {this}
): TO {
    val fromProps = FROM::class.memberProperties.associateBy { it.name }
    val constructor = TO::class.primaryConstructor!!
    
    val args = constructor.parameters.associateWith { param ->
        val fromProp = fromProps[param.name]
        if (fromProp != null && param.type.classifier == fromProp.returnType.classifier) {
            fromProp.get(this)
        } else null
    }.filterValues { it != null }
    
    val instance = constructor.callBy(args)
    return overrides(instance, this@mapIdenticals)
}





// Might be needed later

/*inline fun <reified FROM : Any, reified TO : Any> FROM.mapWithRenames(
    renames: Map<String, String>,
    overrides: TO.(FROM) -> TO = { this }
): TO {
    val fromProps = FROM::class.memberProperties.associateBy { it.name }
    val constructor = TO::class.primaryConstructor!!

    val args = constructor.parameters.associateWith { param ->
        val fromPropName = renames.entries.find { it.value == param.name }?.key ?: param.name
        val fromProp = fromProps[fromPropName]
        if (fromProp != null && param.type.classifier == fromProp.returnType.classifier) {
            fromProp.get(this)
        } else null
    }.filterValues { it != null }

    val instance = constructor.callBy(args)
    return overrides(instance, this@mapWithRenames)
}*/




















