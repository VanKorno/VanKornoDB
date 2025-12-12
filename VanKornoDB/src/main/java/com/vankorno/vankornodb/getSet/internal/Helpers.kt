package com.vankorno.vankornodb.getSet.internal

import com.vankorno.vankornodb.api.WhereBuilder
import com.vankorno.vankornodb.core.data.DbConstants

internal fun byIdAnd(                                                     id: Int,
                                                                    andWhere: WhereBuilder.()->Unit,
): WhereBuilder.()->Unit = {
    DbConstants.ID equal id
    andGroup(andWhere)
}