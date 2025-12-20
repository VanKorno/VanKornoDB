// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.MigrationDsl
import com.vankorno.vankornodb.api.TransformCol
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord
import kotlin.reflect.KClass


class ModifyRow(val fieldName: String, val block: TransformColInternal.FieldOverride.()->Unit)


internal fun <T: DbEntity> defineMigrationsInternal(           latestVersion: Int,
                                                                 latestClass: KClass<T>,
                                                                       block: MigrationDsl.()->Unit,
): MigrationBundle {
    val defBuilder = MigrationDsl()
    defBuilder.block()
    
    val latestVersionKlassAbsent = !defBuilder.versionedClasses.containsKey(latestVersion)
    if (latestVersionKlassAbsent)
        defBuilder.versionedClasses[latestVersion] = latestClass
    
    return MigrationBundle(
        versionedClasses = defBuilder.versionedClasses,
        renameHistory = defBuilder.renameHistory,
        milestones = defBuilder.milestones
    )
}


abstract class MigrationDslInternal {
    val versionedClasses = mutableMapOf<Int, KClass<out DbEntity>>()
    val renameHistory = mutableMapOf<String, MutableList<RenameRecord>>()
    val milestones = mutableListOf<Pair<Int, MilestoneLambdas>>()
    
    fun <T : DbEntity> version(                               version: Int,
                                                                clazz: KClass<T>,
                                                                block: VersionBuilder.()->Unit = {},
    ) {
        val verBuilder = VersionBuilder(version)
        verBuilder.block()
        
        // Class
        versionedClasses[version] = clazz
        
        // Renames
        for (record in verBuilder.pendingRenames) {
            val keyName = record.key
            val colRenames = record.value
            
            if (renameHistory.containsKey(keyName)  &&  !renameHistory[keyName].isNullOrEmpty()) {
                for (rename in colRenames) {
                    renameHistory[keyName]!!.add(rename)
                }
            } else {
                renameHistory[keyName] = colRenames
            }
        }
        
        // Milestone
        verBuilder.milestone?.let {
            milestones.add(version to it)
        }
    }
    
    
    
    class VersionBuilder(                                                    val version: Int
    ) {
        var milestone: MilestoneLambdas? = null
        val pendingRenames = mutableMapOf<String, MutableList<RenameRecord>>()
        
        fun rename(                                                   block: RenameBuilder.()->Unit
        ) {
            val renameBuilder = RenameBuilder(version).apply(block)
            
            for (record in renameBuilder.records) {
                val keyName = record.key
                val colRenames = record.value
                
                if (pendingRenames.containsKey(keyName)  &&  !pendingRenames[keyName].isNullOrEmpty()) {
                    for (rename in colRenames) {
                        pendingRenames[keyName]!!.add(rename)
                    }
                } else {
                    pendingRenames[keyName] = colRenames
                }
            }
        }
        
        
        infix fun String.modify(block: TransformColInternal.FieldOverride.()->Unit): ModifyRow = ModifyRow(this, block)
        
        infix fun TypedColumn<*>.modify(block: TransformColInternal.FieldOverride.()->Unit): ModifyRow = ModifyRow(this.name, block)
        
        
        fun milestone(
                     vararg modifications: ModifyRow,
                          processFinalObj: ((oldObj: DbEntity, newObj: DbEntity)->DbEntity)? = null,
        ) {
            val overrideBlock: TransformCol.() -> Unit = {
                modifications.forEach { modify(it.fieldName, it.block) }
            }
            milestone = MilestoneLambdas(
                transformColVal = overrideBlock,
                processFinalObj = processFinalObj
            )
        }
        
        
        
        class RenameBuilder(                                                 val version: Int
        ) {
            val records = mutableMapOf<String, MutableList<RenameRecord>>()
            
            
            infix fun TypedColumn<*>.from(old: String) = PartialRename(this.name, old)
            
            infix fun String.from(old: String) = PartialRename(this, old)
            
            
            inner class PartialRename(                                       val keyName: String,
                                                                          val renameFrom: String,
            ) {
                infix fun to(                                                   renameTo: String
                ) {
                    val record = RenameRecord(version, renameFrom, renameTo)
                    
                    if (records.containsKey(keyName)  &&  !records[keyName].isNullOrEmpty())
                        records[keyName]!!.add(record)
                    else
                        records[keyName] = mutableListOf(record)
                }
            }
        }
    }
}













