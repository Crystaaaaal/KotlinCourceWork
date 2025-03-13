package com.example.kotlincoursework.ui.theme.screens.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.components.SearchAndInputTextWithPlaceholder
import com.example.kotlincoursework.ui.theme.screens.auth.registrationIsError
import com.example.kotlincoursework.ui.theme.screens.auth.registrationIsSucces
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.viewModel.viewModel
import dataBase.Message
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun chatScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
    //items: List<String>
) {
    Column(Modifier.fillMaxSize()) {
        searchPanel(
            navController = navController,
            mainColor = mainColor,
            secondColor = secondColor,
            thirdColor = thirdColor,
            textColor = textColor,
            viewModel = viewModel
        )
        //Spacer(Modifier.height(0.dp))

        val state by viewModel.searchState.collectAsState()

        when (state) {
            is SeacrhState.Idle -> {
                Log.d("ChatScreen: SeacrhState.Idle", "Idle")
                showChats(
                    navController = navController,
                    mainColor = mainColor,
                    secondColor = secondColor,
                    thirdColor = thirdColor,
                    textColor = textColor,
                    viewModel = viewModel
                )
            }

            is SeacrhState.Loading -> {
                Log.d("ChatScreen: SeacrhState.Loading", "Loading")
            }

            is SeacrhState.Success -> {
                Log.d("ChatScreen: SeacrhState.Success", "Success")
            }

            is SeacrhState.Error -> {
                Log.d("ChatScreen: SeacrhState.Error", "Error")
                searchIsError(
                    message = (state as SeacrhState.Error).message,
                    mainColor = mainColor,
                    secondColor = secondColor,
                    thirdColor = thirdColor,
                    textColor = textColor,
                    viewModel = viewModel

                )
            }
        }
    }
}

@Composable
fun searchIsError(
    message: String,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(mainColor),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                color = textColor,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(100.dp))

            ButtonThirdColor(
                thirdColor = thirdColor,
                textColor = textColor,
                onClick = { viewModel.searchUser() },
                buttonText = "Повторить"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .clickable { viewModel.resetSearchState() },
                text = "Назад",
                fontSize = 20.sp,
                color = secondColor,
                textDecoration = Underline
            )
        }
    }
}

@Composable
fun showChats(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(4) {
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("ToUserChat") }
                    .border(
                        width = 4.dp,
                        shape = RoundedCornerShape(15.dp),
                        color = secondColor
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Круг
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(thirdColor)

                        .border(
                            width = 4.dp,
                            color = thirdColor,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Изображение из drawable
                    Image(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(70.dp),
                        painter = painterResource(id = R.drawable.picture), // Загружаем изображение из ресурсов
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop // Масштабирует и обрезает изображение
                    )
                }
                Spacer(modifier = Modifier.width(70.dp))
                Text(
                    text = "Антон Скугарев",
                    fontSize = 18.sp,
                    color = textColor,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun searchPanel(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(10.dp)
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        val text by viewModel.textForSearch.collectAsState()
        SearchAndInputTextWithPlaceholder(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = text,
            onValueChange = { viewModel.updateTextForSearch(it) },
            placeholderText = "Поиск",
            singleline = true,
            modifier = Modifier
                .width(250.dp)
                .border(
                    width = 4.dp,
                    color = secondColor,
                    shape = RoundedCornerShape(30.dp)
                )
        )

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(modifier = Modifier
            .size(50.dp)
            .background(thirdColor, CircleShape),
            onClick = { viewModel.searchUser() }) {
            Surface(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(80.dp)
                    .width(300.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Чат",
                    tint = textColor,
                    modifier = Modifier
                        .size(3.dp)
                        .background(thirdColor)
                )
            }
        }
    }
    //Spacer(modifier = Modifier.height(30.dp))
}


@Preview(showBackground = true)
@Composable
fun chatPreview() {
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
        val viewModel = viewModel()
//        searchIsError(
//            message = "text",
//            mainColor = mainColor,
//            secondColor = secondColor,
//            thirdColor = thirdColor,
//            textColor = textColor,
//            viewModel = viewModel
//        )
        searchPanel(
            navController = navController,
            mainColor = mainColor,
            secondColor = secondColor,
            thirdColor = thirdColor,
            textColor = textColor,
            viewModel = viewModel
        )
        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        //chatScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}