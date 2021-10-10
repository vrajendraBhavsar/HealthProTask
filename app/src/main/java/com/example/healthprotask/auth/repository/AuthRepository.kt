package com.example.healthprotask.auth.repository

import android.util.Log
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.nework.AuthApiService
import retrofit2.http.Field
import retrofit2.http.Header
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService) {
    val TAG = AuthRepository::class.java.simpleName
    
    suspend fun requestToken(
        authorization: String,
        clientId: String,
        grantType: String,
        redirectUri: String,
        code: String,
        codeVerifier: String
    ): AccessTokenRequestResponse {
        Log.d(TAG, "requestToken: ")
        return authApiService.requestToken(authorization, clientId, grantType, redirectUri, code, codeVerifier)
    }

    suspend fun getUserActivities(
        bearerToken: String
    ): UserActivitiesResponse {
        Log.d(TAG, "getUserProfile: ")
        return authApiService.getUserActivities(bearerToken)
    }

    suspend fun refreshToken(
        authorization: String,
        grantType: String,
        refreshToken: String
    ): AccessTokenRequestResponse {
        Log.d(TAG, "refreshToken: ")
        return authApiService.refreshToken(authorization, grantType, refreshToken)
    }
}