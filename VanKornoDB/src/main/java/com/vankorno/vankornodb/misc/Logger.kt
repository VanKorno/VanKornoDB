package com.vankorno.vankornodb.misc

import android.util.Log
import com.vankorno.vankornodb.core.data.DbConstants.DbTAG

private val unitTestCase: Boolean by lazy {
    val androidTestsRun = try {
        Class.forName("androidx.test.platform.app.InstrumentationRegistry")
        true
    } catch (_: ClassNotFoundException) {
        false
    }
    !androidTestsRun  &&  Thread.currentThread().stackTrace.any { it.className.startsWith("org.junit.") }
}


@PublishedApi
internal fun dLog(                                                                   msg: String
) {
    if (unitTestCase)
        println("Dbg ($DbTAG): $msg")
    else
        Log.d(DbTAG, msg)
}


@PublishedApi
internal fun wLog(                                                                   msg: String
) {
    if (unitTestCase)
        println("Warning ($DbTAG): $msg")
    else
        Log.w(DbTAG, msg)
}


@PublishedApi
internal fun eLog(                                                           msg: String,
                                                                       throwable: Throwable? = null,
) {
    if (unitTestCase) {
        println("ERROR! ($DbTAG): $msg")
        throwable?.printStackTrace()
    } else {
        Log.e(DbTAG, msg, throwable)
    }
}


@PublishedApi
internal fun lambdaError(lambdaName: String) = eLog("$lambdaName should not be empty at this point, but it is.")