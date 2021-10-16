package com.example.healthprotask.auth.usecase

import android.util.Log
import com.example.healthprotask.auth.model.DistanceResponse
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.repository.AuthRepository
import com.example.healthprotask.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DistanceUseCase @Inject constructor(private val authRepository: AuthRepository): BaseUseCase<DistanceUseCase.Param, DistanceResponse>() {
    val TAG = DistanceUseCase::class.java.simpleName
    
    data class Param(
        val bearerToken: String,
        val date: String
    )

    override suspend fun execute(param: Param): DistanceResponse {
        Log.d(TAG, "execute")
        return withContext(Dispatchers.IO) {
            authRepository.getDistance(
                param.bearerToken,
                param.date
            )
        }
    }
}