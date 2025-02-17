package com.example.quickfixx.repository

import com.example.quickfixx.repository.Tutor.TutorRepo
import com.example.quickfixx.repository.Tutor.TutorRepoImpl
import com.example.quickfixx.repository.UserRepository.UserRepository
import com.example.quickfixx.repository.UserRepository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindElectricianApiRepo(
        electricianApiRepo :RepositoryImpl
    ): Repository

    @Binds
    @Singleton
    abstract fun bindTutorApiRepo(
        tutorApiRepo :TutorRepoImpl
    ): TutorRepo

    @Binds
    @Singleton
    abstract fun bindUserApiRepo(
        userApiRepo: UserRepositoryImpl
    ): UserRepository
}