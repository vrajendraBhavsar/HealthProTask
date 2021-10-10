package com.example.healthprotask.auth.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ResultData
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.usecase.AuthUseCase
import com.example.healthprotask.auth.usecase.ProfileUseCase
import com.example.healthprotask.auth.usecase.RefreshTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val profileUseCase: ProfileUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) : ViewModel() {
    val TAG = AuthViewModel::class.java.simpleName
    
    //Backing property
    private val _accessTokenRequestResponseLiveData = MutableLiveData<ResultData<AccessTokenRequestResponse>>()
    val accessTokenRequestResponseLiveData: LiveData<ResultData<AccessTokenRequestResponse>> = _accessTokenRequestResponseLiveData

    private val _userActivitiesResponseLiveData = MutableLiveData<UserActivitiesResponse>()
    val userActivitiesResponseLiveData: LiveData<UserActivitiesResponse> = _userActivitiesResponseLiveData

    private val _accessTokenRefreshResponseLiveData = MutableLiveData<ResultData<AccessTokenRequestResponse>>()
    val accessTokenRefreshResponseLiveData: LiveData<ResultData<AccessTokenRequestResponse>> = _accessTokenRefreshResponseLiveData



    @DelicateCoroutinesApi
    fun requestToken(
        authorization: String,
        clientId: String,
        grantType: String,
        redirectUri: String,
        code: String,
        codeVerifier: String
    ) {
        Log.d(TAG, "requestToken: started")
        viewModelScope.launch {
            val accessTokenRequestResponse = authUseCase.execute(AuthUseCase.Param(authorization, clientId, grantType, redirectUri, code, codeVerifier))
            if (accessTokenRequestResponse.access_token?.isNotEmpty() == true) {
                _accessTokenRequestResponseLiveData.value = ResultData.Success(accessTokenRequestResponse)
            } else {
                ResultData.Failed(accessTokenRequestResponse.errors?.get(0)?.message)
            }
        }
        Log.d(TAG, "requestToken: ended")
    }

    fun getUserActivities(bearerToken: String) {
        Log.d(TAG, "getUserProfile: started")
        viewModelScope.launch {
            val userProfileResponse: UserActivitiesResponse = profileUseCase.execute(ProfileUseCase.Param(bearerToken))
                _userActivitiesResponseLiveData.value = userProfileResponse
            }
        Log.d(TAG, "getUserProfile: ended")
    }

    @DelicateCoroutinesApi
    fun refreshToken(
        authorization: String,
        grantType: String,
        refreshToken: String
    ) {
        Log.d(TAG, "refreshToken: started")
        viewModelScope.launch {
            val accessTokenRefreshResponse = refreshTokenUseCase.execute(RefreshTokenUseCase.Param(authorization, grantType, refreshToken))
            if (accessTokenRefreshResponse.access_token?.isNotEmpty() == true) {
                _accessTokenRefreshResponseLiveData.value = ResultData.Success(accessTokenRefreshResponse)
            } else {
                ResultData.Failed(accessTokenRefreshResponse.errors?.get(0)?.message)
            }
        }
        Log.d(TAG, "refreshToken: ended")
    }
}