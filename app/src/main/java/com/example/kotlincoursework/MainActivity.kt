package com.example.kotlincoursework


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.ui.theme.BarDrawing
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.viewModel.viewModel


var mainColor: Color = Color.Black
var secondColor: Color = Color.Black
var thirdColor: Color = Color.Black
var textColor: Color = Color.Black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinCourseWorkTheme {
                mainColor = colorResource(R.color.light_main_color)
                secondColor = colorResource(R.color.light_second_color)
                thirdColor = colorResource(R.color.light_third_color)
                textColor = colorResource(R.color.light_text_color)
                val applicatonContext = applicationContext

                val navController = rememberNavController()
                val masterKey = MasterKey.Builder(applicationContext, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()

                val sharedPreferences = EncryptedSharedPreferences.create(
                    applicationContext,
                    "secure_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )

                val viewModel: viewModel = viewModel(
                    applicationContext = applicatonContext,
                    sharedPreferences = sharedPreferences
                )
                viewModel.getUserInfo()

                //Отрисовываем панелей
                BarDrawing(
                    navController = navController,
                    viewModel = viewModel,
                )
            }
        }
    }
}




