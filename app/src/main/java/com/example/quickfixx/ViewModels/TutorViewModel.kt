package com.example.quickfixx.ViewModels

import com.example.quickfixx.repository.Tutor.TutorRepo
import javax.inject.Inject

class TutorViewModel @Inject constructor(
    private val tutorRepo: TutorRepo
) {
}