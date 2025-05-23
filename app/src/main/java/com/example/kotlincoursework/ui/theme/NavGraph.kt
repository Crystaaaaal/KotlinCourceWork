package com.example.kotlincoursework.ui.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kotlincoursework.ui.theme.screens.auth.EnterScreen
import com.example.kotlincoursework.ui.theme.screens.auth.FirstRegistrationScreen
import com.example.kotlincoursework.ui.theme.screens.auth.SecondRegistrationScreen
import com.example.kotlincoursework.ui.theme.screens.chat.ChatWithUserScreen
import com.example.kotlincoursework.ui.theme.screens.chat.SearchScreen
import com.example.kotlincoursework.ui.theme.screens.chat.chatScreen
import com.example.kotlincoursework.ui.theme.screens.settings.AppearanceScreen
import com.example.kotlincoursework.ui.theme.screens.settings.NotificationScreen
import com.example.kotlincoursework.ui.theme.screens.settings.SettingScreen
import com.example.kotlincoursework.viewModel.AuthenticationViewModel
import com.example.kotlincoursework.viewModel.SearchViewModel
import com.example.kotlincoursework.viewModel.SettingsViewModel
import com.example.kotlincoursework.viewModel.ThemeViewModel
import com.example.kotlincoursework.viewModel.viewModel

// Метод навигации
@Composable
fun ScreenNavHost(
    navController: NavHostController,
    viewModel: viewModel,
    authenticationViewModel: AuthenticationViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    themeViewModel: ThemeViewModel,
    context: Context,
    startScreen:String
) {

    NavHost(
        navController = navController,
        startDestination = startScreen
    ) {
        composable("ToSetting") {
            settingsViewModel.getUserInfo()
            SettingScreen(
                navController = navController,
                settingsViewModel = settingsViewModel,
                context = context
            )
        }
        composable("ToChat") {
            chatScreen(
                navController = navController,
                searchViewModel = searchViewModel,
                viewModel = viewModel
            )
        }
        composable("ToEnter") {
            EnterScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel,
                context = context,
                viewModel = viewModel
            )
        }
        composable("ToRegister") {
            FirstRegistrationScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable("ToSecondRegister") {
            SecondRegistrationScreen(
                navController = navController,
                authenticationViewModel = authenticationViewModel
            )
        }
        composable("ToAppearance") {
            AppearanceScreen(
                navController = navController,
                themeViewModel = themeViewModel
            )
        }
        composable("ToNotification") {
            NotificationScreen(
                navController = navController
            )
        }
        composable("ToUserChat") {
            ChatWithUserScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable("ToSearchScreen") {
            SearchScreen(
                viewModel = viewModel,
                navController = navController,
                searchViewModel = searchViewModel
            )
        }
    }

}