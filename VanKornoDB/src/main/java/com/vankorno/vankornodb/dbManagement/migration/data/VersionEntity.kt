package com.vankorno.vankornodb.dbManagement.migration.data

import com.vankorno.vankornodb.getSet.DbEntity


data class VersionEntity(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                                    val id: Int = -1,
) : DbEntity
