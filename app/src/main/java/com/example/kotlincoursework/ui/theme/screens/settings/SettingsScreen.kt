package com.example.kotlincoursework.ui.theme.screens.settings

import android.content.Context
import android.graphics.Bitmap
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
import com.example.kotlincoursework.viewModel.SettingsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


@Composable
fun SettingScreen(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    val color = androidx.compose.material3.MaterialTheme.colorScheme
    var imageBytes by remember { mutableStateOf<ByteArray?>(null) }
    var showImagePicker by remember { mutableStateOf(false) } // Состояние для управления выбором изображения
    val context = LocalContext.current
    val ioCoroutineScope = remember {
        CoroutineScope(Dispatchers.IO + SupervisorJob())
    }
    val ActiveUser by settingsViewModel.ActiveUser.collectAsState()
    val imageBitmap = remember(ActiveUser.userImage) {
        ActiveUser.userImage.takeIf { it.isNotEmpty() }?.toImageBitmap()
    }

    // Launcher для выбора изображения из галереи
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                ioCoroutineScope.launch {
                    val bytes = getImageBytes(context, it)
                    imageBytes = bytes // Сохраняем массив байтов
                    bytes?.let { onImagePicked ->
                        settingsViewModel.updateUserImageMas(onImagePicked)
                        settingsViewModel.udpateUserImage()
                    }
                }
            }
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color.background),
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
                            .background(color.outline)
                            .border(
                                width = 4.dp,
                                color = color.outline,
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
                        text = ActiveUser.fullName,
                        fontSize = 24.sp,
                        color = color.onPrimary,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
            item {
                SettingsButton(
                    buttonText = "Изменить фото",
                    onClick = {
                        // Запускаем выбор изображения
                        imagePickerLauncher.launch("image/*")
                    }
                )
            }
            item {
                SettingsButton(
                    buttonText = "Оформление",
                    onClick = { navController.navigate("ToAppearance") }
                )
            }
            item {
                SettingsButton(
                    buttonText = "Уведомления",
                    onClick = { navController.navigate("ToNotification") }
                )
            }
            item {
                SettingsButton(
                    buttonText = "Выйти из аккаунта",
                    warningColor = true,
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
//private suspend fun getImageBytes(context: android.content.Context, uri: Uri): ByteArray? {
//    return withContext(Dispatchers.IO) {
//        try {
//            val inputStream = context.contentResolver.openInputStream(uri)
//            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
//            inputStream?.close()
//            bitmap?.let {
//                val stream = ByteArrayOutputStream()
//                it.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, stream)
//                stream.toByteArray()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//}
private suspend fun getImageBytes(
    context: Context,
    uri: Uri,
    maxWidth: Int = 1024,
    maxHeight: Int = 1024,
    quality: Int = 80
): ByteArray? = withContext(Dispatchers.IO) {
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            // Первое декодирование только для получения размеров
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)

            // Вычисляем коэффициент масштабирования
            val (width, height) = options.outWidth to options.outHeight
            val scaleFactor = calculateInSampleSize(options, maxWidth, maxHeight)

            // Декодируем с уменьшением размера
            val newOptions = BitmapFactory.Options().apply {
                inSampleSize = scaleFactor
            }

            // Переоткрываем поток (важно!)
            context.contentResolver.openInputStream(uri)?.use { newInputStream ->
                BitmapFactory.decodeStream(newInputStream, null, newOptions)?.let { bitmap ->
                    ByteArrayOutputStream().use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                        outputStream.toByteArray()
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Вычисление коэффициента уменьшения
private fun calculateInSampleSize(
    options: BitmapFactory.Options,
    reqWidth: Int,
    reqHeight: Int
): Int {
    val (height, width) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2

        while (halfHeight / inSampleSize >= reqHeight &&
            halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}


    @Preview(showBackground = true)
    @Composable
    fun SettingPreview() {
        KotlinCourseWorkTheme {
            val navController = rememberNavController()

//        val mainColor = colorResource(R.color.dark_main_color)
//        val secondColor = colorResource(R.color.dark_second_color)
//        val thirdColor = colorResource(R.color.dark_third_color)
//        val textColor = colorResource(R.color.dark_text_color)


            //SettingScreen(navController, mainColor, secondColor, thirdColor, textColor)

        }
    }
