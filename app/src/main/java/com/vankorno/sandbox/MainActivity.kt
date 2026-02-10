package com.vankorno.sandbox

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import com.vankorno.sandbox.AppStart._ui.DemoAppUI
import com.vankorno.sandbox._navig.BackClick
import com.vankorno.sandbox._navig.Navig
import com.vankorno.sandbox._navig.UpdateScr
import com.vankorno.sandbox._vm.VmShared
import com.vankorno.vankornocompose.LibMainActivity
import com.vankorno.vankornocompose.navig.ScrHome
import com.vankorno.vankornodb.api.DbRuntime.lops

class MainActivity : LibMainActivity() {
    companion object {
        lateinit var vm: VmShared
    }
    
    @Composable
    override fun AppUI() { DemoAppUI() }
    
    override fun beforeStartup() {
        vm = ViewModelProvider(this)[VmShared::class.java]
    }
    
    override fun startupFirstLaunch() { lops.launchApp() }
    
    override fun doEveryMinute() { lops.exec { UpdateScr().updateEveryMinute() } }
    
    override fun goingHome() { Navig().goTo(ScrHome) }
    override fun goingBack() { lops.exec { BackClick().goBack() } }
    
    
    
    
    
    
}