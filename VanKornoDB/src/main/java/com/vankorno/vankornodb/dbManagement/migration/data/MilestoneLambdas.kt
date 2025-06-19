package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.dbManagement.migration.MigrationDSL

data class MilestoneLambdas(
    val processFinalObj: MigrProcessFinalObj? = null,
    val overrideColVal: (MigrationDSL.() -> Unit)? = null
)

typealias MigrProcessFinalObj = (oldObj: Any, newObj: Any) -> Any
