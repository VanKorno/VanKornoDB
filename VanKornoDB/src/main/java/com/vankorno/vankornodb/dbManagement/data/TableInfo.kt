package com.vankorno.vankornodb.dbManagement.data

import com.vankorno.vankornodb.api.CurrEntity
import com.vankorno.vankornodb.api.LiteEntity
import com.vankorno.vankornodb.api.OldEntity

open class TableInfoBase<T : BaseEntity>(                             val name: String,
                                                               open val schema: BaseSchemaBundle<T>,
)


open class TableInfoNormal<T : NormalEntity>(                           name: String,
                                                         override val schema: NormalSchemaBundle<T>,
) : TableInfoBase<T>(name, schema)



open class TableInfoCurr<T : CurrEntity>(                                 name: String,
                                                           override val schema: CurrSchemaBundle<T>,
) : TableInfoNormal<T>(name, schema)



open class TableInfoOld<T : OldEntity>(                                    name: String,
                                                            override val schema: OldSchemaBundle<T>,
) : TableInfoNormal<T>(name, schema)



open class TableInfoLite<T : LiteEntity>(                                 name: String,
                                                           override val schema: LiteSchemaBundle<T>,
) : TableInfoBase<T>(name, schema)



infix fun <T : BaseEntity> String.using(schema: BaseSchemaBundle<T>) = TableInfoBase(this, schema)

infix fun <T : NormalEntity> String.using(schema: NormalSchemaBundle<T>) = TableInfoNormal(this, schema)

infix fun <T : CurrEntity> String.using(schema: CurrSchemaBundle<T>) = TableInfoCurr(this, schema)

infix fun <T : OldEntity> String.using(schema: OldSchemaBundle<T>) = TableInfoOld(this, schema)

infix fun <T : LiteEntity> String.using(schema: LiteSchemaBundle<T>) = TableInfoLite(this, schema)









