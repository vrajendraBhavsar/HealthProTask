package com.example.healthprotask.auth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthprotask.auth.usecase.AuthUseCase
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {
//Backing pattern
    private val _accessTokenRequestResponseLiveData = MutableLiveData<ResultData<AccessTokenRequestResponse>>()
    val accessTokenRequestResponseLiveData: LiveData<ResultData<AccessTokenRequestResponse>> = _accessTokenRequestResponseLiveData

    @DelicateCoroutinesApi
    fun requestToken(
        authorization: String,
        clientId: String,
        grantType: String,
        redirectUri: String,
        code: String
    ) {
        viewModelScope.launch() {
            val accessTokenRequestResponse = authUseCase.execute(AuthUseCase.Param(authorization, clientId, grantType, redirectUri, code))
            if (accessTokenRequestResponse.access_token?.isNotEmpty() == true){
                _accessTokenRequestResponseLiveData.value = ResultData.Success(accessTokenRequestResponse)
            } else {
                ResultData.Failed(accessTokenRequestResponse.errors?.get(0)?.message)
            }
        }
    }
}