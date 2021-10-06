package com.example.healthprotask.auth.nework

import com.example.healthproclienttask.utility.NetworkUtility
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import retrofit2.http.*

interface AuthApiService {//application/x-www-form-urlencoded

    @FormUrlEncoded
    @POST(NetworkUtility.TOKEN_URL)
    suspend fun requestToken(
        @Header("Authorization") authorization: String,
        @Field("clientId") clientId: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): AccessTokenRequestResponse

    @GET
    suspend fun getUserProfile()
}