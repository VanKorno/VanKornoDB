package com.vankorno.sandbox._entities.crashTester.old.to100.to10

import com.vankorno.vankornodb.api.DbEntity

data class V1_CrashTester(
                           override val id: Long = -1L,
                                  val alias: String = "",
                                  val trace: String = "",
                                  val armed: Int = 0,
                            val retiredCode: String = "",
) : DbEntity
