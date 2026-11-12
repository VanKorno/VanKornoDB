package com.vankorno.sandbox

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
abstract class BaseAndroidTest {
    
    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    
    
    
    
    
}
