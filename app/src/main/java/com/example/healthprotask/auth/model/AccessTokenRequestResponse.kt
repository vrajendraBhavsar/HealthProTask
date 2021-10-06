package com.example.healthprotask.auth.model

data class AccessTokenRequestResponse(
    val access_token: String?,
    val expires_in: Int?,
    val refresh_token: String?,
    val scope: String?,
    val token_type: String?,
    val user_id: String?,
//to handle error case
    val errors: List<Error>?,
    val success: Boolean?
) {
    data class Error(
        val errorType: String?,
        val message: String?
    )
}