package com.vankorno.sandbox._navig

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class Screen : Parcelable {
    override fun toString(): String = javaClass.simpleName
}

@Parcelize object ScrHome : Screen()
@Parcelize object ScrSett : Screen()

@Parcelize object ScrPlayground : Screen()
@Parcelize object ScrDbBrowser : Screen()
@Parcelize object ScrTable : Screen()








