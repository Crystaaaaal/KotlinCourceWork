package com.example.kotlincoursework.ui.theme.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun CustomToggleSwitch(
    SwitchValue: Boolean,
    onValueChange: (Boolean) -> Unit,
    onClick: () -> Unit = {},
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    // Анимация для плавного перемещения ползунка
    val thumbOffset by animateDpAsState(
        targetValue = if (SwitchValue) 150.dp - 40.dp - 20.dp else 0.dp,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .width(150.dp) // Ширина переключателя
            .height(50.dp) // Высота переключателя
            .background(
                color = if (SwitchValue) color.primary else color.background,
                shape = RoundedCornerShape(15.dp) // Закруглённые углы
            )
            .border(
                width = 4.dp, // Окантовка
                color = color.outline,
                shape = RoundedCornerShape(15.dp) // Закруглённые углы окантовки
            )
            .clickable { onValueChange(!SwitchValue)
                onClick()} // Обработка клика
            .padding(10.dp) // Отступ для ползунка
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset) // Анимация перемещения
                .size(30.dp) // Размер ползунка
                .clip(CircleShape)
                .background(color.outline) // Закруглённые углы ползунка

        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppearancePreview() {
    KotlinCourseWorkTheme {


        var SwitchValue by remember { mutableStateOf(false) }
        CustomToggleSwitch(
            SwitchValue,
            {newState -> SwitchValue = newState }
        )

    }
}