package com.example.healthproclienttask.auth.repository

import com.example.healthproclienttask.auth.nework.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun authorizationRequest(): String {
        return authApiService.authorizationRequest()
    }
}