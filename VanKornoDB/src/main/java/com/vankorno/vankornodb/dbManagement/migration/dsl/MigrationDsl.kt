/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.MigrationDsl
import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle
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
internal fun <T: DbEntity> defineMigrationsInternal(           latestVersion: Int,
                                                          latestSchemaBundle: SchemaBundle<T>,
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
    val versionedSchemaBundles = mutableMapOf<Int, SchemaBundle<out DbEntity>>()
    val renameHistory = mutableListOf<RenameRecord>()
    val milestones = mutableListOf<Pair<Int, MilestoneLambdas>>()
    
    /**
     * Registers a schema bundle for a specific entity version and configures its optional column renames or milestone steps.
     *
     * @param version The entity version number.
     * @param schemaBundle The schema bundle defining this version's entity class and structure.
     * @param block Configuration block for renames and milestones at this version.
     */
    fun <T : DbEntity> version(                               version: Int,
                                                         schemaBundle: SchemaBundle<T>,
                                                                block: VersionBuilder.()->Unit = {},
    ) {
        val verBuilder = VersionBuilder(version)
        verBuilder.block()
        
        // Class
        versionedSchemaBundles[version] = schemaBundle
        
        // Renames
        renameHistory.addAll(verBuilder.pendingRenames)
        
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
        val pendingRenames = mutableListOf<RenameRecord>()
        
        /**
         * Defines column rename history for this version.
         */
        fun rename(                                                   block: RenameBuilder.()->Unit
        ) {
            val renameBuilder = RenameBuilder(version).apply(block)
            pendingRenames.addAll(renameBuilder.records)
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
                    processFinalEntity: ((oldEntity: DbEntity, newEntity: DbEntity)->DbEntity)? = null,
        ) {
            val overrideBlock: TransformColDsl.() -> Unit = {
                modifications.forEach { modify(it.colName, it.block) }
            }
            milestone = MilestoneLambdas(
                transformColVal = overrideBlock,
                processFinalEntity = processFinalEntity,
            )
        }
        
        
        
        /**
         * Builder for recording column renames across entity versions.
         */
        class RenameBuilder(                                                 val version: Int
        ) {
            val records = mutableListOf<RenameRecord>()
            
            /** "old" to "new" */
            infix fun String.to(                                                renameTo: String
            ) {
                records.add(RenameRecord(version, from = this, to = renameTo))
            }
            
            /** "old" to newTypedCol */
            infix fun String.to(                                           renameTo: TypedColumn<*>
            ) {
                records.add(RenameRecord(version, from = this, to = renameTo.name))
            }
            
            /** oldTypedCol to "new" */
            infix fun TypedColumn<*>.to(                                        renameTo: String
            ) {
                records.add(RenameRecord(version, from = this.name, to = renameTo))
            }
            
            /** oldTypedCol to newTypedCol */
            infix fun TypedColumn<*>.to(                                   renameTo: TypedColumn<*>
            ) {
                records.add(RenameRecord(version, from = this.name, to = renameTo.name))
            }
        }
    }
}
