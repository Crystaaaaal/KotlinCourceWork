package com.example.kotlincoursework.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun RegisterAndAuntificationTextFieldsWithText(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    textForValue: String,
    onValueChange: (String) -> Unit,
    titleText: String
) {
    Column() {
        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            text = titleText,
            color = textColor,
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        )

        TextField(
            value = textForValue,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(20.dp),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = textColor,
                textAlign = TextAlign.Start
            ),

            colors = TextFieldDefaults.colors
                (
                focusedContainerColor = mainColor,
                unfocusedContainerColor = mainColor
            ),
            modifier = Modifier
                .background(mainColor)
                .clip(RoundedCornerShape(20.dp))
                .height(50.dp)
                .width(300.dp)
                .fillMaxWidth()
                .border
                    (
                    width = 4.dp,
                    color = secondColor,
                    shape = RoundedCornerShape(20.dp)
                )
        )
    }
}

@Composable
fun SearchAndInputTextWithPlaceholder(
    mainColor: Color,
    secondColor: Color,
    textColor: Color,
    textForValue: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    singleline:Boolean,
    modifier: Modifier
){
    TextField(
        singleLine = singleline,
        value = textForValue,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholderText,
                color = textColor,
                fontSize = 18.sp
            )
        },
        shape = RoundedCornerShape(30.dp),
        textStyle = androidx.compose.ui.text.TextStyle(color = textColor, fontSize = 18.sp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = mainColor,
            unfocusedContainerColor = mainColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
            .background(
                secondColor,
                shape = RoundedCornerShape(30.dp))
            .clip(RoundedCornerShape(30.dp))
    )
}


@Preview(showBackground = true)
@Composable
fun TextFieldPreview() {
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

        var textForPhoneNumber by rememberSaveable { mutableStateOf("") }
//        RegisterAndAuntificationTextFieldsWithText(
//            mainColor = mainColor,
//            secondColor = secondColor,
//            textColor = textColor,
//            textForValue = textForPhoneNumber,
//            onValueChange = { textForPhoneNumber = it },
//            titleText = "Номер телефона"
//        )

        SearchAndInputTextWithPlaceholder(
            mainColor = mainColor,
            secondColor = secondColor,
            textColor = textColor,
            textForValue = textForPhoneNumber,
            onValueChange = { textForPhoneNumber = it },
            placeholderText = "Сообщение",
            singleline = true,
            modifier = Modifier
        )

    }
}