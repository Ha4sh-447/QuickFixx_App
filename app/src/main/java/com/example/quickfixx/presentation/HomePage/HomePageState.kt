package com.example.quickfixx.presentation.HomePage

data class HomePageState(
    val index: Int,
    val service: Services?= null,
    val title: String,
)
