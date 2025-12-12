package com.vankorno.vankornodb.get.raw.data

data class RawTableStr(
                               val columns: List<String>,
                                 val types: List<String>,
                                  val rows: List<List<String>>,
)

