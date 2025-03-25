package com.example.kotlincoursework.ui.theme.screens.settings

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.ui.theme.KotlinCourseWorkTheme
import com.example.kotlincoursework.ui.theme.components.SettingsButton
import com.example.kotlincoursework.viewModel.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Composable
fun SettingScreen(
    navController: NavHostController,
    mainColor: Color,
    secondColor: Color,
    thirdColor: Color,
    textColor: Color,
    viewModel: viewModel
) {
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var showImagePicker by remember { mutableStateOf(false) } // Состояние для управления выбором изображения
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val ActiveUser by viewModel.ActiveUser.collectAsState()
    val imageBitmap = remember(ActiveUser.userImage) {
        ActiveUser.userImage.takeIf { it.isNotEmpty() }?.toImageBitmap()
    }

    // Launcher для выбора изображения из галереи
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                coroutineScope.launch {
                    val bytes = getImageBytes(context, it)
                    imageBytes = bytes // Сохраняем массив байтов
                    bytes?.let { onImagePicked ->
                        viewModel.updateUserImageMas(onImagePicked)
                        viewModel.udpateUserImage()
                    }
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainColor),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(thirdColor)
                            .border(
                                width = 4.dp,
                                color = thirdColor,
                                shape = RoundedCornerShape(1.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(90.dp),
                            painter = if (imageBitmap != null) {
                                BitmapPainter(imageBitmap)
                            } else { painterResource(id = R.drawable.picture) // Фолбек, если изображение не загружено
                            },
                            contentDescription = "User Avatar",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(30.dp))
                    Text(
                        text = "Антон Скугарев",
                        fontSize = 24.sp,
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            item {
                SettingsButton(
                    mainColor = mainColor,
                    secondColor = secondColor,
                    textColor = textColor,
                    buttonText = "Изменить фото",
                    onClick = {
                        // Запускаем выбор изображения
                        imagePickerLauncher.launch("image/*")
                    }
                )
            }
            item {
                SettingsButton(
                    mainColor = mainColor,
                    secondColor = secondColor,
                    textColor = textColor,
                    buttonText = "Оформление",
                    onClick = { navController.navigate("ToAppearance") }
                )
            }
            item {
                SettingsButton(
                    mainColor = mainColor,
                    secondColor = secondColor,
                    textColor = textColor,
                    buttonText = "Уведомления",
                    onClick = { navController.navigate("ToNotification") }
                )
            }
            item {
                SettingsButton(
                    mainColor = mainColor,
                    secondColor = secondColor,
                    textColor = Color.Red,
                    buttonText = "Выйти из аккаунта",
                    onClick = { navController.navigate("ToEnter") }
                )
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

// Функция для преобразования Uri изображения в массив байтов
private suspend fun getImageBytes(context: android.content.Context, uri: Uri): ByteArray? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap?.let {
                val stream = ByteArrayOutputStream()
                it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, stream)
                stream.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


    @Preview(showBackground = true)
    @Composable
    fun SettingPreview() {
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


            //SettingScreen(navController, mainColor, secondColor, thirdColor, textColor)

        }
    }
