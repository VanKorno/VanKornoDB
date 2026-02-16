package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.core.data.DbConstants.DbTAG

private val unitTestCase: Boolean by lazy {
    Thread.currentThread().stackTrace.any { it.className.startsWith("org.junit.") }
}


@PublishedApi
internal fun dLog(                                                                   msg: String
) {
    if (unitTestCase)
        println("Dbg ($DbTAG): $msg")
    else
        dLog(msg)
}


@PublishedApi
internal fun wLog(                                                                   msg: String
) {
    if (unitTestCase)
        println("Warning ($DbTAG): $msg")
    else
        wLog(msg)
}


@PublishedApi
internal fun eLog(                                                           msg: String,
                                                                       throwable: Throwable? = null,
) {
    if (unitTestCase) {
        println("ERROR! ($DbTAG): $msg")
        throwable?.printStackTrace()
    } else {
        eLog(msg, throwable)
    }
}


@PublishedApi
internal fun lambdaError(lambdaName: String) = eLog("$lambdaName should not be empty at this point, but it is.")