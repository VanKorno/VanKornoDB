/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.dbManagement.data.NormalEntity
import com.vankorno.vankornodb.dbManagement.data.NormalSchemaBundle

data class MigrationBundle(
                val versionedSchemaBundles: Map<Int, NormalSchemaBundle<out NormalEntity>>,
                         val renameHistory: Map<String, List<RenameRecord>>,
                            val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyMap(), emptyList())
    }
}

