package com.vankorno.sandbox

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.vankorno.sandbox.AppStart._ui.DemoAppUI
import com.vankorno.sandbox.AppStart.logic.launchApp
import com.vankorno.sandbox._entities.testEntity._Test
import com.vankorno.sandbox._navig.BackNav.goBack
import com.vankorno.sandbox._ui.MainScaffold
import com.vankorno.sandbox._ui.theme.AppColor
import com.vankorno.sandbox._vm.VmShared
import com.vankorno.vankornodb.api.DbRuntime.dbh
import com.vankorno.vankornodb.dbManagement.data.using
import com.vankorno.vankornohelpers.LibMisc
import com.vankorno.vankornohelpers.LibUI
import com.vankorno.vankornohelpers.values.LibGlobals.actExists
import com.vankorno.vankornohelpers.values.LibGlobals.actPaused
import com.vankorno.vankornohelpers.values.LibGlobals.actRunning
import com.vankorno.vankornohelpers.values.LibGlobals.appStarted
import com.vankorno.vankornohelpers.values.LibGlobals.configChangeJustHappened
import com.vankorno.vankornohelpers.values.LibLambdas.longToast
import com.vankorno.vankornohelpers.values.LibLambdas.minimizeApp
import com.vankorno.vankornohelpers.values.LibLambdas.shortToast

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var vm: VmShared
        val DummyTableName = "DummyTable"
        val _DummyTable = DummyTableName using _Test
    }
    
    override fun onCreate(                                            savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        
        actExists = true
        
        enableEdgeToEdge()
        
        LibUI().setWindowBackgroundColor(this, AppColor.Background.argb)
        
        vm = ViewModelProvider(this)[VmShared::class.java]
        
        initLibLambdas()
        
        startup(savedInstanceState)
        
        setBackBtn()
        
        setContent {
            MainScaffold {
                DemoAppUI()
            }
        }
    }
    
    private fun startup(                                              savedInstanceState: Bundle?
    ) {
        launchApp()
        when {
            savedInstanceState == null -> {
                val dummyExists = dbh.tableExists(DummyTableName)
                
                if (!dummyExists) {
                    startupFirstLaunch()
                } else {
                    startupNotFirstLaunch()
                }
                startupLaunch()
            }
            configChangeJustHappened -> {
                startupConfigChange()
            }
            else -> {
                startupAfterProcessDeath()
            }
        }
        configChangeJustHappened = false // Just in case
        appStarted = true
    }
    
    fun startupFirstLaunch() {
        dbh.createTable(_DummyTable)
    }
    fun startupNotFirstLaunch() {}
    fun startupLaunch() {}
    fun startupConfigChange() {}
    
    private fun startupAfterProcessDeath() {
        startupNotFirstLaunch()
        startupLaunch()
    }
    
    
    private fun initLibLambdas() {
        minimizeApp = { finishAffinity() }
        
        longToast = { LibMisc().makeToast(this, it, Toast.LENGTH_LONG) }
        shortToast = { LibMisc().makeToast(this, it, Toast.LENGTH_SHORT) }
    }
    
    
    private fun setBackBtn() {
        onBackPressedDispatcher.addCallback(this) { goBack() }
    }
    
    
    
    
    override fun onPause() {
        super.onPause()
        actRunning = false
        actPaused = true
    }
    override fun onStop() {
        super.onStop()
        actRunning = false
    }
    override fun onDestroy() {
        super.onDestroy()
        actExists = false
        configChangeJustHappened = isChangingConfigurations
    }
    override fun onResume() {
        super.onResume()
        actRunning = true
        actPaused = false
    }
    
}
