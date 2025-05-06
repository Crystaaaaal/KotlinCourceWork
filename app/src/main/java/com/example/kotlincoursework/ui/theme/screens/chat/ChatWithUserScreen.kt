package com.example.kotlincoursework.ui.theme.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import dataBase.MessageForShow
import kotlinx.coroutines.launch

@Composable
fun ChatWithUserScreen(
    navController: NavController,
    viewModel: viewModel
) {
    val color = MaterialTheme.colorScheme
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Объединение и сортировка сообщений (новые в конце списка)
    val allMessages by remember(viewModel.items, viewModel.incomingItems) {
        derivedStateOf {
            (viewModel.items.map { it to true } + viewModel.incomingItems.map { it to false })
                .sortedBy { (message, _) -> message.sentAt }
        }
    }


    // Автоскролл к новому сообщению
    LaunchedEffect(allMessages.size) {
        if (allMessages.isNotEmpty()) {
            lazyListState.animateScrollToItem(allMessages.size - 1)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState,
            verticalArrangement = Arrangement.Bottom, // Сообщения прижимаем к низу
            horizontalAlignment = Alignment.Start // Базовое выравнивание
        ) {
            items(items = allMessages) { (message, isOutgoing) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentAlignment = if (isOutgoing) Alignment.BottomEnd else Alignment.BottomStart
                ) {
                    if (isOutgoing) {
                        OutgoingMessage(message, color)
                    } else {
                        IncomingMessage(message, color)
                    }
                }
            }
        }

        // Кнопка для скролла вниз
        if (lazyListState.firstVisibleItemIndex < allMessages.lastIndex - 30) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(allMessages.size - 1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                backgroundColor = color.primary
            ) {
                Icon(
                    Icons.Default.ArrowDownward,
                    contentDescription = "Scroll to bottom",
                    tint = color.onPrimary
                )
            }
        }
    }
}

@Composable
private fun OutgoingMessage(message: MessageForShow, color: ColorScheme) {
    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .background(
                color = color.primary,
                shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp)
            )
            .padding(12.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = message.messageText,
                color = color.onPrimary,
                fontSize = 20.sp
            )
            Box() {
                Text(
                    text = message.sentAt.substring(11, 16),
                    color = color.onPrimary,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }

    }
}

@Composable
private fun IncomingMessage(message: MessageForShow, color: ColorScheme) {
    Box(
        modifier = Modifier
            .padding(start = 16.dp)
            .background(
                color = color.secondary,
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
            )
            .padding(12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box() {
                Text(
                    text = message.sentAt.substring(11, 16),
                    color = color.onSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
            Text(
                text = message.messageText,
                color = color.onSecondary,
                fontSize = 20.sp
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun chatWithUserPreview() {
    KotlinCourseWorkTheme {
        val navController = rememberNavController()

        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        //val viewModel: viewModel = viewModel()
        //ChatWithUserScreen(navController, mainColor, secondColor, thirdColor, textColor, viewModel)

    }
}