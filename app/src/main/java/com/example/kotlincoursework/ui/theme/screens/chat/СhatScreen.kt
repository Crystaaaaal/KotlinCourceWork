package com.example.kotlincoursework.ui.theme.screens.chat

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
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kotlincoursework.R
import com.example.kotlincoursework.viewModel.SearchViewModel
import com.example.kotlincoursework.viewModel.viewModel


@Composable
fun chatScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    viewModel: viewModel
) {
    val color = MaterialTheme.colorScheme
    searchViewModel.getAllUsers()
    val userList = searchViewModel.allChatUser
    Column {
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