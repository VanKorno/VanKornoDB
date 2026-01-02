package com.vankorno.sandbox

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.vankorno.sandbox.AppStart.DemoApp.Companion.dbh
import com.vankorno.sandbox.AppStart._ui.DemoAppUI
import com.vankorno.sandbox._navig.goBack
import com.vankorno.sandbox._navig.goTo
import com.vankorno.sandbox._navig.updateEveryMinute
import com.vankorno.sandbox._vm.VmShared
import com.vankorno.vankornocompose.LibMainActivity
import com.vankorno.vankornocompose.navig.ScrHome

class MainActivity : LibMainActivity() {
    companion object {
        lateinit var vm: VmShared
    }
    
    @Composable
    override fun AppUI() { DemoAppUI() }
    
    override fun beforeStartup() {
        vm = ViewModelProvider(this)[VmShared::class.java]
    }
    
    override fun startupFirstLaunch() { dbh.launchApp() }
    
    override fun doEveryMinute() { dbh.updateEveryMinute() }
    
    override fun goingHome() { dbh.goTo(ScrHome) }
    override fun goingBack() { dbh.goBack() }
    
    
    
    
    
    
}