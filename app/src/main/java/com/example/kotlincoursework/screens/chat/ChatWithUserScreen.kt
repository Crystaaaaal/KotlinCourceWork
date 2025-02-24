package com.example.kotlincoursework.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.viewModel.MainScreenViewModel

@Composable
fun ChatWithUserScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: MainScreenViewModel
) {
    // Отображение списка Box с помощью LazyColumn
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.End,
        reverseLayout = true // Элементы будут добавляться снизу
    ) {
        items(viewModel.items.size) { item ->
            Box(
                modifier = Modifier
                    .background(mainColor)
                    .wrapContentSize()
                    .padding(10.dp)
                    .border(
                        width = 4.dp,
                        color = secondColor,
                        shape = RoundedCornerShape(30.dp)
                    ),

                ) {
                Text(
                    modifier = Modifier
                        .padding(20.dp)
                        .widthIn(max = 300.dp),
                    text = viewModel.items[item],
                    color = textColor,
                    softWrap = true,
                    fontSize = 18.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun chatWithUserPreview() {
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

        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val viewModel: MainScreenViewModel = viewModel()
        ChatWithUserScreen(navController, mainColor, secondColor, thirdColor, textColor, viewModel)

    }
}