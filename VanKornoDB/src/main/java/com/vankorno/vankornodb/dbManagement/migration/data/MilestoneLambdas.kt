package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.dbManagement.migration.MigrationDSL

data class MilestoneLambdas(
    val overrideColVal: (MigrationDSL.() -> Unit)? = null,
    val processFinalObj: MigrProcessFinalObj? = null
)

typealias MigrProcessFinalObj = (oldObj: Any, newObj: Any) -> Any
