/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoggerAndroidTest {

    @Test
    fun unitTestCaseEvaluatesToFalseInAndroidTestEnvironment() {
        val androidTestsRun = try {
            Class.forName("androidx.test.platform.app.InstrumentationRegistry")
            true
        } catch (_: ClassNotFoundException) {
            false
        }
        val jUnitTestPresent = try {
            Class.forName("org.junit.Test")
            true
        } catch (_: ClassNotFoundException) {
            false
        }

        assertTrue("InstrumentationRegistry must be present on runtime classpath in Android test", androidTestsRun)
        assertTrue("org.junit.Test must be present on runtime classpath in Android test", jUnitTestPresent)
        assertFalse("unitTestCase must evaluate to false in Android instrumented test environment", unitTestCase)
    }

    @Test
    fun androidLoggingFunctionsExecuteWithoutErrors() {
        // In Android instrumentation tests, unitTestCase is false, so dLog/wLog/eLog invoke android.util.Log
        dLog("Android instrumentation debug log test")
        wLog("Android instrumentation warning log test")
        eLog("Android instrumentation error log test")
        eLog("Android instrumentation error log test with exception", RuntimeException("Android log exception test"))
        lambdaError("testAndroidLambda")
    }
}
