package com.example.healthprotask.auth.nework

import com.example.healthproclienttask.utility.NetworkUtility
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import retrofit2.http.*
import java.util.*

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

    @GET(NetworkUtility.USER_PROFILE)
    suspend fun getUserProfile(
        @Header("Authorization") bearerToken: String
    ): ProfileResponse

    @GET(NetworkUtility.USER_ACTIVITIES)    //https://api.fitbit.com/1/user/-/activities/list.json?beforeDate=2021-10-13&sort=desc&limit=5&offset=0
    suspend fun getUserActivities(
        @Header("Authorization") bearerToken: String,
        @Query("beforeDate") beforeDate: String,
        @Query("sort") sort: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("next") next: String?,
        @Query("previous") previous: String?
    ): UserActivitiesResponse

    @FormUrlEncoded
    @POST(NetworkUtility.TOKEN_URL)
    suspend fun refreshToken(
        @Header("Authorization") authorization: String,     //Basic MjNCS1lGOjE4ZTRjZTQ5MDkwMTk4Y2NkYTMxMzM2ZGI3OTE0ZTJm
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String,
    ): AccessTokenRequestResponse
}