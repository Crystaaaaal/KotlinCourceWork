package com.example.kotlincoursework.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

//@Composable
//fun RegisterAndAuntificationTextFieldsWithText(
//    mainColor: Color,
//    secondColor: Color,
//    textColor: Color,
//    textForValue: String,
//    onValueChange: (String) -> Unit,
//    titleText: String,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    visualTransformation: VisualTransformation = VisualTransformation.None
//) {
//    Column() {
//        Text(
//            modifier = Modifier
//                .padding(horizontal = 10.dp, vertical = 5.dp),
//            text = titleText,
//            color = textColor,
//            fontSize = 18.sp,
//            textAlign = TextAlign.Start
//        )
//
//        TextField(
//            singleLine = true,
//            value = textForValue,
//            onValueChange = onValueChange,
//            shape = RoundedCornerShape(20.dp),
//            textStyle = androidx.compose.ui.text.TextStyle(
//                color = textColor,
//                textAlign = TextAlign.Start
//            ),
//            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
//            visualTransformation = visualTransformation,
//            colors = TextFieldDefaults.colors
//                (
//                focusedContainerColor = mainColor,
//                unfocusedContainerColor = mainColor
//            ),
//            modifier = Modifier
//                .background(mainColor)
//                .clip(RoundedCornerShape(20.dp))
//                .height(50.dp)
//                .width(300.dp)
//                .fillMaxWidth()
//                .border
//                    (
//                    width = 4.dp,
//                    color = secondColor,
//                    shape = RoundedCornerShape(20.dp)
//                )
//        )
//    }
//}

@Composable
fun RegisterAndAuntificationTextFieldsWithText(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    textForValue: String,
    onValueChange: (String) -> Unit,
    titleText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    // Состояние для управления видимостью пароля
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            text = titleText,
            color = textColor,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )

        TextField(
            singleLine = true,
            value = textForValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor,
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),

            visualTransformation = if (isPasswordVisible && visualTransformation is PasswordVisualTransformation) {
                VisualTransformation.None // Показываем текст, если пароль видим
            } else {
                visualTransformation // Используем переданное значение (например, PasswordVisualTransformation)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = mainColor,
                unfocusedContainerColor = mainColor
            ),
            trailingIcon = {
                // Показываем кнопку только если visualTransformation = PasswordVisualTransformation
                if (visualTransformation is PasswordVisualTransformation) {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible // Переключаем состояние
                        }
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) {
                                Icons.Default.Visibility // Иконка "глаз открыт"
                            } else {
                                Icons.Default.VisibilityOff // Иконка "глаз закрыт"
                            },
                            contentDescription = if (isPasswordVisible) {
                                "Скрыть пароль"
                            } else {
                                "Показать пароль"
                            },
                            tint = textColor
                        )
                    }
                }
            },
            modifier = Modifier
                .background(mainColor)
                .clip(RoundedCornerShape(20.dp))
                .height(50.dp)
                .width(300.dp)
                .fillMaxWidth()
                .border(
                    width = 4.dp,
                    color = secondColor,
                    shape = RoundedCornerShape(20.dp)
                )
        )
    }
}

@Composable
fun SearchAndInputTextWithPlaceholder(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    textForValue: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    singleline: Boolean,
    modifier: Modifier
) {
    TextField(
        singleLine = singleline,
        value = textForValue,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = textColor.copy(alpha = 0.5f), // Полупрозрачный цвет для плейсхолдера
                fontSize = 16.sp
            )
        },
        shape = RoundedCornerShape(30.dp),
        textStyle = androidx.compose.ui.text.TextStyle(color = textColor, fontSize = 16.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = mainColor,
            unfocusedContainerColor = mainColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        trailingIcon = {
            if (textForValue.isNotEmpty()) {
                IconButton(
                    onClick = { onValueChange("") } // Очищаем текст
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, // Иконка очистки
                        contentDescription = "Очистить",
                        tint = textColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = modifier
            .background(
                secondColor,
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp))
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
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

        var textForPhoneNumber by rememberSaveable { mutableStateOf("") }
//        RegisterAndAuntificationTextFieldsWithText(
//            mainColor = mainColor,
//            secondColor = secondColor,
//            textColor = textColor,
//            textForValue = textForPhoneNumber,
//            onValueChange = { textForPhoneNumber = it },
//            titleText = "Номер телефона"
//        )

        SearchAndInputTextWithPlaceholder(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForPhoneNumber,
            onValueChange = { textForPhoneNumber = it },
            placeholderText = "Сообщение",
            singleline = true,
            modifier = Modifier
        )

    }
}