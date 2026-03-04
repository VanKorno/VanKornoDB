package com.vankorno.sandbox

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.vankorno.sandbox.AppStart._ui.DemoAppUI
import com.vankorno.sandbox.AppStart.logic.launchApp
import com.vankorno.sandbox._vm.VmShared
import com.vankorno.vankornocompose.LibMainActivity
import com.vankorno.vankornocompose.navig.Navig.updateScreen

class MainActivity : LibMainActivity() {
    companion object {
        lateinit var vm: VmShared
    }
    
    @Composable
    override fun AppUI() { DemoAppUI() }
    
    override fun beforeStartup() {
        vm = ViewModelProvider(this)[VmShared::class.java]
    }
    
    override fun startupLaunch() { launchApp() }
    
    
    override fun doEveryMinute() { updateScreen.async() }
    
    
    
    
    
    
    
}