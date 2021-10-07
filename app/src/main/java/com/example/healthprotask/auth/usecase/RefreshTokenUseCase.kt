package com.example.healthprotask.auth.usecase

import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.repository.AuthRepository
import com.example.healthprotask.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RefreshTokenUseCase @Inject constructor(private val authRepository: AuthRepository) :
    BaseUseCase<RefreshTokenUseCase.Param, AccessTokenRequestResponse>() {
    data class Param(
        val authorization: String,
        val grantType: String,
        val refreshToken: String
    )

    override suspend fun execute(param: Param): AccessTokenRequestResponse {
        return withContext(Dispatchers.IO) {
            authRepository.refreshToken(
                param.authorization,
                param.grantType,
                param.refreshToken
            )
        }
    }
}