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

data class Tutor(
    val id: Int = 0,
    val name: String,
    val uid: Int,
    val contact: String,
    val email: String,
    val subject: String,
    val fees: Int,
    val rating: Float,
    val bio: String,
    var earnings: Int,
    val experience: Int,
    val availability: String,
    val image: String,
    val role: String // Added role field
) {
    fun convertToJson(): RequestBody {
        val jsonObj = JSONObject().apply {
            put("name", name)
            put("uid", uid)
            put("contact", contact)
            put("email", email)
            put("subject", subject)
            put("fees", fees)
            put("rating", rating)
            put("bio", bio)
            put("earnings", earnings)
            put("experience", experience)
            put("availability", availability)
            put("image", image)
            put("role", role) // Include role in JSON request
        }

        return jsonObj.toString().toRequestBody("application/json".toMediaTypeOrNull())
    }
}
