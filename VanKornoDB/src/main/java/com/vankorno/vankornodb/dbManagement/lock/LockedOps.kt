package com.vankorno.vankornodb.dbManagement.lock

import com.vankorno.vankornodb.api.DbLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExecOp<P>(                                                                    lock: DbLock,
                                                                       private val block: (P)->Unit,
) : BaseLockedOp<P>(lock) {

    fun exec(param: P) { execLocked { block(param) } }

    fun async(param: P) { execAsyncLocked { block(param) } }

    suspend fun susp(param: P) { execSuspLocked { block(param) } }

    operator fun invoke(param: P) = exec(param)
}



class GetOp<P, R>(                                                                  lock: DbLock,
                                                                     private val default: R,
                                                                       private val block: (P)->R,
) : BaseLockedOp<P>(lock) {

    fun get(param: P): R =
        try {
            execLocked { block(param) }
        } catch (_: Exception) {
            default
        }

    fun async(param: P, callback: (R) -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            callback(get(param))
        }
    }

    suspend fun susp(param: P): R =
        try {
            execSuspLocked { block(param) }
        } catch (_: Exception) {
            default
        }

    operator fun invoke(param: P): R = get(param)
}



