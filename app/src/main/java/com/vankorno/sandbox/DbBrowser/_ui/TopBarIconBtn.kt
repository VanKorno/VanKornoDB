package com.vankorno.sandbox.DbBrowser._ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vankorno.sandbox._ui.theme.AppColor
import com.vankorno.vankornohelpers.values.LibColors.WhiteTransp

@Composable
fun TopBarIconBtn(                                                          enabled: Boolean,
                                                                               icon: Int,
                                                                        onLongClick: ()->Unit = {},
                                                                            onClick: ()->Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    Box(
        Modifier
            .size(44.dp)
            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource,
                indication = null
            )
            .padding(6.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(id = icon),
            tint =  if (enabled)
                        AppColor.WhiteText.color
                    else
                        Color(WhiteTransp)
            ,
            contentDescription = null
        )
    }
}
