package com.vankorno.sandbox._ui.theme

import androidx.compose.ui.graphics.Color
import com.vankorno.vankornohelpers.values.LibColors.*

// functional colors
private const val IntColorBackground = 0xFF151515.toInt()
private const val IntColorSurfaceD = 0xFF2F2F2F.toInt()
private const val IntColorTextField = 0xFF353535.toInt()
private const val IntColorTextFieldDark = 0xFF2A2A2A.toInt()

private const val IntColorWhiteText = 0xFFECECEC.toInt()

private const val IntColorBlackSurf = 0xFF000000.toInt()
private const val IntColorBlackSurfDisabled = 0x4D000000.toInt()
private const val IntColorBlackSurfBorder = 0xFF5A5A5A.toInt()
private const val IntColorBlackSurfBorderDisabled = 0x1AFFFFFF.toInt()



enum class AppColor(                                                           val color: Color,
                                                                                val argb: Int,
) {
    Background(Color(IntColorBackground), IntColorBackground),
    Surface(Color(IntColorSurfaceD), IntColorSurfaceD),
    TextField(Color(IntColorTextField), IntColorTextField),
    TextFieldDark(Color(IntColorTextFieldDark), IntColorTextFieldDark),
    
    WhiteText(Color(IntColorWhiteText), IntColorWhiteText),
    BlackSurf(Color(IntColorBlackSurf), IntColorBlackSurf),
    BlackSurfDisabled(Color(IntColorBlackSurfDisabled), IntColorBlackSurfDisabled),
    BlackSurfBorder(Color(IntColorBlackSurfBorder), IntColorBlackSurfBorder),
    BlackSurfBorderDisabled(Color(IntColorBlackSurfBorderDisabled), IntColorBlackSurfBorderDisabled),
    
    BlackScrim(Color(BlackT90), BlackT90),
}


enum class AccentColor(                                                        val color: Color,
                                                                                val argb: Int,
) {
    Black(Color(PlainBlack), PlainBlack),
    Grey(Color(PlainGrey), PlainGrey),
    
    Red(Color(PlainRed), PlainRed),
    Pink(Color(PlainPink), PlainPink),
    Magenta(Color(PlainMagenta), PlainMagenta),
    Violet(Color(PlainViolet), PlainViolet),
    Blue(Color(PlainBlueDark), PlainBlueDark),
    LightBlue(Color(PlainBlueLight), PlainBlueLight),
    Green(Color(PlainGreenDark), PlainGreenDark),
    LightGreen(Color(PlainGreenLight), PlainGreenLight),
    Olive(Color(PlainOlive), PlainOlive),
    Brown(Color(PlainBrown), PlainBrown),
    Yellow(Color(PlainYellow), PlainYellow),
    Avocado(Color(PlainAvocado), PlainAvocado),
    Orange(Color(PlainOrange), PlainOrange),
    Cyan(Color(PlainCyan), PlainCyan),
}
