package com.example.kotlincoursework.screens


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlincoursework.BarDrawing
import com.example.kotlincoursework.viewModel.MainScreenViewModel
import com.example.kotlincoursework.R


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


// Метод начала отрисовки приложения
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppStart() {

    mainColor = colorResource(R.color.light_main_color)
    secondColor = colorResource(R.color.light_second_color)
    thirdColor = colorResource(R.color.light_third_color)
    textColor = colorResource(R.color.light_text_color)

//    mainColor = colorResource(R.color.dark_main_color)
//    secondColor = colorResource(R.color.dark_second_color)
//    thirdColor = colorResource(R.color.dark_third_color)
//    textColor = colorResource(R.color.dark_text_color)

    val viewModel: MainScreenViewModel = viewModel()
    val navController = rememberNavController()


    //Отрисовываем панели
    BarDrawing(navController, viewModel)

}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    KotlinCourseWorkTheme {
        AppStart()
    }
}