package com.example.quickfixx.repository

import com.example.quickfixx.api.ElectricianApi
import com.example.quickfixx.api.TutorsApi
import com.example.quickfixx.domain.model.Electrician
import com.example.quickfixx.domain.model.Tutor
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: ElectricianApi,
) : Repository{

    override suspend fun getAllElectrician(): List<Electrician>?{
        return api.getAllElectrician().body()
    }

    override suspend fun getElectricianByACService(): List<Electrician>? {
        return api.getElectricianByACService().body()
    }

    override suspend fun getElectricianByTVRepair(): List<Electrician>? {
        return api.getElectricianByTVRepair().body()
    }

    override suspend fun getElectricianByCircuit(): List<Electrician>? {
        return api.getElectricianByCircuit().body()
    }
}