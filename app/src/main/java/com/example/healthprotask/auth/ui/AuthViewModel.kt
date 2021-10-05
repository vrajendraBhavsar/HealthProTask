package com.example.healthproclienttask.auth.ui

import androidx.lifecycle.ViewModel
import com.example.healthproclienttask.auth.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authUseCase: AuthUseCase) : ViewModel() {

    private var tokenString = ""

    @DelicateCoroutinesApi
    fun authorizationRequest(): String {
        GlobalScope.launch(Dispatchers.IO) {
            tokenString = authUseCase.authorizationRequest()
        }
        return tokenString
    }
}