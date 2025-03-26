package com.example.kotlincoursework.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlincoursework.mainColor
import com.example.kotlincoursework.secondColor
import com.example.kotlincoursework.textColor
import com.example.kotlincoursework.thirdColor
import com.example.kotlincoursework.ui.theme.screens.auth.EnterScreen
import com.example.kotlincoursework.ui.theme.screens.auth.FirstRegistrationScreen
import com.example.kotlincoursework.ui.theme.screens.auth.SecondRegistrationScreen
import com.example.kotlincoursework.ui.theme.screens.chat.ChatWithUserScreen
import com.example.kotlincoursework.ui.theme.screens.chat.chatScreen
import com.example.kotlincoursework.ui.theme.screens.settings.AppearanceScreen
import com.example.kotlincoursework.ui.theme.screens.settings.NotificationScreen
import com.example.kotlincoursework.ui.theme.screens.settings.SettingScreen
import com.example.kotlincoursework.viewModel.AuthenticationViewModel
import com.example.kotlincoursework.viewModel.ChatViewModel
import com.example.kotlincoursework.viewModel.SettingsViewModel
import com.example.kotlincoursework.viewModel.viewModel

// Метод навигации
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    viewModel: viewModel,
    authenticationViewModel: AuthenticationViewModel,
    chatViewModel: ChatViewModel,
    settingsViewModel: SettingsViewModel) {
    NavHost(
        navController = navController,
        startDestination = "ToChat"
    ) {
        composable("ToSetting") {
            SettingScreen(
                navController = navController,
                mainColor = mainColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                textColor = textColor,
                settingsViewModel = settingsViewModel
            )
        }
        composable("ToChat") {
            chatScreen(
                navController = navController,
                mainColor = mainColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                textColor = textColor,
                chatViewModel = chatViewModel
            )
        }
        composable("ToEnter") {
            EnterScreen(
                navController = navController,
                mainColor = mainColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                textColor = textColor,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable("ToRegister") {
            FirstRegistrationScreen(
                navController = navController,
                mainColor = mainColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                textColor = textColor,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable("ToSecondRegister") {
            SecondRegistrationScreen(
                navController = navController,
                mainColor = mainColor,
                secondColor = secondColor,
                thirdColor = thirdColor,
                textColor = textColor,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable("ToAppearance") {
            AppearanceScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
        composable("ToNotification") {
            NotificationScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
        composable("ToUserChat") {
            ChatWithUserScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor,
                viewModel
            )
        }
    }

}