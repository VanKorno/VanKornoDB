package com.vankorno.sandbox._entities.crashTester.old.to100.to10

import com.vankorno.vankornodb.api.DbEntity

data class V3_CrashTester(
                           override val id: Long = -1L,
                                   val name: String = "",
                                val enabled: Boolean = false,
                                 val packet: ByteArray = byteArrayOf(),
                             val sampleList: List<Int> = List(4) { 0 },
                              val labelList: List<String> = List(2) { "" },
) : DbEntity
