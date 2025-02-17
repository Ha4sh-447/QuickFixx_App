package com.example.quickfixx.domain.model

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

data class Electrician(
    val name : String,
    val id: Int,
    val contact: String,
    val location: String,
    val experience: String,
    val qualitfication: Array<String>,
    val rating: Float
){
    fun convertToJson(): RequestBody? {
//        val jsonObj = JSONObject()
//        jsonObj.put("name", name)
//        jsonObj.put("email" , email)
//        jsonObj.put("password" ,password)
//        jsonObj.put("contact" , contact)
//        jsonObj.put("role" ,role)
//        jsonObj.put("image" ,image)

//        return jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
        return null
    }
}
///"id": 4,
//"name": "Nigahm R",
//"contact": 9998599985,
//"location": "Mulund",
//"address": "Mulund, Central Line, Home",
//"experience": "5 years",
//"qualification": ["Gove

data class Tutor(
    val name: String,
    val uid: Int,
    val contact: String,
    val email: String,
    val subject: String,
    val fees: Int,
    val rating: Float,
    val bio: String,
    val experience: String,
    val availability: String,
    val image: String
){
    fun convertToJson(): RequestBody {
        val jsonObj = JSONObject()
        jsonObj.put("name", name)
        jsonObj.put("uid", uid)
        jsonObj.put("contact", contact)
        jsonObj.put("email", email)
        jsonObj.put("subject", subject)
        jsonObj.put("fees", fees)
        jsonObj.put("rating", rating)
        jsonObj.put("bio", bio)
        jsonObj.put("experience", experience)
        jsonObj.put("availability", availability)
        jsonObj.put("image", image)

        return jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }

}
