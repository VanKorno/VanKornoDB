package com.vankorno.vankornodb.dbManagement

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