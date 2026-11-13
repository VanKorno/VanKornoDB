/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.TransformColDsl
import com.vankorno.vankornodb.dbManagement.data.NormalEntity

data class MilestoneLambdas(
    val processFinalEntity: MigrProcessFinalEntity? = null,
    val transformColVal: (TransformColDsl.()->Unit)? = null,
)

typealias MigrProcessFinalEntity = (oldEntity: NormalEntity, newEntity: NormalEntity) -> NormalEntity
