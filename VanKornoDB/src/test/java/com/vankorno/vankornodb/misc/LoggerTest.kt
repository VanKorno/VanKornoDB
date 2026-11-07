/* SPDX-License-Identifier: MPL-2.0 */
package com.vankorno.vankornodb.misc

import com.vankorno.vankornodb.core.data.DbConstants.DbTAG
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class LoggerTest {

    @Test
    fun `unitTestCase evaluates to true in local JVM unit test environment`() {
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

        assertFalse("InstrumentationRegistry should not be on classpath during local unit test", androidTestsRun)
        assertTrue("org.junit.Test should be on classpath during local unit test", jUnitTestPresent)
        assertTrue("unitTestCase must evaluate to true in local JVM unit test", unitTestCase)
    }

    @Test
    fun `dLog outputs to stdout without throwing unmocked android Log exception`() {
        val originalOut = System.out
        val outStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))
        try {
            dLog("Debug test message")
            val output = outStream.toString().trim()
            assertEquals("Dbg ($DbTAG): Debug test message", output)
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `wLog outputs to stdout without throwing unmocked android Log exception`() {
        val originalOut = System.out
        val outStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))
        try {
            wLog("Warning test message")
            val output = outStream.toString().trim()
            assertEquals("Warning ($DbTAG): Warning test message", output)
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `eLog outputs to stdout without throwable`() {
        val originalOut = System.out
        val outStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))
        try {
            eLog("Error test message")
            val output = outStream.toString().trim()
            assertEquals("ERROR! ($DbTAG): Error test message", output)
        } finally {
            System.setOut(originalOut)
        }
    }

    @Test
    fun `eLog outputs message to stdout and prints stack trace to stderr when throwable provided`() {
        val originalOut = System.out
        val originalErr = System.err
        val outStream = ByteArrayOutputStream()
        val errStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))
        System.setErr(PrintStream(errStream))
        try {
            val exception = RuntimeException("Test exception for logger")
            eLog("Error with throwable", exception)
            val stdOutput = outStream.toString().trim()
            val errOutput = errStream.toString()
            assertEquals("ERROR! ($DbTAG): Error with throwable", stdOutput)
            assertTrue("Stack trace should contain RuntimeException message", errOutput.contains("java.lang.RuntimeException: Test exception for logger"))
        } finally {
            System.setOut(originalOut)
            System.setErr(originalErr)
        }
    }

    @Test
    fun `lambdaError outputs to stdout`() {
        val originalOut = System.out
        val outStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outStream))
        try {
            lambdaError("myTestLambda")
            val output = outStream.toString().trim()
            assertEquals("ERROR! ($DbTAG): myTestLambda should not be empty at this point, but it is.", output)
        } finally {
            System.setOut(originalOut)
        }
    }
}
