package com.example.kotlincoursework.ui.theme.components

import android.inputmethodservice.Keyboard
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun SettingsButton(
    buttonText: String,
    onClick: () -> Unit = {},
    warningColor: Boolean = false
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    val modifier: Modifier = Modifier
        .background(
            color = color.background,
            shape = RoundedCornerShape(30.dp)
        )
        .fillMaxWidth()
        .height(50.dp)
        .border(
            width = 4.dp,
            color = color.primary,
            shape = RoundedCornerShape(30.dp)
        )

    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = color.background,
        contentColor = color.onBackground
    )
    Button(
        modifier = modifier,
        colors = buttonColors,
        shape = RoundedCornerShape(30.dp),
        onClick = { onClick() }) {
        Text(
            text = buttonText,
            fontSize = 16.sp,
            color = if (warningColor) color.error else color.onPrimary
        )
    }
    Spacer(modifier = Modifier.height(30.dp))
}


@Composable
fun ButtonThirdColor(
    onClick: () -> Unit = {},
    buttonText: String
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    val buttonColors = ButtonDefaults.buttonColors(
        backgroundColor = color.secondary,
        contentColor = color.onSecondary
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
        )
    }
}

@Composable
fun SwapColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val themColor = androidx.compose.material3.MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(
                color = color,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = if (isSelected) 4.dp else -1.dp,
                color = themColor.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
    )
}

@Preview
@Composable
fun CustomRoundedButtonPreview() {

}

