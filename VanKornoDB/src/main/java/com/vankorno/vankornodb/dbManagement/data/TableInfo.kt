package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.EntitySpec

data class TableInfo (
                                  val name: String,
                                  val spec: EntitySpec<out BaseEntity>,
)