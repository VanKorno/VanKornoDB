/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import kotlin.reflect.KClass

data class MigrationBundle(
                val versionedClasses: Map<Int, KClass<out DbEntity>>,
                         val renameHistory: List<RenameRecord>,
                            val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyList(), emptyList())
    }
}

