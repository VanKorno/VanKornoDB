package com.vankorno.sandbox.navig

import com.vankorno.sandbox.LocalDbHelper
import com.vankorno.vankornocompose.navig.Screen


fun LocalDbHelper.updateEveryMinute() = write("drainPoints", true) { UpdateScr(it).updateEveryMinute() }


fun LocalDbHelper.updateScreen() = write("updateScreen", true) { UpdateScr(it).updateScr() }

fun LocalDbHelper.updateScreenBlocking() = write("updateScreen", false) { UpdateScr(it).updateScr() }



fun LocalDbHelper.goTo(screen: Screen) = write("goTo", true) { Navig(it).goTo(screen) }

fun LocalDbHelper.goToBlocking(screen: Screen) = write("goTo") { Navig(it).goTo(screen) }



fun LocalDbHelper.goBack() = write("goBack", true) { BackClick(it).goBack() }

fun LocalDbHelper.goBackBlocking() = write("goBack", false) { BackClick(it).goBack() }
