package com.example.kotlincoursework.ui.theme.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.components.NameAppTextWithExtra
import com.example.kotlincoursework.ui.theme.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.components.Toast
import com.example.kotlincoursework.viewModel.viewModel
import kotlinx.coroutines.delay

@Composable
fun SecondRegisterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    var message by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf(false) }
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


        var colorForRegisterSecondName by remember { mutableStateOf(secondColor) }
        var colorForRegisterName by remember { mutableStateOf(secondColor) }
        var colorForRegisterFatherName by remember { mutableStateOf(secondColor) }

        val textForRegisterSecondName by viewModel.textForRegisterSecondName.collectAsState()
        val textForRegisterName by viewModel.textForRegisterName.collectAsState()
        val textForRegisterFatherName by viewModel.textForRegisterFatherName.collectAsState()





        if (textForRegisterSecondName != "" && !viewModel.isRegisterSecondNameValid) {
            colorForRegisterSecondName = Color.Red
        } else {
            colorForRegisterSecondName = secondColor
        }

        if (textForRegisterName != "" && !viewModel.isRegisterNameValid) {
            colorForRegisterName = Color.Red
        } else {
            colorForRegisterName = secondColor
        }

        if (textForRegisterFatherName != "" && !viewModel.isRegisterFatherNameValid) {
            colorForRegisterFatherName = Color.Red
        } else {
            colorForRegisterFatherName = secondColor
        }



        Spacer(modifier = Modifier.height(100.dp))

        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterSecondName,
            textColor = textColor,
            textForValue = textForRegisterSecondName,
            onValueChange = { viewModel.updateTextForRegisterSecondName(it) },
            titleText = "Фамилия"
        )

        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterName,
            textColor = textColor,
            textForValue = textForRegisterName,
            onValueChange = { viewModel.updateTextForRegisterName(it) },
            titleText = "Имя"
        )


        RegisterAndAuntificationTextFieldsWithText(
            mainColor = mainColor,
            secondColor = colorForRegisterFatherName,
            textColor = textColor,
            textForValue = textForRegisterFatherName,
            onValueChange = { viewModel.updateTextForRegisterFatherName(it) },
            titleText = "Отчество"
        )

        Spacer(modifier = Modifier.height(130.dp))

        ButtonThirdColor(
            thirdColor = thirdColor,
            textColor = textColor,
            onClick = {
                viewModel.registerUser { isSuccess ->
                    if (isSuccess) {
                        navController.navigate("ToEnter")
                        viewModel.updateTextForRegisterPhoneNumber("+7")
                        viewModel.updateTextForRegisterLogin("")
                        viewModel.updateTextForRegisterPassword("")
                        viewModel.updateTextForRegisterSecondName("")
                        viewModel.updateTextForRegisterName("")
                        viewModel.updateTextForRegisterFatherName("")
                    } else {
                        message = "Неверное заполнение полей"
                        showToast = true
                    }
                }
            },
            buttonText = "Закончить"
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier
                .clickable {
                    navController.navigate("ToRegister")
                },
            text = "Назад",
            fontSize = 20.sp,
            color = secondColor,
            textDecoration = Underline
        )

    }
    Toast(
        message = message,
        visible = showToast,
        mainColor = mainColor,
        secondColor = secondColor,
        textColor = textColor
    )
    LaunchedEffect(showToast) {
        if (showToast) {
            delay(3000)
            showToast = false
        }
    }
}

@Composable
fun UserEnter() {
    val viewModel: viewModel = viewModel()
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


        //SecondRegisterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}