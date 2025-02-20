package com.example.kotlincoursework

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlincoursework.screens.SettingScreen
import com.example.kotlincoursework.screens.chatScreen
import com.example.kotlincoursework.screens.EnterScreen
import com.example.kotlincoursework.screens.FirstRegisterScreen
import com.example.kotlincoursework.screens.SecondRegisterScreen
import com.example.kotlincoursework.screens.firstRegisterPreview
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
    }

}