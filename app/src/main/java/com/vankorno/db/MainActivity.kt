package com.vankorno.db

import androidx.compose.runtime.Composable
import com.vankorno.db.MyApp.Companion.dbh
import com.vankorno.db.ui.ConstraintLayer
import com.vankorno.vankornocompose.LibMainActivity
import com.vankorno.vankornohelpers.values.LibColors.PlainBlack

class MainActivity : LibMainActivity(
    underAppColor = PlainBlack
) {
    
    @Composable
    override fun AppUI() {
        ConstraintLayer()
    }
    
    override fun beforeStartup() {
        
    }
    
    override fun startupFirstLaunch() {
        dbh.launchApp()
    }
    
    
    
}