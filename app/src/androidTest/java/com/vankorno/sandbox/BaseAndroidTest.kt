package com.vankorno.sandbox

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vankorno.sandbox.MyApp.Companion.androidTestRun
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
open class BaseAndroidTest {
    
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    
    
    @Before
    open fun baseSetup() {
        androidTestRun = true
    }
    
    
}