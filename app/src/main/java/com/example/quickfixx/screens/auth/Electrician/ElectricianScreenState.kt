package com.example.quickfixx.screens.auth.Electrician


import com.example.quickfixx.domain.model.Tutor

data class ElectricianScreenState(
    val microprocessorsTutors: List<Tutor>? = null,
    val dataStructuresTutors: List<Tutor>? = null,
    val mobileComputingTutors: List<Tutor>? = null,
    val engineeringMathsTutors: List<Tutor>? = null,
    val errorMsg: String?= null,
    val tutor: Tutor?= null
)
