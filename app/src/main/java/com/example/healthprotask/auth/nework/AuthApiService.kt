package com.example.healthproclienttask.auth.nework

import com.example.healthproclienttask.utility.NetworkUtility
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {
    @POST(NetworkUtility.AUTHORIZATION_URL)
    suspend fun authorizationRequest(
//        @Query("oauth_token") oAuth: String
    ): String
}