package com.example.kotlincoursework.screens

import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.components.NameAppTextWithExtra
import com.example.kotlincoursework.components.RegisterAndAuntificationTextFieldsWithText
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun enterScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor)
    )
    {
        NameAppTextWithExtra(
            secondColor = secondColor,
            thirdColor = thirdColor,
            extraText = "Вход в аккаунт"
        )
    }

    Spacer(modifier = Modifier.height(300.dp))

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            var textForPhoneNumber by rememberSaveable { mutableStateOf("") }
            RegisterAndAuntificationTextFieldsWithText(
                mainColor = mainColor,
                secondColor = secondColor,
                textColor = textColor,
                textForValue = textForPhoneNumber,
                onValueChange = { textForPhoneNumber = it },
                titleText = "Номер телефона"

            )

            Spacer(modifier = Modifier.height(20.dp))

            var textForPassword by rememberSaveable { mutableStateOf("") }
            RegisterAndAuntificationTextFieldsWithText(
                mainColor = mainColor,
                secondColor = secondColor,
                textColor = textColor,
                textForValue = textForPassword,
                onValueChange = { textForPassword = it },
                titleText = "Пароль"

            )

            Spacer(modifier = Modifier.height(100.dp))

            val buttonColors = ButtonDefaults.buttonColors(
                backgroundColor = thirdColor,
                contentColor = textColor)
            androidx.compose.material.Button(
                modifier = Modifier
                    .size(120.dp, 50.dp),
                colors = buttonColors,
                shape = RoundedCornerShape(20.dp),
                onClick = { navController.navigate("ToChat") }) {
                Text(
                    text = "Войти",
                    fontSize = 18.sp,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .clickable {},
                text = "Регистрация",
                fontSize = 20.sp,
                color = secondColor,
                textDecoration = Underline
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun EnterPreview() {
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


        enterScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}