package com.vankorno.sandbox._entities.crashTester.old.to100.to10

import com.vankorno.vankornodb.api.DbEntity

data class V2_CrashTester(
                           override val id: Long = -1L,
                               val callsign: String = "",
                                  val trace: String = "",
                                  val armed: Int = 0,
                                 val packet: ByteArray = byteArrayOf(),
) : DbEntity
