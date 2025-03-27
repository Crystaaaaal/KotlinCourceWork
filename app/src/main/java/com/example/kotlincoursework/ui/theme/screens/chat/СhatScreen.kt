package com.example.kotlincoursework.ui.theme.screens.chat

import android.graphics.BitmapFactory
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
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
import com.example.kotlincoursework.ui.theme.screens.settings.toImageBitmap
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.viewModel.ChatViewModel
import dataBase.User

@Composable
fun chatScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    Column(Modifier.fillMaxSize()) {
        searchPanel(
            navController = navController,
            chatViewModel = chatViewModel
        )

        val state by chatViewModel.searchState.collectAsState()

        when (state) {
            is SeacrhState.Idle -> {
                Log.d("ChatScreen: SeacrhState.Idle", "Idle")
                val byteArray = ByteArray(1)
                val user = User(
                    "+79168653828",
                    "",
                    "Скугарев Антон Павлович",
                    "Crystal",
                    byteArray,
                    "")
                showChats(
                    navController = navController,
                    chatViewModel = chatViewModel,
                    userList = listOf(user)
                )
            }

            is SeacrhState.Loading -> {
                Log.d("ChatScreen: SeacrhState.Loading", "Loading")
            }

            is SeacrhState.Success -> {
                Log.d("ChatScreen: SeacrhState.Success", "Success")
                if ((state as SeacrhState.Success).UserList.isEmpty()) {
                    searchIsError(
                        message = "Ничего не найдено",
                        chatViewModel = chatViewModel
                    )
                }
                else{
                    showChats(
                        navController = navController,
                        chatViewModel = chatViewModel,
                        userList = (state as SeacrhState.Success).UserList)
                }
            }

            is SeacrhState.Error -> {
                Log.d("ChatScreen: SeacrhState.Error", "Error")
                searchIsError(
                    message = (state as SeacrhState.Error).message,
                    chatViewModel = chatViewModel
                )
            }
        }
    }
}

@Composable
fun searchIsError(
    message: String,
    chatViewModel: ChatViewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                color = color.onPrimary,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(100.dp))

            ButtonThirdColor(
                onClick = { chatViewModel.searchUser() },
                buttonText = "Повторить"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .clickable { chatViewModel.resetSearchState() },
                text = "Назад",
                fontSize = 20.sp,
                color = color.primary,
                textDecoration = Underline
            )
        }
    }
}


@Composable
fun showChats(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    userList: List<User>
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(userList) { user ->
            val imageBitmap = user.profileImage?.takeIf { it.isNotEmpty() }?.toImageBitmap()
            Row(
                modifier = Modifier
                    .height(120.dp)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .clickable { navController.navigate("ToUserChat") }
                    .border(
                        width = 4.dp,
                        shape = RoundedCornerShape(15.dp),
                        color = color.primary
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //Круг
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(color.outline)

                        .border(
                            width = 4.dp,
                            color = color.outline,
                            shape = RoundedCornerShape(30.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Изображение из drawable
                    Image(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(70.dp),
                        painter = if (imageBitmap != null) {
                            BitmapPainter(imageBitmap)
                        } else {
                            painterResource(id = R.drawable.picture) // Фолбек, если изображение не загружено
                        },
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop // Масштабирует и обрезает изображение
                    )
                }
                Spacer(modifier = Modifier.width(70.dp))
                Text(
                    text = user.fullName,
                    fontSize = 18.sp,
                    color = color.onPrimary,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Composable
fun searchPanel(
    navController: NavHostController,
    chatViewModel: ChatViewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .height(70.dp)
            .padding(10.dp)
            .fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        val text by chatViewModel.textForSearch.collectAsState()
        SearchAndInputTextWithPlaceholder(
            textForValue = text,
            onValueChange = { chatViewModel.updateTextForSearch(it) },
            placeholderText = "Поиск",
            singleline = true,
            modifier = Modifier
                .width(250.dp)
                .border(
                    width = 4.dp,
                    color = color.primary,
                    shape = RoundedCornerShape(30.dp)
                )
        )

        Spacer(modifier = Modifier.width(20.dp))

        IconButton(modifier = Modifier
            .size(50.dp)
            .background(color.outline, CircleShape),
            onClick = { chatViewModel.searchUser() }) {
            Surface(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .height(80.dp)
                    .width(300.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Чат",
                    tint = color.onPrimary,
                    modifier = Modifier
                        .size(3.dp)
                        .background(color.outline)
                )
            }
        }
    }
    //Spacer(modifier = Modifier.height(30.dp))
}

fun ByteArray.toImageBitmap(): ImageBitmap? {
    return try {
        BitmapFactory.decodeByteArray(this, 0, this.size)?.asImageBitmap()
    } catch (e: Exception) {
        null
    }
}


@Preview(showBackground = true)
@Composable
fun chatPreview() {
    KotlinCourseWorkTheme {
        val navController = rememberNavController()


        //val viewModel = viewModel()
//        searchIsError(
//            message = "text",
//            mainColor = mainColor,
//            secondColor = secondColor,
//            thirdColor = thirdColor,
//            textColor = textColor,
//            viewModel = viewModel
//        )
//        searchPanel(
//            navController = navController,
//            mainColor = mainColor,
//            secondColor = secondColor,
//            thirdColor = thirdColor,
//            textColor = textColor,
//            viewModel = viewModel
//        )
        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        //chatScreen(navController, mainColor, secondColor, thirdColor, textColor)

    }
}