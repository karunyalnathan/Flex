package com.example.flex.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun FlexButton(onClick: () -> Unit,     modifier: Modifier = Modifier,
               content: @Composable RowScope.() -> Unit) {
    Button(onClick = {onClick()}, modifier = modifier, colors = FlexButtonColors()) {
        content()
    }
}

@Composable
fun FlexOutlinedButton(onClick: () -> Unit,     modifier: Modifier = Modifier,
               content: @Composable RowScope.() -> Unit) {
    OutlinedButton(onClick = {onClick()}, modifier = modifier, colors = FlexOutlinedButtonColors(),
        border = BorderStroke(
            width = 1.dp,
            color = Blue200,
        )) {
        content()
    }
}



@Composable
fun Divider() {
    Row(Modifier.fillMaxWidth().height(1.dp).padding(16.dp, 0.dp, 0.dp, 0.dp).background(Color.LightGray)) {}
}