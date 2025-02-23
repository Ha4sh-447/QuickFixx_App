package com.example.quickfixx.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.repository.Tutor.TutorRepo
import com.example.quickfixx.screens.auth.Electrician.ElectricianScreenState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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