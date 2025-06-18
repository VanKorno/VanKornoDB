package com.vankorno.vankornodb.dbManagement.data

import kotlin.reflect.KClass

data class TableInfo (
    val name: String,
    val entityClass: KClass<*>
)