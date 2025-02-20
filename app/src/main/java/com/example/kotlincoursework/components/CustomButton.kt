package com.example.kotlincoursework.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButton(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    buttonText: String
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
        onClick = {}) {
        Text(
            text = buttonText,
            color = textColor
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
}