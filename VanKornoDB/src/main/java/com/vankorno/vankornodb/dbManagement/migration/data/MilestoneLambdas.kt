/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.api.TransformColDsl

data class MilestoneLambdas(
    val processFinalEntity: MigrProcessFinalEntity? = null,
    val transformColVal: (TransformColDsl.()->Unit)? = null,
)

typealias MigrProcessFinalEntity = (oldEntity: DbEntity, newEntity: DbEntity) -> DbEntity
