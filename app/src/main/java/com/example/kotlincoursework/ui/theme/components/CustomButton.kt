package com.example.kotlincoursework.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsButton(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    buttonText: String,
    onClick: () -> Unit = {},
) {
    val modifier: Modifier = Modifier
        .background(
            color = mainColor,
            shape = RoundedCornerShape(30.dp)
        )
        .fillMaxWidth()
        .height(50.dp)
        .border(
            width = 4.dp,
            color = secondColor,
            shape = RoundedCornerShape(30.dp)
        )

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = mainColor,
        contentColor = textColor
    )
    Button(
        modifier = modifier,
        colors = buttonColors,
        shape = RoundedCornerShape(30.dp),
        onClick = {onClick()}) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            color = textColor
        )
    }
    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun ButtonThirdColor(
    thirdColor: Color,
    textColor: Color,
    onClick: () -> Unit = {},
    buttonText: String
) {
    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = thirdColor,
        contentColor = textColor
    )
    androidx.compose.material.Button(
        modifier = Modifier
            .size(150.dp, 50.dp),
        colors = buttonColors,
        shape = RoundedCornerShape(20.dp),
        onClick = { onClick() }) {
        Text(
            text = buttonText,
            fontSize = 18.sp,
            color = textColor
        )
    }
}