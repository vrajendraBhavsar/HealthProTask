package com.example.healthproclienttask.utility

import android.media.session.MediaSession
//http://www.fitbit.com/oauth/authorize?oauth_token=%s
//https://www.fitbit.com/oauth2/authorize
//https://api.fitbit.com/1/user/-/profile.json
//https://api.fitbit.com/1/user/-/activities/date/2021-10-11.json
//https://api.fitbit.com/1/user/-/activities/list.json?beforeDate=2021-10-13&sort=desc&limit=5&offset=0

object NetworkUtility {
    const val BASE_URL = "https://api.fitbit.com/"
    const val AUTHORIZATION_URL = "authorize?oauth_token=%s"
    const val TOKEN_URL = "oauth2/token"
    const val USER_PROFILE = "1/user/-/profile.json"
//    const val USER_ACTIVITIES = "1/user/-/activities/date/{Date}.json"
    const val USER_ACTIVITIES = "1/user/-/activities/list.json"
    const val REQUEST_TOKEN_URL = "request_token"
    const val ACCESS_TOKEN_URL = "access_token"

    const val Client_ID = "23BKYF"
    const val Client_SECRET = "18e4ce49090198ccda31336db7914e2f"
    const val REDIRECT_URL = "https://www.mindinventory.com/"

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