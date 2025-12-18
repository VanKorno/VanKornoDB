// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.TransformCol

data class MilestoneLambdas(
    val processFinalObj: MigrProcessFinalObj? = null,
    val transformColVal: (TransformCol.()->Unit)? = null,
)

typealias MigrProcessFinalObj = (oldObj: DbEntity, newObj: DbEntity) -> DbEntity
