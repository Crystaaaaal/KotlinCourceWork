package com.example.kotlincoursework.ui.theme.screens.auth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.components.NameAppTextWithExtra
import com.example.kotlincoursework.ui.theme.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.components.Toast
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.viewModel.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach

@Composable
fun EnterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    var message by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }

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
                if (!viewModel.isLoginPhoneNumberValid.value || !viewModel.isLoginPasswordValid.value) {
                    message = "Невалидные данные"
                    showToast = true
                }
                else {
                    viewModel.loginUser()
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


    val state by viewModel.loginState.collectAsState()
        when(state){
            is LoginState.Idle -> {}
            is LoginState.Loading ->{}
            is LoginState.Success -> {
                loginIsSucces(navController =  navController)
                viewModel.resetLoginState()}

            is LoginState.Error -> {
                loginIsError(
                    mainColor = mainColor,
                    secondColor = secondColor,
                    textColor = textColor,
                    message = (state as LoginState.Error).message
                )
            }
        }

    Toast(
        message = message,
        visible = showToast,
        mainColor = mainColor,
        secondColor = secondColor,
        textColor = textColor
    )
    // Управление Toast
    LaunchedEffect(showToast) {
        if (showToast) {
            Log.d("LaunchedEffect", "Toast показан: $message")
            delay(3000) // Показываем Toast в течение 3 секунд
            showToast = false // Скрываем Toast
            Log.d("LaunchedEffect", "Toast скрыт")
        }
    }

}
@Composable
fun loginIsError(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    message:String
){
    var showToast by remember { mutableStateOf(true) }
    LoginState.Idle
    Toast(
        message = message,
        visible = showToast,
        mainColor = mainColor,
        secondColor = secondColor,
        textColor = textColor
    )

    // Управление Toast
    LaunchedEffect(showToast) {
        if (showToast) {
            Log.d("LaunchedEffect", "Toast показан: $message")
            delay(3000) // Показываем Toast в течение 3 секунд
            showToast = false // Скрываем Toast
            Log.d("LaunchedEffect", "Toast скрыт")
        }
    }
}
fun loginIsSucces(navController: NavController){
    Log.d("EnterScreen: loginIsSucces","переход")
    navController.navigate("toChat")



}
