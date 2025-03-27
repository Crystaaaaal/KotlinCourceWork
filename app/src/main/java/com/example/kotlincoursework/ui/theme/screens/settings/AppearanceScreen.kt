package com.example.kotlincoursework.ui.theme.screens.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.DarkPrimary
import com.example.kotlincoursework.ui.theme.GreenPrimary
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.LightPrimary
import com.example.kotlincoursework.ui.theme.OrangePrimary
import com.example.kotlincoursework.ui.theme.PurplePrimary
import com.example.kotlincoursework.ui.theme.components.CustomToggleSwitch
import com.example.kotlincoursework.ui.theme.components.SwapColorButton
import com.example.kotlincoursework.viewModel.ThemeViewModel

@Composable
fun AppearanceScreen(
    navController: NavHostController,
    themeViewModel: ThemeViewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(10.dp)
                .border(
                    width = 4.dp,
                    color = color.primary,
                    shape = RoundedCornerShape(30.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Выберите тему:",
                fontSize = 18.sp,
                color = color.onPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            val currentTheme by themeViewModel.currentTheme.collectAsState()

            // Определяем состояние для каждой кнопки на основе текущей темы
            val lightSelected = currentTheme.id == "light"
            val darkSelected = currentTheme.id == "dark"
            val purpleSelected = currentTheme.id == "purple"
            val greenSelected = currentTheme.id == "green"
            val orangeSelected = currentTheme.id == "orange"

            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SwapColorButton(
                    color = LightPrimary,
                    isSelected = lightSelected,
                    onClick = { themeViewModel.setTheme("light") }
                )

                SwapColorButton(
                    color = DarkPrimary,
                    isSelected = darkSelected,
                    onClick = { themeViewModel.setTheme("dark") }
                )

                SwapColorButton(
                    color = PurplePrimary,
                    isSelected = purpleSelected,
                    onClick = { themeViewModel.setTheme("purple") }
                )
            }

            Row(
                modifier = Modifier
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SwapColorButton(
                    color = GreenPrimary,
                    isSelected = greenSelected,
                    onClick = { themeViewModel.setTheme("green") }
                )

                SwapColorButton(
                    color = OrangePrimary,
                    isSelected = orangeSelected,
                    onClick = { themeViewModel.setTheme("orange") }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppearancePreview() {
    KotlinCourseWorkTheme {
        val navController = rememberNavController()

        //val sampleItems = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        //AppearanceScreen(navController)

    }
}