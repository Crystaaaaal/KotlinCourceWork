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
    authenticationViewModel: AuthenticationViewModel
) {
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameAppTextWithExtra(
            extraText = "Регистрация"
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(100.dp))
        }
        else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        var colorForRegisterPhoneNumber by remember { mutableStateOf(false) }
        var colorForRegisterLogin by remember { mutableStateOf(false) }
        var colorForRegisterPassword by remember { mutableStateOf(false) }

        val textForRegisterPhoneNumber by authenticationViewModel.textForRegisterPhoneNumber.collectAsState()
        val textForRegisterLogin by authenticationViewModel.textForRegisterLogin.collectAsState()
        val textForRegisterPassword by authenticationViewModel.textForRegisterPassword.collectAsState()

        if (textForRegisterPhoneNumber != "+7" && !authenticationViewModel.isRegisterPhoneNumberValid){
            colorForRegisterPhoneNumber = true
        }
        else{
            colorForRegisterPhoneNumber = false
        }

        if (textForRegisterLogin != "" && !authenticationViewModel.isRegisterLoginValid){
            colorForRegisterLogin = true
        }
        else {
            colorForRegisterLogin = false
        }

        if (textForRegisterPassword != "" && !authenticationViewModel.isRegisterPasswordValid){
            colorForRegisterPassword = true
        }
        else {
            colorForRegisterPassword = false
        }



        RegisterAndAuntificationTextFieldsWithText(
            textForValue = textForRegisterPhoneNumber,
            keyboardType = KeyboardType.Phone,
            onValueChange = { authenticationViewModel.updateTextForRegisterPhoneNumber(it) },
            titleText = "Номер телефона",
            errorStatus = colorForRegisterPhoneNumber
        )


        RegisterAndAuntificationTextFieldsWithText(
            textForValue = textForRegisterLogin,
            onValueChange = { authenticationViewModel.updateTextForRegisterLogin(it) },
            titleText = "Логин",
            errorStatus = colorForRegisterLogin
        )

        RegisterAndAuntificationTextFieldsWithText(
            textForValue = textForRegisterPassword,
            keyboardType = KeyboardType.Password,
            onValueChange = { authenticationViewModel.updateTextForRegisterPassword(it) },
            visualTransformation = PasswordVisualTransformation(),
            titleText = "Пароль",
            errorStatus = colorForRegisterPassword
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(130.dp))
        }
        else {
            Spacer(modifier = Modifier.height(30.dp))
        }

        ButtonThirdColor(
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
            color = color.primary,
            textDecoration = Underline
        )

    }
}

@Preview(showBackground = true)
@Composable
fun firstRegisterPreview() {
    KotlinCourseWorkTheme {
        val navController = rememberNavController()

        //FirstRegisterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}