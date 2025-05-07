package com.example.kotlincoursework.ui.theme.screens.auth

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.components.NameAppTextWithExtra
import com.example.kotlincoursework.ui.theme.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.components.Toast
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.viewModel.AuthenticationViewModel
import kotlinx.coroutines.delay

@Composable
fun EnterScreen(
    navController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    context: Context
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    var message by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current
    val screenHeightDp = configuration.screenHeightDp.dp

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameAppTextWithExtra(
            extraText = "Вход в аккаунт"
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(100.dp))
        }
        else {
            Spacer(modifier = Modifier.height(25.dp))
        }

        val loginText by authenticationViewModel.loginTextForPhoneNumber.collectAsState()
        RegisterAndAuntificationTextFieldsWithText(
            textForValue = loginText,
            onValueChange = { authenticationViewModel.updateLoginTextForPhoneNumber(it) },
            titleText = "Номер телефона",
            keyboardType = KeyboardType.Phone
        )

        val passwordText by authenticationViewModel.loginTextForPassword.collectAsState()
        RegisterAndAuntificationTextFieldsWithText(
            textForValue = passwordText,
            onValueChange = { authenticationViewModel.updateTextForPassword(it) },
            titleText = "Пароль",
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )

        if (screenHeightDp > 650.dp) {
            Spacer(modifier = Modifier.height(200.dp))
        }
        else {
            Spacer(modifier = Modifier.height(50.dp))
        }
        ButtonThirdColor(
            onClick = {
                if (!authenticationViewModel.isLoginPhoneNumberValid.value || !authenticationViewModel.isLoginPasswordValid.value) {
                    message = "Невалидные данные"
                    showToast = true
                } else {
                    authenticationViewModel.loginUser()
                }
            },
            buttonText = "Войти"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("ToRegister")
                    authenticationViewModel.updateTextForPassword("")
                    authenticationViewModel.updateLoginTextForPhoneNumber("+7")
                },
            text = "Регистрация",
            fontSize = 20.sp,
            color = color.primary,
            textDecoration = Underline
        )
    }


    val state by authenticationViewModel.loginState.collectAsState()
    when (state) {
        is LoginState.Idle -> {}
        is LoginState.Loading -> {}
        is LoginState.Success -> {
            loginIsSucces(navController = navController, context = context)
            authenticationViewModel.resetLoginState()
        }

        is LoginState.Error -> {
            loginIsError(
                message = (state as LoginState.Error).message
            )
        }
    }

    Toast(
        message = message,
        visible = showToast,
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
    message: String
) {
    var showToast by remember { mutableStateOf(true) }
    LoginState.Idle
    Toast(
        message = message,
        visible = showToast,
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

fun loginIsSucces(navController: NavController,context: Context) {
    Log.d("EnterScreen: loginIsSucces", "переход")
    navController.navigate("toChat")
    if (!hasNotificationPermission(context)) {
        requestNotificationPermission(context)
        return
    }
}

private fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        // Для версий ниже Android 13 разрешение не требуется
        true
    }
}

// Запрос разрешения
private fun requestNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (context is Activity) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }
}
