/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.api

import com.vankorno.vankornodb.core.data.Subquery
import com.vankorno.vankornodb.core.dsl.WhereDslInternal
import com.vankorno.vankornodb.core.dsl.getQuery
import com.vankorno.vankornodb.dbManagement.data.TableInfoBase
import com.vankorno.vankornodb.dbManagement.data.TypedColumn
import com.vankorno.vankornodb.misc.getColNames


class WhereDsl : WhereDslInternal() {
    fun group(                                                              where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        appendGroup(innerBuilder)
    }
    private fun appendGroup(                                                innerBuilder: WhereDsl
    ) {
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        clauses.add("(" + innerBuilder.buildStr() + ")")
        args.addAll(innerBuilder.args)
    }
    
    
    fun and(                                                                where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        if (clauses.isNotEmpty()) // Check if the parent block already has conditions
            clauses.add("AND")
        
        appendGroup(innerBuilder)
    }
    
    fun or(                                                                 where: WhereDsl.()->Unit
    ) {
        val innerBuilder = WhereDsl().apply(where)
        if (innerBuilder.clauses.isEmpty())
            return //\/\/\/\/\/\
        
        if (clauses.isNotEmpty()) // Check if the parent block already has conditions
            clauses.add("OR")
        
        appendGroup(innerBuilder)
    }
    
    fun exists(                                                            subquery: Subquery
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        clauses.add("${missingAnd}EXISTS (${subquery.query})")
        args.addAll(subquery.args)
    }
    
    fun notExists(                                                         subquery: Subquery
    ) {
        val missingAnd = if (clauses.isNotEmpty()) "AND " else ""
        clauses.add("${missingAnd}NOT EXISTS (${subquery.query})")
        args.addAll(subquery.args)
    }
    
    fun subquery(                                                           table: String,
                                                                          columns: Array<out TypedColumn<*>>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery {
        val innerBuilder = getQuery(table, getColNames(columns), orderBy, extra, where)
        return Subquery(innerBuilder.query, innerBuilder.args)
    }
    
    fun subquery(                                                           table: String,
                                                                           column: TypedColumn<*>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery {
        val innerBuilder = getQuery(table, arrayOf(column.name), orderBy, extra, where)
        return Subquery(innerBuilder.query, innerBuilder.args)
    }
    
    fun subquery(                                                           table: String,
                                                                          columns: Array<out String> = arrayOf("*"),
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery {
        val innerBuilder = getQuery(table, columns, orderBy, extra, where)
        return Subquery(innerBuilder.query, innerBuilder.args)
    }
    
    fun subquery(                                                           table: String,
                                                                           column: String,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery {
        val innerBuilder = getQuery(table, arrayOf(column), orderBy, extra, where)
        return Subquery(innerBuilder.query, innerBuilder.args)
    }
    
    fun subquery(                                                           table: TableInfoBase<*>,
                                                                          columns: Array<out TypedColumn<*>>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery = subquery(table.name, columns, orderBy, extra, where)
    
    fun subquery(                                                           table: TableInfoBase<*>,
                                                                           column: TypedColumn<*>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery = subquery(table.name, column, orderBy, extra, where)
    
    fun subquery(                                                           table: TableInfoBase<*>,
                                                                          columns: Array<out String> = arrayOf("*"),
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery = subquery(table.name, columns, orderBy, extra, where)
    
    fun subquery(                                                           table: TableInfoBase<*>,
                                                                           column: String,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
    ): Subquery = subquery(table.name, column, orderBy, extra, where)
}


fun subquery(                                                               table: String,
                                                                          columns: Array<out TypedColumn<*>>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery {
    val innerBuilder = getQuery(table, getColNames(columns), orderBy, extra, where)
    return Subquery(innerBuilder.query, innerBuilder.args)
}

fun subquery(                                                               table: String,
                                                                           column: TypedColumn<*>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery {
    val innerBuilder = getQuery(table, arrayOf(column.name), orderBy, extra, where)
    return Subquery(innerBuilder.query, innerBuilder.args)
}

fun subquery(                                                               table: String,
                                                                          columns: Array<out String> = arrayOf("*"),
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery {
    val innerBuilder = getQuery(table, columns, orderBy, extra, where)
    return Subquery(innerBuilder.query, innerBuilder.args)
}

fun subquery(                                                               table: String,
                                                                           column: String,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery {
    val innerBuilder = getQuery(table, arrayOf(column), orderBy, extra, where)
    return Subquery(innerBuilder.query, innerBuilder.args)
}

fun subquery(                                                               table: TableInfoBase<*>,
                                                                          columns: Array<out TypedColumn<*>>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery = subquery(table.name, columns, orderBy, extra, where)

fun subquery(                                                               table: TableInfoBase<*>,
                                                                           column: TypedColumn<*>,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery = subquery(table.name, column, orderBy, extra, where)

fun subquery(                                                               table: TableInfoBase<*>,
                                                                          columns: Array<out String> = arrayOf("*"),
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery = subquery(table.name, columns, orderBy, extra, where)

fun subquery(                                                               table: TableInfoBase<*>,
                                                                           column: String,
                                                                          orderBy: OrderDsl.()->Unit = {},
                                                                            extra: ExtraDsl.()->Unit = {},
                                                                            where: WhereDsl.()->Unit = {},
): Subquery = subquery(table.name, column, orderBy, extra, where)
