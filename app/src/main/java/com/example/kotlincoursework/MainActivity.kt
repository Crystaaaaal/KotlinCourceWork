package com.example.kotlincoursework


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel


var mainColor: Color = Color.Black
var secondColor: Color = Color.Black
var thirdColor: Color = Color.Black
var textColor: Color = Color.Black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinCourseWorkTheme {
                AppStart()
            }
        }
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
                systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && lightNavigationBar) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            window.decorView.systemUiVisibility = systemUiVisibility
        }

        onDispose {
        }
    }
}

// Метод начала отрисовки приложения
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppStart() {
    mainColor = colorResource(R.color.light_main_color)
    secondColor = colorResource(R.color.light_second_color)
    thirdColor = colorResource(R.color.light_third_color)
    textColor = colorResource(R.color.light_text_color)

        // mainColor = colorResource(R.color.dark_main_color)
    //secondColor = colorResource(R.color.dark_second_color)
    //thirdColor = colorResource(R.color.dark_third_color)
    //textColor = colorResource(R.color.dark_text_color)


    val viewModel: MainScreenViewModel = viewModel()

    val navController = rememberNavController()
    SetSystemBarsColor(
        statusBarColor = secondColor.toArgb(), // Цвет статус-бара
        navigationBarColor = secondColor.toArgb(), // Цвет навигационной панели
        lightStatusBar = true, // Светлый текст на статус-баре
        lightNavigationBar = true // Светлый текст на навигационной панели
    )
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    Scaffold(
        topBar = { MainScreenTopBar(navController,viewModel) },
        bottomBar = { MainScreenBottomBar(navController,viewModel)},
        content = { paddingValues ->
            MainScreenMainContent(navController,paddingValues)}
    )
}

// Метод навигации
@Composable
fun MainScreenNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "ToChat"
    ) {
        composable("ToSetting") {
            settingScreen(
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

// Метод для создания TopBar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    navController: NavHostController,
    viewModel: MainScreenViewModel
) {

    val currentRoute = navController.currentBackStackEntry?.destination?.route
    when (currentRoute) {
        "ToEnter" -> {}
        else->
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
}


// Метод для создания BottomBar
@Composable
fun MainScreenBottomBar(
    navController: NavController,
    viewModel: MainScreenViewModel) {
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
                    viewModel.updateTopBarText("Мессенджер")
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
                    viewModel.updateTopBarText("Настройки")}
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Настройки",
                        tint = textColor,
                        modifier = Modifier.background(thirdColor))
                }
            }
        }
    }
}

// Метод для создания основной части экрана
@Composable
fun MainScreenMainContent(
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
        MainScreenNavHost(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KotlinCourseWorkTheme {
        AppStart()
    }
}