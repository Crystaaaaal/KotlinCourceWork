package com.example.kotlincoursework

import com.example.kotlincoursework.viewModel.viewModel


fun ShowMessage(
    viewModel: viewModel,
    messageText: String
){
    viewModel.addItem(messageText)
}