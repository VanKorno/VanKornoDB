package com.vankorno.vankornodb.dbManagement.migration.data


data class VersionEntity(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                                    val id: Int = -1
)
