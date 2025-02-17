package com.example.quickfixx.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickfixx.repository.Repository
import com.example.quickfixx.repository.Tutor.TutorRepo
import com.example.quickfixx.screens.auth.Electrician.ElectricianScreenState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ElectricianViewModel @Inject constructor(
    private val repository: Repository,
    private val tutorRepo: TutorRepo,
    private val subject: Subject
): ViewModel() {


    private val currUser  = Firebase.auth.currentUser?.email.toString()
    private val _state = MutableStateFlow(ElectricianScreenState())
    val state = _state.asStateFlow()
    val title = subject.title

    init {
        getAllTutors()
    }

    fun currTitle(title: String){
        subject.currTitle(title)
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



//    fun getAllElectrician() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val electricians = async {
//                    repository.getAllElectrician()
//                }.await()
//
//                val acrepair = async {
//                    repository.getElectricianByACService()
//                }.await()
//
//                val tvrepair = async {
//                    repository.getElectricianByTVRepair()
//                }.await()
//
//                val circuit = async{
//                    repository.getElectricianByCircuit()
//                }.await()
//
//                _state.value = state.value.copy(
//                    data = electricians,
//                    acservice = acrepair,
//                    tvservice = tvrepair,
//                    circuitService = circuit
//                )
//            } catch (e: Exception) {
//                Log.e("ElectricianViewModel", "Error fetching post", e)
//                _state.value = state.value.copy(
//                    errorMsg = e.message
//                )
//            }
//        }
//    }

}