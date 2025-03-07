package com.example.kotlincoursework.ui.theme.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.viewModel.viewModel

@Composable
fun ChatWithUserScreen(
    navController: NavController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
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
                    text = viewModel.items[viewModel.items.size - 1 - item],
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
        val viewModel: viewModel = viewModel()
        ChatWithUserScreen(navController, mainColor, secondColor, thirdColor, textColor, viewModel)

    }
}