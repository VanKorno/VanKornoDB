// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.EntitySpec
import com.vankorno.vankornodb.dbManagement.data.BaseEntity

data class MigrationBundle(
                        val versionedSpecs: Map<Int, EntitySpec<out BaseEntity>>,
                         val renameHistory: Map<String, List<RenameRecord>>,
                            val milestones: List<Pair<Int, MilestoneLambdas>>,
) {
    companion object {
        val EMPTY = MigrationBundle(emptyMap(), emptyMap(), emptyList())
    }
}

