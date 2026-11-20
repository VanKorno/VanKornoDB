/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.dbManagement.data.SchemaBundle

data class MigrationBundle(
                val versionedSchemaBundles: Map<Int, SchemaBundle<out DbEntity>>,
                         val renameHistory: List<RenameRecord>,
                            val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyList(), emptyList())
    }
}

