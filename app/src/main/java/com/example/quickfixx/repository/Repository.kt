package com.example.quickfixx.repository

import com.example.quickfixx.domain.model.Electrician
import com.example.quickfixx.domain.model.Tutor
import retrofit2.Response

interface Repository {
    suspend fun getAllElectrician(): List<Electrician>?
    suspend fun getElectricianByACService(): List<Electrician>?
    suspend fun getElectricianByTVRepair(): List<Electrician>?
    suspend fun getElectricianByCircuit(): List<Electrician>?

}