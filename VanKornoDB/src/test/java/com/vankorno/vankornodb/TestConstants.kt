package com.vankorno.vankornodb

import com.vankorno.vankornodb.dbManagement.data.bCol
import com.vankorno.vankornodb.dbManagement.data.fCol
import com.vankorno.vankornodb.dbManagement.data.iCol
import com.vankorno.vankornodb.dbManagement.data.lCol
import com.vankorno.vankornodb.dbManagement.data.sCol

internal object TestConstants {
    const val DirtyTable: String = "DirtyTable"
    const val BestName: String = "BestName"
    const val Bool1: String = "Bool1"
    const val Users: String = "Users"
    const val UserID: String = "user_id"
    const val Posts: String = "Posts"
    
    val cInfo by lazy { sCol("info") }
    
    val cInt by lazy { iCol("cInt") }
    val cStr by lazy { sCol("cStr") }
    val cBool by lazy { bCol("cBool") }
    val cLong by lazy { lCol("cLong") }
    val cFloat by lazy { fCol("cFloat") }
}
