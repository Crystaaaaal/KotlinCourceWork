package com.example.kotlincoursework.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ShowMessage
import com.example.kotlincoursework.ui.theme.components.InputMessageTextField
import com.example.kotlincoursework.ui.theme.components.SearchTextFieldWithPlaceholder
import com.example.kotlincoursework.viewModel.AuthenticationViewModel
import com.example.kotlincoursework.viewModel.SearchViewModel
import com.example.kotlincoursework.viewModel.SettingsViewModel
import com.example.kotlincoursework.viewModel.ThemeViewModel
import com.example.kotlincoursework.viewModel.viewModel


@Composable
fun BarDrawing(
    navController: NavHostController,
    viewModel: viewModel,
    authenticationViewModel: AuthenticationViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    themeViewModel: ThemeViewModel
) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val color = androidx.compose.material3.MaterialTheme.colorScheme

    Scaffold(

        topBar = {
            when (currentRoute) {
                "ToEnter",
                "ToRegister",
                "ToSecondRegister" -> {
                }

                "ToNotification" -> {
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

                "ToChat", "ToSearchScreen" -> {
                    viewModel.updateTopBarText("Мессенджер")
                    ChatTopBar(
                        navController = navController,
                        viewModel = viewModel,
                        searchViewModel = searchViewModel
                    )
                }

                "ToUserChat" -> {
                    viewModel.updateTopBarText("Антон Скугарев")
                    ChatWithUserTopBar(viewModel, navController)
                }
            }
        },

        bottomBar = {
            when (currentRoute) {
                "ToEnter",
                "ToRegister",
                "ToSecondRegister",
                "ToSearchScreen" -> {
                }

                "ToUserChat" -> {
                    ChatWithUserBottomBar(viewModel)
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
                        statusBarColor = color.background.toArgb(), // Цвет статус-бара
                        navigationBarColor = color.background.toArgb(), // Цвет навигационной панели
                        lightStatusBar = true, // Светлый текст на статус-баре
                        lightNavigationBar = true // Светлый текст на навигационной панели
                    )
                }

                else -> {
                    SetSystemBarsColor(
                        statusBarColor = color.primary.toArgb(), // Цвет статус-бара
                        navigationBarColor = color.primary.toArgb(), // Цвет навигационной панели
                        lightStatusBar = true, // Светлый текст на статус-баре
                        lightNavigationBar = true // Светлый текст на навигационной панели
                    )
                }
            }
            ScreenMainContent(
                navController = navController,
                paddingValues = paddingValues,
                viewModel = viewModel,
                authenticationViewModel = authenticationViewModel,
                searchViewModel = searchViewModel,
                settingsViewModel = settingsViewModel,
                themeViewModel = themeViewModel
            )
        }
    )
}

//метод создания topBar для настроек
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    viewModel: viewModel,
    navController: NavController
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    TopAppBar(
        modifier = Modifier.height(60.dp),
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Выравниваем текст по центру
            ) {
                Text(
                    text = viewModel.topBarText,
                    textAlign = TextAlign.Center,
                    color = color.onPrimary,
                    modifier = Modifier.offset(x = (-24).dp) // Компенсируем смещение от кнопки
                )
            }
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.size(50.dp, 30.dp),
                onClick = { navController.popBackStack() }
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = color.onPrimary,
                        modifier = Modifier.background(color.secondary)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = color.primary
        )
    )
}

//метод создания topBar для чата с пользователем
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatWithUserTopBar(
    viewModel: viewModel,
    navController: NavController
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    TopAppBar(
        modifier = Modifier.height(60.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Кнопка "Назад" (слева)
                IconButton(
                    modifier = Modifier.size(50.dp, 30.dp),
                    onClick = { navController.popBackStack() }
                ) {
                    Surface(
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад",
                            tint = color.onPrimary,
                            modifier = Modifier.background(color.secondary)
                        )
                    }
                }

                // Текст (по центру)
                Text(
                    text = viewModel.topBarText,
                    textAlign = TextAlign.Center,
                    color = color.onPrimary,
                    modifier = Modifier
                        .weight(1f) // Занимает все доступное пространство между кнопкой и аватаром
                        .padding(horizontal = 8.dp) // Добавляем отступы, чтобы текст не прилипал к краям
                )

                // Аватар пользователя (справа)
                Box(
                    modifier = Modifier
                        .padding(end = 20.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color.secondary)
                        .border(
                            width = 2.dp,
                            color = color.secondary,
                            shape = RoundedCornerShape(1.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp),
                        painter = painterResource(id = R.drawable.picture),
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = color.primary
        )
    )
}

//метод создания bottomBar для чата с пользователем
@Composable
fun ChatWithUserBottomBar(
    viewModel: viewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    BottomAppBar(
        modifier = Modifier
            .heightIn(min = 60.dp, max = 600.dp)
            .wrapContentHeight(),
        containerColor = color.primary
    ) {
        Row(
            modifier =
            Modifier
                .padding(vertical = 10.dp)
                .heightIn(min = 60.dp, max = 600.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(modifier = Modifier
                .padding(start = 10.dp)
                .size(50.dp),
                onClick = {}) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(80.dp)
                        .width(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = color.onPrimary,
                        modifier = Modifier
                            .size(3.dp)
                            .background(color.secondary)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))

            var textForMessage by rememberSaveable { mutableStateOf("") }
            InputMessageTextField(
                textForValue = textForMessage,
                onValueChange = { textForMessage = it },
                placeholderText = "Сообщение",
                singleline = false,
                modifier = Modifier
                    .width(225.dp)
                    .wrapContentHeight()
                    .heightIn(min = 50.dp, max = 590.dp)
                //  .widthIn(min= 200.dp,max = 250.dp)

            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(modifier = Modifier
                .padding(end = 10.dp)
                .size(50.dp),
                onClick = {
                    ShowMessage(viewModel, textForMessage)
                    textForMessage = ""
                }
            ) {
                Surface(
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .height(80.dp)
                        .width(300.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Отправить",
                        tint = color.onPrimary,
                        modifier = Modifier
                            .size(3.dp)
                            .background(color.secondary)
                    )
                }
            }
        }

    }
}


@Composable
fun ChatTopBar(
    navController: NavController,
    viewModel: viewModel,
    searchViewModel: SearchViewModel
) {
    val color = MaterialTheme.colorScheme
    var userSearching by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        color = color.primary,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (!userSearching) {
                Text(
                    text = viewModel.topBarText,
                    textAlign = TextAlign.Center,
                    color = color.onPrimary,
                    fontSize = 20.sp
                )
            }
            else{
                Spacer(modifier = Modifier.height(5.dp))
            }
            val text by searchViewModel.textForSearch.collectAsState()

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchTextFieldWithPlaceholder(
                    textForValue = text,
                    onValueChange = {
                        searchViewModel.updateTextForSearch(it)
                    },
                    placeholderText = "Поиск",
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                userSearching = true
                                navController.navigate("ToSearchScreen")
                            }
                        }
                )
                if (userSearching) {
                    Text(
                        text = "Назад",
                        textAlign = TextAlign.Center,
                        color = color.onPrimary,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                userSearching = false
                                focusManager.clearFocus() // Сбрасываем фокус
                                keyboardController?.hide() // Закрываем клавиатуру
                                navController.navigate("ToChat")
                                searchViewModel.resetSearchState()
                                searchViewModel.updateTextForSearch("")
                            })
                }
            }
        }
    }
}

// Метод для создания TopBar для основного экрана
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTopBar(
    viewModel: viewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    CenterAlignedTopAppBar(
        modifier = Modifier.height(60.dp),
        title = {
            Text(
                fontSize = 20.sp,
                text = viewModel.topBarText,
                textAlign = TextAlign.Center,
                color = color.onPrimary
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = color.primary
        )
    )
}


// Метод для создания BottomBar для основного экрана
@Composable
fun ScreenBottomBar(
    navController: NavController,
    viewModel: viewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    BottomAppBar(
        modifier = Modifier.height(60.dp),
        containerColor = color.primary
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
                        tint = color.onPrimary,
                        modifier = Modifier
                            .size(3.dp)
                            .background(color.secondary)
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
                        tint = color.onPrimary,
                        modifier = Modifier.background(color.secondary)
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
    paddingValues: PaddingValues,
    viewModel: viewModel,
    authenticationViewModel: AuthenticationViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    themeViewModel: ThemeViewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.background)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        ScreenNavHost(
            navController = navController,
            viewModel = viewModel,
            authenticationViewModel = authenticationViewModel,
            searchViewModel = searchViewModel,
            settingsViewModel = settingsViewModel,
            themeViewModel = themeViewModel
        )
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

@Preview(showBackground = true)
@Composable
fun barPreview() {
    KotlinCourseWorkTheme {
        val navController = rememberNavController()

        //val viewModel: viewModel = viewModel()
        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        //ChatWithUserBottomBar(viewModel)
    }
}
