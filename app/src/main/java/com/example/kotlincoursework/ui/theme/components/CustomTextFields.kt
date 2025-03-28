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

@Composable
fun RegisterAndAuntificationTextFieldsWithText(
    textForValue: String,
    onValueChange: (String) -> Unit,
    titleText: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorStatus: Boolean = false
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    // Состояние для управления видимостью пароля
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            text = titleText,
            color = color.onPrimary,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )

        TextField(
            singleLine = true,
            value = textForValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = color.onPrimary,
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
                focusedContainerColor = color.background,
                unfocusedContainerColor = color.background
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
                            tint = color.onPrimary
                        )
                    }
                }
            },
            modifier = Modifier
                .background(color.background)
                .clip(RoundedCornerShape(20.dp))
                .height(50.dp)
                .width(300.dp)
                .fillMaxWidth()
                .border(
                    width = 4.dp,
                    color = if (errorStatus) color.error else color.primary ,
                    shape = RoundedCornerShape(20.dp)
                )

        )
    }
}

@Composable
fun SearchAndInputTextWithPlaceholder(
    textForValue: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    singleline: Boolean,
    modifier: Modifier
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    TextField(
        singleLine = singleline,
        value = textForValue,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = color.onPrimary.copy(alpha = 0.5f), // Полупрозрачный цвет для плейсхолдера
                fontSize = 16.sp
            )
        },

        shape = RoundedCornerShape(30.dp),
        textStyle = androidx.compose.ui.text.TextStyle(color = color.onPrimary, fontSize = 16.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = color.background,
            unfocusedContainerColor = color.background,
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
                        tint = color.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        modifier = modifier
            .background(
                color = color.primary,
                shape = RoundedCornerShape(30.dp)
            )
            .clip(RoundedCornerShape(30.dp))
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
    KotlinCourseWorkTheme {

        var textForPhoneNumber by rememberSaveable { mutableStateOf("") }

        SearchAndInputTextWithPlaceholder(
            textForValue = textForPhoneNumber,
            onValueChange = { textForPhoneNumber = it },
            placeholderText = "Сообщение",
            singleline = true,
            modifier = Modifier
        )

    }
}