package com.example.healthproclienttask.auth.repository

import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.nework.AuthApiService
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    suspend fun requestToken(
        authorization: String,
        clientId: String,
        grantType: String,
        redirectUri: String,
        code: String
    ): AccessTokenRequestResponse{
        return authApiService.requestToken(authorization, clientId, grantType, redirectUri, code)
    }
}