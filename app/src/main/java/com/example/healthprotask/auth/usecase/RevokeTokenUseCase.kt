package com.example.healthprotask.auth.usecase

import com.example.healthprotask.base.BaseUseCase

class RevokeTokenUseCase {
    data class Param(
        val authorization: String,
        val grantType: String,
        val refreshToken: String
    )
}