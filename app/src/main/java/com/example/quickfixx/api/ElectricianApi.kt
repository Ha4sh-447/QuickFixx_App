package com.example.quickfixx.api

import com.example.quickfixx.ViewModels.Subject
import com.example.quickfixx.domain.model.Electrician
import com.example.quickfixx.domain.model.Tutor
import com.example.quickfixx.domain.model.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ElectricianApi {
    @GET("electrician")
    suspend fun getAllElectrician(): Response<List<Electrician>>
    @POST("electrician")
    suspend fun saveElectrician(@Body elecBody: RequestBody)
    @GET("electrician/field/acService")
    suspend fun getElectricianByACService(): Response<List<Electrician>>
    @GET("electrician/field/tvRepair")
    suspend fun getElectricianByTVRepair(): Response<List<Electrician>>
    @GET("electrician/field/homeCircuit")
    suspend fun getElectricianByCircuit(): Response<List<Electrician>>
}

interface TutorsApi{
    @GET("tutors")
    suspend fun getAllTutors(): Response<List<Tutor>>
    @POST("tutors")
    suspend fun saveAsTutor(@Body tutor: RequestBody)
    @GET("tutors/subject")
    suspend fun getBySubject(@Query("subject") subject: String) : Response<List<Tutor>>
    @PUT("tutors/{id}")
    suspend fun updateTutorProfile(@Path("id") tutorId: String, @Body tutor: RequestBody)
    @GET("tutors/{id}")
    suspend fun getTutorById(@Path("id") tutorId: String): Tutor?
}