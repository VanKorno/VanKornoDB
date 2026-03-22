package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.OldEntity


interface BaseEntity {
    val id: Long
}

interface NormalEntity : BaseEntity





// Mainly for tests:

abstract class CurrEntityWithId : CurrEntity {
    override val id: Long = -1L
}


abstract class OldEntityWithId : OldEntity {
    override val id: Long = -1L
}

