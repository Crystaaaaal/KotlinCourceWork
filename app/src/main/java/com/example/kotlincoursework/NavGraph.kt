package com.example.kotlincoursework

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlincoursework.screens.SettingScreen
import com.example.kotlincoursework.screens.chatScreen
import com.example.kotlincoursework.screens.enterScreen
import com.example.kotlincoursework.screens.mainColor
import com.example.kotlincoursework.screens.secondColor
import com.example.kotlincoursework.screens.textColor
import com.example.kotlincoursework.screens.thirdColor

// Метод навигации
@Composable
fun ScreenNavHost(navController: NavHostController) {
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
            enterScreen(
                navController,
                mainColor,
                secondColor,
                thirdColor,
                textColor
            )
        }
    }

}