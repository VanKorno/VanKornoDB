package com.vankorno.vankornodb.dbManagement.migration.dsl

import com.vankorno.vankornodb.dbManagement.migration.data.MigrationBundle
import com.vankorno.vankornodb.dbManagement.migration.data.MilestoneLambdas
import kotlin.reflect.KClass


class ModifyRow(val fieldName: String, val block: TransformCol.FieldOverride.()->Unit)


fun defineMigrations(                                latestVersion: Int,
                                                       latestClass: KClass<*>,
                                                             block: MigrationDefinitionBuilder.()->Unit
): MigrationBundle {
    val builder = MigrationDefinitionBuilder()
    builder.block()
    
    builder.versions[latestVersion] = latestClass // always add final version
    
    return MigrationBundle(
        versionedClasses = builder.versions,
        renameHistory = builder.renameHistory,
        milestones = builder.milestones
    )
}


class MigrationDefinitionBuilder() {
    val versions = mutableMapOf<Int, KClass<*>>()
    val renameHistory = mutableMapOf<String, MutableList<Pair<Int, String>>>()
    val milestones = mutableListOf<Pair<Int, MilestoneLambdas>>()
    
    fun version(version: Int, clazz: KClass<*>, block: VersionScope.()->Unit = {}) {
        val scope = VersionScope(version)
        scope.block()
        versions[version] = clazz
        scope.renames.forEach { (newName, oldName) ->
            renameHistory.getOrPut(newName) { mutableListOf() }.add(version to oldName)
        }
        scope.milestone?.let {
            milestones.add(version to it)
        }
    }
    
    inner class VersionScope(val version: Int) {
        val renames = mutableListOf<Pair<String, String>>()
        var milestone: MilestoneLambdas? = null
        
        fun rename(block: MutableList<Pair<String, String>>.()->Unit) {
            renames.block()
        }
        
        infix fun String.modify(block: TransformCol.FieldOverride.()->Unit): ModifyRow =
            ModifyRow(this, block)
        
        fun milestone(
            vararg modifications: ModifyRow,
            processFinalObj: ((Any, Any) -> Any)? = null
        ) {
            val overrideBlock: TransformCol.()->Unit = {
                modifications.forEach { modify(it.fieldName, it.block) }
            }
            milestone = MilestoneLambdas(
                transformColVal = overrideBlock,
                processFinalObj = processFinalObj
            )
        }
    }
}


