package com.vankorno.sandbox

import androidx.compose.runtime.Composable
import com.vankorno.sandbox.MyApp.Companion.dbh
import com.vankorno.sandbox.navig.goBack
import com.vankorno.sandbox.navig.goTo
import com.vankorno.sandbox.navig.updateEveryMinute
import com.vankorno.sandbox.ui.DemoAppUI
import com.vankorno.vankornocompose.LibMainActivity
import com.vankorno.vankornocompose.navig.ScrHome

class MainActivity : LibMainActivity() {
    
    @Composable
    override fun AppUI() { DemoAppUI() }
    
    override fun beforeStartup() {
        
    }
    
    override fun startupFirstLaunch() { dbh.launchApp() }
    
    override fun doEveryMinute() { dbh.updateEveryMinute() }
    
    override fun goingHome() { dbh.goTo(ScrHome) }
    override fun goingBack() { dbh.goBack() }
    
    
    
    
    
    
}