package com.example.kotlincoursework

import androidx.compose.runtime.Composable
import com.example.kotlincoursework.viewModel.MainScreenViewModel


fun ShowMessage(
    viewModel: MainScreenViewModel,
    messageText: String
){
    viewModel.addItem(messageText)
}