package com.example.healthprotask.auth.model
// Sealed class
sealed class ResultData<out T> {
    // Type classes ,API call nu status batava ..like success, call failed, loading, exception etc
    data class Success<out T>(val data: T? = null) : ResultData<T>() // nullable ..cuz even after successful Api call ..Server pr data j na hoy to Empty response j aave
    data class Loading(val nothing: Nothing? = null) : ResultData<Nothing>()
    data class Failed(val message: String? = null) : ResultData<Nothing>()
    data class Exception(val message: String? = null) : ResultData<Nothing>()
}

// successful Api call thay to ..it will return data ..Model walo data ,but here we will keep it generic.
