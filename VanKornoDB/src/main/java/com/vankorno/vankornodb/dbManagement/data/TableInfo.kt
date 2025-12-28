package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.CurrEntity

data class TableInfo (
                                  val name: String,
                             val ormBundle: CurrOrmBundle<out CurrEntity>,
)