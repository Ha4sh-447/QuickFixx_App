package com.example.quickfixx.presentation.HomePage

import androidx.compose.ui.graphics.vector.ImageVector

data class Services(
    val title: String,
    val icon: ImageVector,
    val description: String,
    val image: Int,
    val index: Int
)

data class ServiceIcon(
    val icon: ImageVector,
    val route: String,
    val tabIndex: Int
)