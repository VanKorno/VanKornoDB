package com.vankorno.vankornodb.api
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
import com.vankorno.vankornodb.core.queryBuilder.JoinBuilderInternal
import com.vankorno.vankornodb.core.queryBuilder.QueryOptsInternal
import com.vankorno.vankornodb.core.queryBuilder.WhereBuilderInternal
import com.vankorno.vankornodb.dbManagement.data.EntityColumnsInternal


class QueryOpts : QueryOptsInternal() {
    fun applyOpts(opts: QueryOpts.()->Unit) { this.opts() }
}


class WhereBuilder : WhereBuilderInternal()


class JoinBuilder : JoinBuilderInternal()


interface EntityColumns : EntityColumnsInternal





