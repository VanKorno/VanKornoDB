package com.vankorno.db.entities.versions

const val EnttVersion = "version"


data class VersionEntity(
                                  val name: String = "",
                               val version: Int = 0,
                                 val notes: String = "",

                                    val id: Int = -1
)
