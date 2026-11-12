package com.vankorno.sandbox._ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val MOD_DefaultSpacer = Modifier.height(35.dp)
@Composable fun Spa_______________cer() { Spacer(MOD_DefaultSpacer) }

@Composable fun Spa_______________cer(modifier: Modifier) { Spacer(modifier) }

@Composable fun Spa_______________cer(heightDP: Dp) { Spacer(Modifier.height(heightDP)) }

@Composable fun Spa_______________cer(height: Int) { Spacer(Modifier.height(height.dp)) }




@Composable fun Spa_______________cerH(widthDP: Dp) { Spacer(Modifier.width(widthDP)) }

@Composable fun Spa_______________cerH(width: Int) { Spacer(Modifier.width(width.dp)) }


