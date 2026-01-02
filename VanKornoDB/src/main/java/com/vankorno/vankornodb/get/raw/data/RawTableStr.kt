package com.vankorno.vankornodb.get.raw.data

data class RawTableStr(
                               val columns: List<String> = emptyList(),
                                 val types: List<String> = emptyList(),
                                  val rows: List<List<String>> = emptyList(),
)

