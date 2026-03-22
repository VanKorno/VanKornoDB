/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.dbManagement.lock

import java.util.concurrent.locks.ReentrantLock

internal class DbLockInternal {
    private val lock = ReentrantLock()

    fun <T> doLock(block: () -> T): T {
        lock.lock()
        try {
            return block()
        } finally {
            lock.unlock()
        }
    }
}