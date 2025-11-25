package com.vankorno.db

import androidx.compose.runtime.Composable
import com.vankorno.db.MyApp.Companion.dbh
import com.vankorno.db.ui.ConstraintLayer
import com.vankorno.vankornocompose.LibMainActivity

class MainActivity : LibMainActivity() {
    
    @Composable
    override fun AppUI() {
        ConstraintLayer()
    }
    
    override fun appLogic() {
        decideFirstLaunch(
            isDataMissing = false,
            onFirstLaunch = {
                dbh.launchApp()
            }
        )
    }
    
    
    
}