package com.vankorno.sandbox

import androidx.compose.runtime.Composable
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.ui.DemoAppUI
import com.vankorno.vankornocompose.LibMainActivity

class MainActivity : LibMainActivity() {
    
    @Composable
    override fun AppUI() { DemoAppUI() }
    
    override fun beforeStartup() {
        initLambdas()
    }
    
    override fun startupFirstLaunch() { dbh.launchApp() }
    
    override fun goingHome() {  }
    override fun goingBack() {  }
    
    fun initLambdas() {
    }
    
    
    
    
    
}