package com.vankorno.db

import com.vankorno.db.actions.LaunchApp

fun LocalDbHelper.launchApp() = write("launchApp") { LaunchApp(it).launch() }