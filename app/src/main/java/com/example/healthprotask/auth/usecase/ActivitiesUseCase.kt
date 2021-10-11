package com.example.healthprotask.auth.usecase

import android.util.Log
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.repository.AuthRepository
import com.example.healthprotask.base.BaseUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ActivitiesUseCase @Inject constructor(private val authRepository: AuthRepository): BaseUseCase<ActivitiesUseCase.Param, UserActivitiesResponse>() {
    val TAG = ActivitiesUseCase::class.java.simpleName
    
    data class Param(
        val bearerToken: String,
        val date: String
    )

    override suspend fun execute(param: Param): UserActivitiesResponse {
        Log.d(TAG, "execute")
        return withContext(Dispatchers.IO) {
            authRepository.getUserActivities(
                param.bearerToken,
                param.date
            )
        }
    }
}