package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.api.TransformCol
import com.vankorno.vankornodb.getSet.DbEntity

data class MilestoneLambdas(
    val processFinalObj: MigrProcessFinalObj? = null,
    val transformColVal: (TransformCol.()->Unit)? = null,
)

typealias MigrProcessFinalObj = (oldObj: DbEntity, newObj: DbEntity) -> DbEntity
