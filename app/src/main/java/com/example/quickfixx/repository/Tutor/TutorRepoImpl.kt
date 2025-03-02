package com.example.quickfixx.repository.Tutor

import com.example.quickfixx.api.TutorsApi
import com.example.quickfixx.domain.model.Tutor
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class TutorRepoImpl @Inject constructor (
    private val api: TutorsApi
) : TutorRepo{
    override suspend fun getAllTutors(): List<Tutor>? {
        return api.getAllTutors().body()
    }

    override suspend fun getTutorsBySubject(subject: String): List<Tutor>? {
        return api.getBySubject(subject).body()
    }

    override suspend fun saveAsTutor(tutorBody: RequestBody) {
        api.saveAsTutor(tutorBody)
    }

    override suspend fun updateTutorProfile(tutorid: String, tutorBody: RequestBody) {
        api.updateTutorProfile(tutorid, tutorBody)
    }

    override suspend fun getTutorById(tutorid: String): Tutor? {
        return api.getTutorById(tutorid)
    }

}