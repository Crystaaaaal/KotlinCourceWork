package com.example.kotlincoursework.ui.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Toast(
    message: String,
    visible: Boolean,
    mainColor:Color,
    textColor: Color,
    secondColor: Color
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { -40 },
        exit = fadeOut() + slideOutVertically { -40 }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    width = 4.dp,
                    color = Color.Red,
                    shape = RoundedCornerShape(20.dp)

                ),
            contentAlignment = Alignment.TopCenter,


        ) {
            Text(
                text = message,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .background(mainColor, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }
}