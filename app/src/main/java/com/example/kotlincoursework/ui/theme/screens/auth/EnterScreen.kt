package com.example.kotlincoursework.ui.theme.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.components.NameAppTextWithExtra
import com.example.kotlincoursework.ui.theme.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.viewModel.viewModel

@Composable
fun EnterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
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


        val loginText by viewModel.loginTextForPhoneNumber.collectAsState()
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = loginText,
            onValueChange = { viewModel.updateLoginTextForPhoneNumber(it) },
            titleText = "Номер телефона",
            keyboardType = KeyboardType.Phone

        )

        val passwordText by viewModel.loginTextForPassword.collectAsState()
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = passwordText,
            onValueChange = { viewModel.updateTextForPassword(it) },
            titleText = "Пароль",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()

        )

        Spacer(modifier = Modifier.height(200.dp))

        ButtonThirdColor(
            thirdColor = thirdColor,
            textColor = textColor,
            onClick = {
                if (viewModel.loginUser()) {
                    navController.navigate("ToChat")
                    viewModel.updateTextForPassword("")
                    viewModel.updateLoginTextForPhoneNumber("+7")
                }
            },
            buttonText = "Войти"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("ToRegister")
                    viewModel.updateTextForPassword("")
                    viewModel.updateLoginTextForPhoneNumber("+7")
                },
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


        //EnterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}