package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants

internal fun byIdAnd(                                                     id: Int,
                                                                    andWhere: WhereBuilder.()->Unit,
): WhereBuilder.()->Unit = {
    DbConstants._ID equal id
    andGroup(andWhere)
}

internal fun byNameAnd(                                                 name: String,
                                                                    andWhere: WhereBuilder.()->Unit,
): WhereBuilder.()->Unit = {
    DbConstants._Name equal name
    andGroup(andWhere)
}

