package com.example.kotlincoursework

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlincoursework.screens.settings.SettingScreen
import com.example.kotlincoursework.screens.chat.chatScreen
import com.example.kotlincoursework.screens.auth.EnterScreen
import com.example.kotlincoursework.screens.auth.FirstRegisterScreen
import com.example.kotlincoursework.screens.auth.SecondRegisterScreen
import com.example.kotlincoursework.screens.chat.ChatWithUserScreen
import com.example.kotlincoursework.screens.mainColor
import com.example.kotlincoursework.screens.secondColor
import com.example.kotlincoursework.screens.settings.AppearanceScreen
import com.example.kotlincoursework.screens.settings.NotificationScreen
import com.example.kotlincoursework.screens.textColor
import com.example.kotlincoursework.screens.thirdColor
import com.example.kotlincoursework.viewModel.MainScreenViewModel

// Метод навигации
@Composable
fun ScreenNavHost(navController: NavHostController,viewModel: MainScreenViewModel) {
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
                textColor
            )
        }
        composable("ToEnter") {
            EnterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
        composable("ToRegister") {
            FirstRegisterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
        composable("ToSecondRegister") {
            SecondRegisterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
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