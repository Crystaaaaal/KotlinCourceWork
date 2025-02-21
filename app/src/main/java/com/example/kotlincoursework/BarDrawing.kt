package com.example.kotlincoursework

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.kotlincoursework.screens.mainColor
import com.example.kotlincoursework.screens.secondColor
import com.example.kotlincoursework.screens.textColor
import com.example.kotlincoursework.screens.thirdColor
import com.example.kotlincoursework.viewModel.MainScreenViewModel


@Composable
fun BarDrawing(navController: NavHostController, viewModel: MainScreenViewModel) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(

        topBar = {
            when (currentRoute) {
                "ToEnter",
                "ToRegister",
                "ToSecondRegister" -> {
                }
                "ToNotification"->{
                    viewModel.updateTopBarText("Уведомления")
                    SettingsTopBar(viewModel, navController)
                }

                "ToAppearance" -> {
                    viewModel.updateTopBarText("Оформление")
                    SettingsTopBar(viewModel, navController)
                }

                "ToSetting" -> {
                    viewModel.updateTopBarText("Настройки")
                    ScreenTopBar(viewModel)
                }

                "ToChat" -> {
                    viewModel.updateTopBarText("Чат")
                    ScreenTopBar(viewModel)
                }
            }
        },

        bottomBar = {
            when (currentRoute) {
                "ToEnter",
                "ToRegister",
                "ToSecondRegister" -> {
                }

                else -> ScreenBottomBar(navController, viewModel)
            }
        },

        content = { paddingValues ->
            when (currentRoute) {
                "ToEnter",
                "ToRegister",
                "ToSecondRegister" -> {
                    SetSystemBarsColor(
                        statusBarColor = mainColor.toArgb(), // Цвет статус-бара
                        navigationBarColor = mainColor.toArgb(), // Цвет навигационной панели
                        lightStatusBar = true, // Светлый текст на статус-баре
                        lightNavigationBar = true // Светлый текст на навигационной панели
                    )
                }

                else -> {
                    SetSystemBarsColor(
                        statusBarColor = secondColor.toArgb(), // Цвет статус-бара
                        navigationBarColor = secondColor.toArgb(), // Цвет навигационной панели
                        lightStatusBar = true, // Светлый текст на статус-баре
                        lightNavigationBar = true // Светлый текст на навигационной панели
                    )
                }
            }
            ScreenMainContent(navController, paddingValues)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    viewModel: MainScreenViewModel,
    navController: NavController
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Выравниваем текст по центру
            ) {
                Text(
                    text = viewModel.topBarText,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier.offset(x = (-24).dp) // Компенсируем смещение от кнопки
                )
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.size(50.dp, 30.dp),
                onClick = {navController.popBackStack()}
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = textColor,
                        modifier = Modifier.background(thirdColor)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = secondColor
        )
    )
}
// Метод для создания TopBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    viewModel: MainScreenViewModel
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.height(40.dp),
        title = {
            Text(
                text = viewModel.topBarText,
                textAlign = TextAlign.Center,
                color = textColor
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = secondColor
        )
    )
}


// Метод для создания BottomBar
@Composable
fun ScreenBottomBar(
    navController: NavController,
    viewModel: MainScreenViewModel
) {
    BottomAppBar(
        modifier = Modifier.height(40.dp),
        containerColor = secondColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier.size(100.dp, 30.dp),
                onClick = {
                    navController.navigate("ToChat");
                },
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.MailOutline,
                        contentDescription = "Чат",
                        tint = textColor,
                        modifier = Modifier
                            .size(3.dp)
                            .background(thirdColor)
                    )
                }
            }
            Spacer(modifier = Modifier.width(120.dp))
            IconButton(
                modifier = Modifier.size(100.dp, 30.dp),
                onClick = {
                    navController.navigate("ToSetting")

                }
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Настройки",
                        tint = textColor,
                        modifier = Modifier.background(thirdColor)
                    )
                }
            }
        }
    }
}

// Метод для создания основной части экрана
@Composable
fun ScreenMainContent(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        ScreenNavHost(navController)
    }
}


@Composable
fun SetSystemBarsColor(
    statusBarColor: Int,
    navigationBarColor: Int,
    lightStatusBar: Boolean = false,
    lightNavigationBar: Boolean = false
) {
    val context = LocalContext.current
    val activity = context as Activity

    DisposableEffect(Unit) {
        val window = activity.window

        // Устанавливаем цвета
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navigationBarColor
        }

        // Управляем светлым/темным текстом
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
            windowInsetsController.isAppearanceLightStatusBars = lightStatusBar
            windowInsetsController.isAppearanceLightNavigationBars = lightNavigationBar
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var systemUiVisibility = window.decorView.systemUiVisibility
            if (lightStatusBar) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                systemUiVisibility =
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && lightNavigationBar) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUiVisibility =
                    systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }

        onDispose {
        }
    }
}
