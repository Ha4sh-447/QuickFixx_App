package com.example.quickfixx.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.HttpException
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.repository.Tutor.TutorRepo
import com.example.quickfixx.screens.auth.Electrician.ElectricianScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ElectricianViewModel @Inject constructor(
    private val tutorRepo: TutorRepo,
    private val subject: Subject
): ViewModel() {

    private val _state = MutableStateFlow(ElectricianScreenState())
    val state = _state.asStateFlow()
    val title = subject.title

    init {
        getAllTutors()
    }

    fun getAllTutors(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val microprocessorsTutors = async {
                    tutorRepo.getTutorsBySubject("microprocessors")
                }.await()

                val dataStructuresTutors = async {
                    tutorRepo.getTutorsBySubject("DS")
                }.await()

                val mobileComputingTutors = async {
                    tutorRepo.getTutorsBySubject("MC")
                }.await()

                val engineeringMathsTutors = async {
                    tutorRepo.getTutorsBySubject("Maths")
                }.await()

                // Storing all subjects into a new field in state
                _state.value = state.value.copy(
                    microprocessorsTutors = microprocessorsTutors,
                    dataStructuresTutors,
                    mobileComputingTutors,
                    engineeringMathsTutors

                )
            } catch (e: Exception) {
                Log.e("ElectricianViewModel", "Error fetching post", e)
                _state.value = state.value.copy(
                    errorMsg = e.message
                )
            }
        }
    }

    fun updateSelectedTutor(tutor: Tutor) {
        _state.update { currentState ->
            currentState.copy(tutor = tutor)
        }
    }

    suspend fun getTutorById(tutorId: String): Tutor? {
        return try {
            var tutor = tutorRepo.getTutorById(tutorId)

            // If tutor is null, wait a bit and retry (fixes race condition)
            if (tutor == null) {
                delay(500) // Wait 500ms before retrying
                tutor = tutorRepo.getTutorById(tutorId)
            }

            tutor?.let {
                Log.d("Tutor", it.name)
                _state.update { currentState -> currentState.copy(tutor = it) }
            }
            tutor
        } catch (e: HttpException) {
            if (e.code() == 500) {
                null // Return null if tutor is not found
            } else {
                throw e // Re-throw the exception for other HTTP errors
            }
        } catch (e: Exception) {
            Log.e("ElectricianViewModel", "Error fetching tutor by ID", e)
            null
        }
    }


    fun saveTutor(name:String, uid: Int, contact: String, email: String, subject: String, fees: Int, bio: String, experience: Int, availability: String, image: String){
        val tutorBody = Tutor(name, uid, contact, email, subject, fees, 0f, bio, experience, availability, image)
        Log.d("INFO FROM SAVE TUTOR", tutorBody.name)

        viewModelScope.launch {
            try {
                tutorRepo.saveAsTutor(tutorBody.convertToJson())
                Log.d("INFO", "REQ made")
                _state.update {
                    it.copy(
                        tutor = tutorBody
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMsg = e.message
                    )
                }
            }
        }
    }
}