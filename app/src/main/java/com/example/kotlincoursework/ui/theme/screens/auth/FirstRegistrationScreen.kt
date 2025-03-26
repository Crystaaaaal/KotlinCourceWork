package com.example.kotlincoursework.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.kotlincoursework.viewModel.AuthenticationViewModel

@Composable
fun FirstRegistrationScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    authenticationViewModel: AuthenticationViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameAppTextWithExtra(
            secondColor = secondColor,
            thirdColor = thirdColor,
            extraText = "Регистрация"
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(100.dp))
        }
        else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        var colorForRegisterPhoneNumber by remember { mutableStateOf(secondColor) }
        var colorForRegisterLogin by remember { mutableStateOf(secondColor) }
        var colorForRegisterPassword by remember { mutableStateOf(secondColor) }

        val textForRegisterPhoneNumber by authenticationViewModel.textForRegisterPhoneNumber.collectAsState()
        val textForRegisterLogin by authenticationViewModel.textForRegisterLogin.collectAsState()
        val textForRegisterPassword by authenticationViewModel.textForRegisterPassword.collectAsState()

        if (textForRegisterPhoneNumber != "+7" && !authenticationViewModel.isRegisterPhoneNumberValid){
            colorForRegisterPhoneNumber = Color.Red
        }
        else{
            colorForRegisterPhoneNumber = secondColor
        }

        if (textForRegisterLogin != "" && !authenticationViewModel.isRegisterLoginValid){
            colorForRegisterLogin = Color.Red
        }
        else {
            colorForRegisterLogin = secondColor
        }

        if (textForRegisterPassword != "" && !authenticationViewModel.isRegisterPasswordValid){
            colorForRegisterPassword = Color.Red
        }
        else {
            colorForRegisterPassword = secondColor
        }



        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterPhoneNumber,
            textColor = textColor,
            textForValue = textForRegisterPhoneNumber,
            keyboardType = KeyboardType.Phone,
            onValueChange = { authenticationViewModel.updateTextForRegisterPhoneNumber(it) },
            titleText = "Номер телефона"
        )


        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterLogin,
            textColor = textColor,
            textForValue = textForRegisterLogin,
            onValueChange = { authenticationViewModel.updateTextForRegisterLogin(it) },
            titleText = "Логин",
        )

        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterPassword,
            textColor = textColor,
            textForValue = textForRegisterPassword,
            keyboardType = KeyboardType.Password,
            onValueChange = { authenticationViewModel.updateTextForRegisterPassword(it) },
            visualTransformation = PasswordVisualTransformation(),
            titleText = "Пароль"
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(130.dp))
        }
        else {
            Spacer(modifier = Modifier.height(30.dp))
        }

        ButtonThirdColor(
            thirdColor = thirdColor,
            textColor = textColor,
            buttonText = "Далее",
            onClick = {
                navController.navigate("ToSecondRegister")
            }

        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("ToEnter")
                },
            text = "Назад",
            fontSize = 20.sp,
            color = secondColor,
            textDecoration = Underline
        )

    }
}

@Preview(showBackground = true)
@Composable
fun firstRegisterPreview() {
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


        //FirstRegisterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}