package com.example.healthprotask.auth.usecase

import android.util.Log
import com.example.healthprotask.auth.repository.AuthRepository
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthUseCase @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<AuthUseCase.Param, AccessTokenRequestResponse>() {
    val TAG = AuthUseCase::class.java.simpleName

    data class Param(
        val authorization: String,
        val clientId: String,
        val grantType: String,
        val redirectUri: String,
        val code: String,
        val codeVerifier: String
    )

    override suspend fun execute(param: Param): AccessTokenRequestResponse {
        Log.d(TAG, "execute: ")
        return withContext(Dispatchers.IO) {
            authRepository.requestToken(
                param.authorization,
                param.clientId,
                param.grantType,
                param.redirectUri,
                param.code,
                param.codeVerifier
            )
        }
    }
}