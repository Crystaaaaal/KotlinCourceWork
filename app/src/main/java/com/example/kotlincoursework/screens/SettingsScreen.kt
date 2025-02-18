package com.example.kotlincoursework.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun SettingScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor),

        contentAlignment = Alignment.Center
    ) {
        val buttonColors = ButtonDefaults.buttonColors(
            backgroundColor = mainColor,
            contentColor = textColor)
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .align(Alignment.Center))
        {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    //Круг
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(thirdColor)

                            .border(
                                width = 4.dp,
                                color = thirdColor,
                                shape = RoundedCornerShape(1.dp)
                            ), // Фоновый цвет, если изображение отсутствует
                        contentAlignment = Alignment.Center
                    ) {
                        // Изображение из drawable
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(90.dp),
                            painter = painterResource(id = R.drawable.picture), // Загружаем изображение из ресурсов
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop // Масштабирует и обрезает изображение
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Антон Скугарев",
                        fontSize = 24.sp,
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            val modifier:Modifier = Modifier
                .background(
                    color = mainColor,
                    shape = RoundedCornerShape(30.dp))
            .fillMaxWidth()
            .height(50.dp)
                .border(
                    width = 4.dp,
                    color = secondColor,
                    shape = RoundedCornerShape(30.dp))

            item{
                Button(
                    modifier = modifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(30.dp),
                    onClick = {}) {
                    Text(
                        text = "Изменить фото",
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Button(
                    modifier = modifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(30.dp),
                    onClick = {}) {
                    Text(
                        text = "Оформление",
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            item {
                Button(
                    modifier = modifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(30.dp),
                    onClick = {}) {
                    Text(
                        text = "Уведомление",
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

            }
            item {
                Button(
                    modifier = modifier,
                    shape = RoundedCornerShape(30.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = mainColor
                    ),
                    onClick = { LogOut(navController) }) {
                    Text(
                        text = "Выйти из аккаунта",
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

fun LogOut(navController: NavHostController){
    navController.navigate("ToEnter")
}

@Preview(showBackground = true)
@Composable
fun SettingPreview() {
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


        SettingScreen(navController,mainColor,secondColor,thirdColor,textColor)

    }
}