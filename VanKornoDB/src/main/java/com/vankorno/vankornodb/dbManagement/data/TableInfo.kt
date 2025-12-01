package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.getSet.DbEntity
import kotlin.reflect.KClass

data class TableInfo (
                                  val name: String,
                           val entityClass: KClass<out DbEntity>,
)