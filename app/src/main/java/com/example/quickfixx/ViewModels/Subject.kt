package com.example.quickfixx.ViewModels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Subject @Inject constructor(){
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun currTitle(title: String){
        _title.value = title
    }
}