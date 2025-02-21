package com.example.kotlincoursework.components

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
import androidx.compose.runtime.*
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
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    SwitchValue: Boolean,
    onValueChange: (Boolean) -> Unit,
) {


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
                color = if (SwitchValue) Color.Green else thirdColor,
                shape = RoundedCornerShape(15.dp) // Закруглённые углы
            )
            .border(
                width = 4.dp, // Окантовка
                color = secondColor,
                shape = RoundedCornerShape(15.dp) // Закруглённые углы окантовки
            )
            .clickable { onValueChange(!SwitchValue) } // Обработка клика
            .padding(10.dp) // Отступ для ползунка
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset) // Анимация перемещения
                .size(30.dp) // Размер ползунка
                .clip(CircleShape)
                .background(mainColor) // Закруглённые углы ползунка

        )
    }
}

@Preview(showBackground = true)
@Composable
fun AppearancePreview() {
    KotlinCourseWorkTheme {
        val mainColor = colorResource(R.color.light_main_color)
        val secondColor = colorResource(R.color.light_second_color)
        val thirdColor = colorResource(R.color.light_third_color)
        val textColor = colorResource(R.color.light_text_color)
        val navController = rememberNavController()

//        val mainColor = colorResource(R.color.dark_main_color)
//        val secondColor = colorResource(R.color.dark_second_color)
//        val thirdColor = colorResource(R.color.dark_third_color)
//        val textColor = colorResource(R.color.dark_text_color)

        var SwitchValue by remember { mutableStateOf(false) }
        CustomToggleSwitch(
            mainColor,
            secondColor,
            thirdColor,
            textColor,
            SwitchValue,
            {newState -> SwitchValue = newState }
        )

    }
}