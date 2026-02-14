// region License
/** This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *  If a copy of the MPL was not distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/.
**/
// endregion
package com.vankorno.vankornodb.dbManagement.lock

import com.vankorno.vankornodb.api.DbLock

/**
 * Allows running bundles of db operations or even business logic
 * as a single operation with the lock, to avoid conflicts with other ops running at the same time.
 */
abstract class LockedOpsRunnerInternal(                                         val lock: DbLock
) {
    fun <P> exec(block: (P)->Unit) = ExecOp(lock, block)

    fun exec(block: ()->Unit) = ExecOp<Unit>(lock) { block() }

    fun <P, R> get(default: R, block: (P)->R) = GetOp(lock, default, block)

    fun <R> get(default: R, block: ()->R) = GetOp<Unit, R>(lock, default) { block() }
}

