package com.example.kotlincoursework


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.Di.databaseModule
import com.example.kotlincoursework.Di.viewModelModule
import com.example.kotlincoursework.ui.theme.BarDrawing
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.viewModel.AuthenticationViewModel
import com.example.kotlincoursework.viewModel.SearchViewModel
import com.example.kotlincoursework.viewModel.SettingsViewModel
import com.example.kotlincoursework.viewModel.ThemeViewModel
import com.example.kotlincoursework.viewModel.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val themeViewModel = ThemeViewModel()

            createNotificationChannel()

            val applicatonContext = applicationContext

            startKoin {
                androidContext(applicatonContext)
                modules(
                    databaseModule,
                    viewModelModule
                )
            }

            val viewModel: viewModel = koinViewModel()

            val dbHelper = DatabaseHelper(applicatonContext)
            val db = dbHelper.writableDatabase

            val navController = rememberNavController()
            val masterKey =
                MasterKey.Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)

                    .build()

            val sharedPreferences = EncryptedSharedPreferences.create(
                applicationContext,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )

            val themId = sharedPreferences.getString("theme", null)
            if (themId == null) {
                Log.i("theme", "не вышло")
                themeViewModel.setTheme("light")
            } else {
                Log.i("theme", themId)
                themeViewModel.setTheme(themId)
            }

//            val viewModel = viewModel(
//                applicationContext = applicatonContext,
//                sharedPreferences = sharedPreferences,
//                db = db,
//                dbHelper = dbHelper
//            )
            val authenticationViewModel = AuthenticationViewModel(
                sharedPreferences = sharedPreferences,
                applicationContext = applicatonContext,
                db = db
            )

            val searchViewModel = SearchViewModel(
                applicationContext = applicatonContext,
                sharedPreferences = sharedPreferences,
                db = db
            )
            val settingsViewModel = SettingsViewModel(
                applicationContext = applicatonContext,
                sharedPreferences = sharedPreferences
            )



            val phoneNumber = sharedPreferences.getString("auth_phone", null)
            val token = sharedPreferences.getString("auth_token", null)
            val startScreen: String
            if (!phoneNumber.isNullOrEmpty() && !token.isNullOrEmpty()) {
                ApiClient.startWebSocket(phoneNumber = phoneNumber!!,viewModel, context = applicatonContext)
                startScreen = "ToChat"
            }
            else{
                startScreen = "ToEnter"
            }




            app(
                themeViewModel = themeViewModel,
                navController = navController,
                authenticationViewModel = authenticationViewModel,
                searchViewModel = searchViewModel,
                settingsViewModel = settingsViewModel,
                viewModel = viewModel,
                sharedPreferences = sharedPreferences,
                context = applicatonContext,
                startScreen = startScreen
            )
        }
    }

    @Composable
    fun app(
        navController: NavHostController,
        themeViewModel: ThemeViewModel,
        authenticationViewModel: AuthenticationViewModel,
        searchViewModel: SearchViewModel,
        settingsViewModel: SettingsViewModel,
        viewModel: viewModel,
        sharedPreferences: SharedPreferences,
        context: Context,
        startScreen: String
    ) {
        val currentTheme by themeViewModel.currentTheme.collectAsState()

        // Сохраняем тему при изменении
        LaunchedEffect(currentTheme.id) {
            sharedPreferences.edit {
                putString("theme", currentTheme.id)
                apply() // или commit()
            }
        }

        KotlinCourseWorkTheme(theme = currentTheme) {
            //Отрисовываем панелей
            BarDrawing(
                navController = navController,
                viewModel = viewModel,
                authenticationViewModel = authenticationViewModel,
                searchViewModel = searchViewModel,
                settingsViewModel = settingsViewModel,
                themeViewModel = themeViewModel,
                context = context,
                startScreen = startScreen
            )
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "MessengerChanel"
            val descriptionText = "Chanel for messenger notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("MessengerId", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}








