package com.vankorno.vankornodb.misc

import android.util.Log

private val unitTestCase: Boolean by lazy {
    Thread.currentThread().stackTrace.any { it.className.startsWith("org.junit.") }
}


internal fun dLog(                                                                   tag: String,
                                                                                     msg: String,
) {
    if (unitTestCase)
        println("Dbg ($tag): $msg")
    else
        Log.d(tag, msg)
}


internal fun wLog(                                                                   tag: String,
                                                                                     msg: String,
) {
    if (unitTestCase)
        println("Warning ($tag): $msg")
    else
        Log.w(tag, msg)
}


internal fun eLog(                                                           tag: String,
                                                                             msg: String,
                                                                       throwable: Throwable? = null,
) {
    if (unitTestCase) {
        println("ERROR! ($tag): $msg")
        throwable?.printStackTrace()
    } else {
        Log.e(tag, msg, throwable)
    }
}


internal fun lambdaError(lambdaName: String) = eLog("Empty lambda!", "$lambdaName should not be empty at this point, but it is.")