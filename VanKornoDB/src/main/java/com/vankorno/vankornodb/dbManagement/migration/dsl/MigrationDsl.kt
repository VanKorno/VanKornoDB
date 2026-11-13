/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.MigrationDsl
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.dbManagement.data.CurrSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.NormalSchemaBundle
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import com.vankorno.vankornodb.dbManagement.migration.data.RenameRecord


/**
 * Encapsulates a column modification rule for a milestone migration step.
 */
class ColModification(val colName: String, val block: TransformColDslInternal.ColOverride.()->Unit)


/**
 * Builds a [MigrationBundle] from the version specifications and milestone rules configured in [block].
 */
internal fun <T: CurrEntity> defineMigrationsInternal(         latestVersion: Int,
                                                          latestSchemaBundle: CurrSchemaBundle<T>,
                                                                       block: MigrationDsl.()->Unit,
): MigrationBundle {
    val defBuilder = MigrationDsl()
    defBuilder.block()
    
    val latestVersionAbsent = !defBuilder.versionedSchemaBundles.containsKey(latestVersion)
    if (latestVersionAbsent)
        defBuilder.versionedSchemaBundles[latestVersion] = latestSchemaBundle
    
    return MigrationBundle(
        versionedSchemaBundles = defBuilder.versionedSchemaBundles,
        renameHistory = defBuilder.renameHistory,
        milestones = defBuilder.milestones
    )
}


/**
 * DSL builder for defining database migration configurations across versioned entity schemas.
 *
 * Simple type conversions between numeric primitives and `Boolean` (`Int`, `Long`, `Float`, `Double`, `Boolean`)
 * are handled automatically between entity versions. Transformations involving `String`, `ByteArray`, or collections
 * require explicit milestone steps via [VersionBuilder.milestone].
 */
abstract class MigrationDslInternal {
    val versionedSchemaBundles = mutableMapOf<Int, NormalSchemaBundle<out NormalEntity>>()
    val renameHistory = mutableMapOf<String, MutableList<RenameRecord>>()
    val milestones = mutableListOf<Pair<Int, MilestoneLambdas>>()
    
    /**
     * Registers a schema bundle for a specific entity version and configures its optional column renames or milestone steps.
     *
     * @param version The entity version number.
     * @param schemaBundle The schema bundle defining this version's entity class and structure.
     * @param block Configuration block for renames and milestones at this version.
     */
    fun <T : NormalEntity> version(                           version: Int,
                                                         schemaBundle: NormalSchemaBundle<T>,
                                                                block: VersionBuilder.()->Unit = {},
    ) {
        val verBuilder = VersionBuilder(version)
        verBuilder.block()
        
        // Class
        versionedSchemaBundles[version] = schemaBundle
        
        // Renames
        for ((keyName, colRenames) in verBuilder.pendingRenames) {
            
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
    
    
    
    /**
     * Builder for configuring a specific entity version, including column rename history and milestone transformations.
     */
    class VersionBuilder(                                                    val version: Int
    ) {
        var milestone: MilestoneLambdas? = null
        val pendingRenames = mutableMapOf<String, MutableList<RenameRecord>>()
        
        /**
         * Defines column rename history for this version.
         */
        fun rename(                                                   block: RenameBuilder.()->Unit
        ) {
            val renameBuilder = RenameBuilder(version).apply(block)
            
            for ((keyName, colRenames) in renameBuilder.records) {
                
                if (pendingRenames.containsKey(keyName)  &&  !pendingRenames[keyName].isNullOrEmpty()) {
                    for (rename in colRenames) {
                        pendingRenames[keyName]!!.add(rename)
                    }
                } else {
                    pendingRenames[keyName] = colRenames
                }
            }
        }
        
        
        /**
         * Creates a column modification rule by column name for use in a milestone step.
         */
        infix fun String.modify(block: TransformColDslInternal.ColOverride.()->Unit): ColModification = ColModification(this, block)
        
        /**
         * Creates a column modification rule by [TypedColumn] for use in a milestone step.
         */
        infix fun TypedColumn<*>.modify(block: TransformColDslInternal.ColOverride.()->Unit): ColModification = ColModification(this.name, block)
        
        
        /**
         * Marks this version as a non-skippable milestone step in the migration chain.
         *
         * Allows executing custom column transformations (e.g. for conversions not handled automatically)
         * or post-processing the entire migrated entity object.
         *
         * @param modifications Column modification rules applied during this step.
         * @param processFinalEntity Optional post-processing lambda to transform the entity after column values are mapped.
         */
        fun milestone(
               vararg modifications: ColModification,
                    processFinalEntity: ((oldEntity: NormalEntity, newEntity: NormalEntity)->NormalEntity)? = null,
        ) {
            val overrideBlock: TransformColDsl.() -> Unit = {
                modifications.forEach { modify(it.colName, it.block) }
            }
            milestone = MilestoneLambdas(
                transformColVal = overrideBlock,
                processFinalEntity = processFinalEntity
            )
        }
        
        
        
        /**
         * Builder for recording column renames across entity versions.
         */
        class RenameBuilder(                                                 val version: Int
        ) {
            val records = mutableMapOf<String, MutableList<RenameRecord>>()
            
            /** Specifies the source column name to rename from using a [TypedColumn]. */
            infix fun TypedColumn<*>.from(old: String) = PartialRename(this.name, old)
            
            /** Specifies the source column name to rename from using a column name string. */
            infix fun String.from(old: String) = PartialRename(this, old)
            
            
            inner class PartialRename(                                       val keyName: String,
                                                                          val renameFrom: String,
            ) {
                /** Maps the old column name [renameFrom] to the new column name [renameTo]. */
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













