package com.example.kotlincoursework.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.components.ButtonThirdColor
import com.example.kotlincoursework.components.NameAppTextWithExtra
import com.example.kotlincoursework.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun EnterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameAppTextWithExtra(
            secondColor = secondColor,
            thirdColor = thirdColor,
            extraText = "Вход в аккаунт"
        )

        Spacer(modifier = Modifier.height(100.dp))


        var textForPhoneNumber by rememberSaveable { mutableStateOf("") }
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForPhoneNumber,
            onValueChange = { textForPhoneNumber = it },
            titleText = "Номер телефона"

        )

        var textForPassword by rememberSaveable { mutableStateOf("") }
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForPassword,
            onValueChange = { textForPassword = it },
            titleText = "Пароль"

        )

        Spacer(modifier = Modifier.height(200.dp))

        ButtonThirdColor(
            thirdColor = thirdColor,
            textColor = textColor,
            navController = navController,
            navControllerRoute = "ToChat",
            buttonText = "Войти"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .clickable {navController.navigate("ToRegister")},
            text = "Регистрация",
            fontSize = 20.sp,
            color = secondColor,
            textDecoration = Underline
        )

    }
}


@Preview(showBackground = true)
@Composable
fun EnterPreview() {
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


        EnterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}