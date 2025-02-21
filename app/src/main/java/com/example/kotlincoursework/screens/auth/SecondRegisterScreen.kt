package com.example.kotlincoursework.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.components.ButtonThirdColor
import com.example.kotlincoursework.components.NameAppTextWithExtra
import com.example.kotlincoursework.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.viewModel.MainScreenViewModel

@Composable
fun SecondRegisterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NameAppTextWithExtra(
            secondColor = secondColor,
            thirdColor = thirdColor,
            extraText = "Личные данные"
        )

        Spacer(modifier = Modifier.height(100.dp))

        var textForRegisterPhoneNumber by rememberSaveable { mutableStateOf("") }
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForRegisterPhoneNumber,
            onValueChange = { textForRegisterPhoneNumber = it },
            titleText = "Фамилия"
        )
        var textForRegisterLogin by rememberSaveable { mutableStateOf("") }
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForRegisterLogin,
            onValueChange = { textForRegisterLogin = it },
            titleText = "Имя"
        )

        var textForRegisterPassword by rememberSaveable { mutableStateOf("") }
        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForRegisterPassword,
            onValueChange = { textForRegisterPassword = it },
            titleText = "Отчество"
        )

        Spacer(modifier = Modifier.height(130.dp))

        ButtonThirdColor(
            thirdColor = thirdColor,
            textColor = textColor,
            navController = navController,
            navControllerRoute = "ToChat",
            buttonText = "Закончить"
        )

    }
}

@Composable
fun UserEnter(){
    val viewModel: MainScreenViewModel = viewModel()
    viewModel.updateTopBarText("Мессенджер")
}

@Preview(showBackground = true)
@Composable
fun secondRegisterPreview() {
    KotlinCourseWorkTheme {
        val mainColor = colorResource(R.color.light_main_color)
        val secondColor = colorResource(R.color.light_second_color)
        val thirdColor = colorResource(R.color.light_third_color)
        val textColor = colorResource(R.color.light_text_color)
        val navController = rememberNavController()

//        val mainColor = colorResource(R.color.dark_main_color)
//        val secondColor = colorResource(R.color.dark_second_color)
//        val thirdColor = colorResource(R.color.dark_third_color)
//        val textColor = colorResource(R.color.dark_text_color)


        SecondRegisterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}