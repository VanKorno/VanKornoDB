package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import kotlin.reflect.KClass

data class MigrationBundle(
              val versionedClasses: Map<Int, KClass<out DbEntity>>,
                 val renameHistory: Map<String, List<RenameRecord>>,
                    val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyMap(), emptyList())
    }
}

