package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.OldEntity


interface BaseEntity {
    val id: Int
}

interface NormalEntity : BaseEntity





// Mainly for tests:

abstract class CurrEntityWithId : CurrEntity {
    override val id: Int = -1
}


abstract class OldEntityWithId : OldEntity {
    override val id: Int = -1
}

