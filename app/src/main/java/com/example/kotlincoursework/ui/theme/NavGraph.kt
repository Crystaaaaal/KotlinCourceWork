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
import com.example.kotlincoursework.ui.theme.screens.auth.FirstRegisterScreen
import com.example.kotlincoursework.ui.theme.screens.auth.SecondRegisterScreen
import com.example.kotlincoursework.ui.theme.screens.chat.ChatWithUserScreen
import com.example.kotlincoursework.ui.theme.screens.chat.chatScreen
import com.example.kotlincoursework.ui.theme.screens.settings.AppearanceScreen
import com.example.kotlincoursework.ui.theme.screens.settings.NotificationScreen
import com.example.kotlincoursework.ui.theme.screens.settings.SettingScreen
import com.example.kotlincoursework.viewModel.viewModel

// Метод навигации
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    viewModel: viewModel) {
    NavHost(
        navController = navController,
        startDestination = "ToChat"
    ) {
        composable("ToSetting") {
            SettingScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
        composable("ToChat") {
            chatScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor,
                viewModel
            )
        }
        composable("ToEnter") {
            EnterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor,
                viewModel
            )
        }
        composable("ToRegister") {
            FirstRegisterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor,
                viewModel
            )
        }
        composable("ToSecondRegister") {
            SecondRegisterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor,
                viewModel
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