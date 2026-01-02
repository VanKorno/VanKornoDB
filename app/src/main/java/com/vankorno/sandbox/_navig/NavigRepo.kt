package com.vankorno.sandbox._navig

import com.vankorno.vankornocompose.navig.Screen
import com.vankorno.vankornodb.api.DbHelper


fun DbHelper.updateEveryMinute() = write("drainPoints", true) { UpdateScr(it).updateEveryMinute() }


fun DbHelper.updateScreen() = write("updateScreen", true) { UpdateScr(it).updateScr() }

fun DbHelper.updateScreenBlocking() = write("updateScreen", false) { UpdateScr(it).updateScr() }



fun DbHelper.goTo(screen: Screen) = write("goTo", true) { Navig(it).goTo(screen) }

fun DbHelper.goToBlocking(screen: Screen) = write("goTo") { Navig(it).goTo(screen) }



fun DbHelper.goBack() = write("goBack", true) { BackClick(it).goBack() }

fun DbHelper.goBackBlocking() = write("goBack", false) { BackClick(it).goBack() }
