package com.example.kotlincoursework.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme

@Composable
fun NameAppTextWithExtra(
    extraText: String) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .padding(vertical = 50.dp)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Мессенджер",
                modifier = Modifier
                    .padding(end = 100.dp),
                fontSize = 30.sp,
                color = color.primary
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = extraText,
                modifier = Modifier
                    .padding(start = 100.dp),
                fontSize = 24.sp,
                color = color.secondary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NameAppPreview() {
    KotlinCourseWorkTheme {

        NameAppTextWithExtra(
            extraText = "Вход в аккаунт"
        )

    }
}