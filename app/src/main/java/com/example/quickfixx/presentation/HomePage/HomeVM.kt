package com.example.quickfixx.presentation.HomePage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickfixx.ViewModels.Subject
import com.example.quickfixx.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class HomeVM  @Inject constructor(
    private val subject: Subject
): ViewModel(){
    private val _state = MutableStateFlow(HomePageState(0, title = ""))
    private val _title = MutableStateFlow("")
    val homeState = _state.asStateFlow()
    val title = subject.title


    fun currService(services: Services, title: String){
        Log.d("INFO from curr-service", services.title)

        _state.value = homeState.value.copy(
            service = services,
            title = title
        )
        Log.d("INFO from curr_service, state set", services.title)
    }

    fun currTitle(currtitle: String){
        subject.currTitle(currtitle)
    }

}

//@Singleton
//class HomeVM @Inject constructor(){
//    private val _title = MutableStateFlow("")
//    val title = _title.asStateFlow()
//
//    fun currTitle(title: String){
//        _title.value = title
//    }
//}