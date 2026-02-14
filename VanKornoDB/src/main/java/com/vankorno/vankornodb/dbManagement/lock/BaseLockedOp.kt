package com.vankorno.vankornodb.dbManagement.lock

import com.vankorno.vankornodb.api.DbLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class BaseLockedOp<P>(                                         protected val lock: DbLock
) {
    protected inline fun <T> execLocked(crossinline block: ()->T): T = lock.withLock { block() }
    
    protected fun execAsyncLocked(                                                 block: ()->Unit
    ) {
        CoroutineScope(Dispatchers.Default).launch {
            execLocked(block)
        }
    }
    
    protected suspend inline fun <T> execSuspLocked(                   crossinline block: ()->T
    ): T =
        withContext(Dispatchers.Default) {
            execLocked { block() }
        }
}