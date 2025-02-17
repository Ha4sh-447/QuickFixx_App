package com.example.quickfixx.repository.Tutor

import android.view.PixelCopy.Request
import com.example.quickfixx.domain.model.Tutor
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Path

interface TutorRepo {
    suspend fun getAllTutors(): List<Tutor>?
    suspend fun getTutorsBySubject(subject: String): List<Tutor>?
    suspend fun saveAsTutor(@Body tutorBody: RequestBody)
    suspend fun updateTutorProfile(@Path("id") tutorid: String, @Body tutorBody: RequestBody)
}