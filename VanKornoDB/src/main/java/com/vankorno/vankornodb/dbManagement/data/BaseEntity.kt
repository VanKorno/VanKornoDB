package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.DbEntity

/** Convenience base for test fixtures. */
abstract class DbEntityWithId : DbEntity {
    override val id: Long = -1L
}
