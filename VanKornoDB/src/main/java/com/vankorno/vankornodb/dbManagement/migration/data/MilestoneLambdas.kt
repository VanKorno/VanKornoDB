package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.dbManagement.migration.dsl.TransformCol

data class MilestoneLambdas(
    val processFinalObj: MigrProcessFinalObj? = null,
    val transformColVal: (TransformCol.()->Unit)? = null,
)

typealias MigrProcessFinalObj = (oldObj: Any, newObj: Any) -> Any
