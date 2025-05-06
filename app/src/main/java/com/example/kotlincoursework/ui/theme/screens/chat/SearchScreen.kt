package com.example.kotlincoursework.ui.theme.screens.chat

import android.graphics.BitmapFactory
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.classes.SearchHistoryManager
import com.example.kotlincoursework.classes.rememberUserHistoryManager
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.ButtonThirdColor
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.viewModel.SearchViewModel
import com.example.kotlincoursework.viewModel.viewModel
import dataBase.User

@Composable
fun SearchScreen(
    viewModel: viewModel,
    navController: NavHostController,
    searchViewModel: SearchViewModel
) {
    Column(Modifier.fillMaxSize()) {
        val state by searchViewModel.searchState.collectAsState()
        val historyManager by rememberUserHistoryManager()

        LaunchedEffect(Unit) {
            searchViewModel.loadHistory(historyManager)
        }

        when (state) {
            is SeacrhState.Idle -> {
                showHistory(
                    viewModel = viewModel,
                    navController = navController,
                    historyManager = historyManager,
                    searchViewModel = searchViewModel
                )
            }

            is SeacrhState.Loading -> {
                searchLoading()
            }

            is SeacrhState.Success -> {
                if ((state as SeacrhState.Success).UserList.isEmpty()) {
                    searchIsError(
                        message = "Ничего не найдено",
                        searchViewModel = searchViewModel
                    )
                } else {
                    showChats(
                        navController = navController,
                        viewModel = viewModel,
                        userList = (state as SeacrhState.Success).UserList,
                        historyManager = historyManager
                    )
                }
            }

            is SeacrhState.Error -> {
                searchIsError(
                    message = (state as SeacrhState.Error).message,
                    searchViewModel = searchViewModel
                )
            }
        }
    }
}

@Composable
fun searchLoading() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(50.dp),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 4.dp
        )
    }
}

@Composable
fun searchIsError(
    message: String,
    searchViewModel: SearchViewModel
) {
    val color = MaterialTheme.colorScheme
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
                onClick = { searchViewModel.searchUser() },
                buttonText = "Повторить"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                modifier = Modifier
                    .clickable { searchViewModel.resetSearchState() },
                text = "Назад",
                fontSize = 20.sp,
                color = color.primary,
                textDecoration = Underline
            )
        }
    }
}

@Composable
fun showHistory(
    viewModel: viewModel,
    historyManager: SearchHistoryManager,
    navController: NavHostController,
    searchViewModel: SearchViewModel
) {
    val userList = historyManager.getUserHistory()
    val historyUpdated by searchViewModel.historyUpdated

    LaunchedEffect(historyUpdated) {
        searchViewModel.loadHistory(historyManager)
    }

    when {
        userList.isEmpty() -> historyIsEmpty()
        else -> {
            val color = MaterialTheme.colorScheme
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = color.primary)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Вы искали:",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                        )
                        Text(
                            text = "Очистить",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .clickable {
                                    searchViewModel.clearHistory(historyManager)
                                }
                        )
                    }
                }
                showChats(
                    navController = navController,
                    viewModel = viewModel,
                    userList = userList,
                    historyManager = historyManager
                )
            }
        }
    }
}

@Composable
fun historyIsEmpty() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "История пуста",
            fontSize = 20.sp
        )
    }
}

@Composable
fun showChats(
    navController: NavHostController,
    viewModel: viewModel,
    userList: List<User>,
    historyManager: SearchHistoryManager
) {
    val color = MaterialTheme.colorScheme
    Column() {
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            items(userList) { user ->
                val imageBitmap =
                    user.profileImage?.takeIf { it.isNotEmpty() }?.toImageBitmap()
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.setUser(user)
                            navController.navigate("ToUserChat")
                            historyManager.saveUser(user)
                        }
                        .border(
                            width = 4.dp,
                            shape = RoundedCornerShape(15.dp),
                            color = color.primary
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(70.dp),
                            painter = if (imageBitmap != null) {
                                BitmapPainter(imageBitmap)
                            } else {
                                painterResource(id = R.drawable.picture)
                            },
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop
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
    }
}