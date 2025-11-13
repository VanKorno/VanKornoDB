package com.vankorno.vankornodb.dbManagement.migration.data

import kotlin.reflect.KClass

data class MigrationBundle(
              val versionedClasses: Map<Int, KClass<*>>,
                 val renameHistory: Map<String, List<RenameRecord>>,
                    val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyMap(), emptyList())
    }
}

