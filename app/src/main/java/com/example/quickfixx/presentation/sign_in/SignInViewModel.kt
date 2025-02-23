package com.example.quickfixx.presentation.sign_in
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickfixx.domain.model.User

import com.example.quickfixx.repository.UserRepository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    val repo: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    suspend fun getUserByEmail(email: String): User? {
        return try {
            var user = repo.getByEmail(email)

            // If user is null, wait a bit and retry (fixes race condition)
            if (user == null) {
                delay(500) // Wait 500ms before retrying
                user = repo.getByEmail(email)
            }

            user?.let {
                Log.d("user", it.name)
                _state.update { currentState -> currentState.copy(user = it) }
            }
            user
        } catch (e: HttpException) {
            if (e.code() == 500) {
                null // Return null if user is not found
            } else {
                throw e // Re-throw the exception for other HTTP errors
            }
        }
    }


    fun getUser(email : String){
        viewModelScope.launch {
            try{
                val user = async{
                    repo.getByEmail(email)
                }.await()
                _state.value = state.value.copy(
                    user = user
                )
            } catch (e: HttpException){
                if (e.code() == 500) {
                    null // User not found
                } else {
                    throw e // Re-throw the exception for other HTTP errors
                }
            }
        }
    }

  fun saveUser(name: String, email: String, password:String, role:String, contact : String, image: String){
        val userBody = User("",name, email, password, contact, role, image)
        viewModelScope.launch {
            repo.saveUser(userBody = userBody.convertToJson())
        }
      _state.update {
          it.copy(
              isSignInSuccessful = userBody != null,
              signInError = null,
              user = userBody
          )
      }
      Log.d("INFO FROM SAVE USER", "User state updated")
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}