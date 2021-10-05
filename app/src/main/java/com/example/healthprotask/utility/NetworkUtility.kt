package com.example.healthproclienttask.utility

import android.media.session.MediaSession
//http://www.fitbit.com/oauth/authorize?oauth_token=%s
//https://www.fitbit.com/oauth2/authorize
object NetworkUtility {
    const val BASE_URL = "https://www.fitbit.com/oauth2/"
    const val AUTHORIZATION_URL = "authorize?oauth_token=%s"
    const val REQUEST_TOKEN_URL = "request_token"
    const val ACCESS_TOKEN_URL = "access_token"

    const val Client_ID = "23BKYF"
    const val Client_SECRET = "18e4ce49090198ccda31336db7914e2f"

//    fun getRequestTokenEndpoint(): String? {
//        return "http://api.fitbit.com/oauth/request_token"
//    }

//    fun getAccessTokenEndpoint(): String? {
//        return "http://api.fitbit.com/oauth/access_token"
//    }

//    fun getAuthorizationUrl(requestToken: MediaSession.Token): String? {
//        return java.lang.String.format(AUTHORIZATION_URL, requestToken.get)
//    }
}