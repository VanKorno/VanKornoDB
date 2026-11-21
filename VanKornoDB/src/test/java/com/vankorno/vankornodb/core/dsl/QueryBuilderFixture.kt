package com.vankorno.vankornodb.core.dsl

import com.vankorno.vankornodb.api.DbEntity
import com.vankorno.vankornodb.core.data.Subquery
import com.vankorno.vankornodb.dbManagement.data.*

internal class BuilderRow(override val id: Long) : DbEntity
internal class BuilderWhere(context: DslContext<BuilderWhere>) : WhereDsl<BuilderWhere>(context) {
    val Id = column(lCol("id"))
    val Name = column(sCol("name"))
    val Type = column(sCol("type"))
    val Position = column(fCol("position"))
    val Bool = column(bCol(com.vankorno.vankornodb.TestConstants.Bool1))
    val Num = column(iCol("cInt"))
    val LongValue = column(lCol("cLong"))
    val Flag = column(bCol("cBool"))
    val Active = column(bCol("active"))
    val Level = column(iCol("level"))
    val Visible = column(bCol("visibleAtHome"))
    val Priority = column(iCol("priority"))
    val A = column(iCol("colA"))
    val B = column(iCol("colB"))
    val Condition = column(bCol("condA"))
    val MinLevel = column(iCol("minLvl"))
}
internal class BuilderOrder : OrderDsl<BuilderWhere, BuilderOrder>()
internal class BuilderExtra : ExtraDsl<BuilderWhere>()
private val schema = SchemaBundle(BuilderRow::class)
internal fun builderTable(name: String) =
    (name using schema).withDsl(::BuilderWhere, { BuilderOrder() }, { BuilderExtra() })

internal fun <W, O : OrderDsl<W, O>, X : ExtraDsl<W>> TableColumns<W, O, X, *>.query(
    orderBy: O.()->Unit = {},
    where: W.()->Unit = {},
): Subquery {
    val result = table.query(selectors, orderBy = orderBy, where = where)
    return Subquery(result.query, result.args)
}

internal fun testPredicate(block: BuilderWhere.()->Unit) = builderTable("Test").predicate("Test", block)

internal fun <C : TypedColumn<*>> testColumn(definition: C) = Field<BuilderWhere, C>(definition)

internal fun testOrder(block: BuilderOrder.()->Unit): BuilderOrder {
    val table = builderTable("Test")
    val context = DslContext(table, WhereDslBase(), table.name)
    fun order(): BuilderOrder = BuilderOrder().also {
        it.context = context
        it.nested = ::order
    }
    return order().apply(block)
}
