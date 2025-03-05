package com.example.kotlincoursework


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlincoursework.API.ApiServer
import com.example.kotlincoursework.API.ServerRepository
import com.example.kotlincoursework.viewModel.MainScreenViewModel
import com.example.kotlincoursework.ui.theme.BarDrawing
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var mainColor: Color = Color.Black
var secondColor: Color = Color.Black
var thirdColor: Color = Color.Black
var textColor: Color = Color.Black

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Пример вызова репозитория
        val server = CoroutineScope(Dispatchers.IO).launch {
            val isServerOnline = async {  ServerRepository().checkServerStatus()}.await()
            //delay(100)
            withContext(Dispatchers.Main) {
                if (isServerOnline) {
                    Log.d("ServerStatus", "Сервер онлайн!")
                } else {
                    Log.e("ServerStatus", "Сервер оффлайн!")
                }
            }
        }
//        if (isServerOnline) {
//            Log.d("ServerStatus", "Сервер онлайн!")
//        } else {
//            Log.e("ServerStatus", "Сервер оффлайн!")
//        }
        setContent {
            KotlinCourseWorkTheme {
                mainColor = colorResource(R.color.light_main_color)
                secondColor = colorResource(R.color.light_second_color)
                thirdColor = colorResource(R.color.light_third_color)
                textColor = colorResource(R.color.light_text_color)

                val viewModel: MainScreenViewModel = viewModel()
                val navController = rememberNavController()

                //Отрисовываем панелей
                BarDrawing(navController, viewModel)
            }
        }
    }


}




