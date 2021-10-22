package com.example.healthprotask.utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import com.example.healthprotask.R
import com.example.healthprotask.auth.AuthActivity
import javax.inject.Inject

public class SessionManager @Inject constructor() {
    private val TAG: String = SessionManager::class.java.simpleName
    lateinit var pref : SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    lateinit var context: Context
    var PRIVATE_MODE: Int = Context.MODE_PRIVATE

    @SuppressLint("CommitPrefEdits")
    constructor(context: Context) : this() {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    companion object{
        const val PREF_NAME: String = "HealthProPref"
        const val IS_LOGIN: String = "isLoggedIn"
        const val BEARER_TOKEN: String = "bearerToken"
    }
    /**
     * to create session
    **/
    fun createAuthSession(bearerToken: String){
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(BEARER_TOKEN, bearerToken)
        Log.d(TAG, "sessionManager: bearer: $bearerToken ")
        editor.commit()
    }
    /**
     * check login status
     **/
    fun checkLogin(){
        if (!this.isLoggedIn()){
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
//            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_webViewFragment)

            context.applicationContext
        }
    }
    /**
     * get log in status
     * */
    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    /**
     * get bearer token from shared preference
     * */
    fun getBearerToken(): String? {
        val token = pref.getString(BEARER_TOKEN, null)
        return token
    }
    /**
     * log out user
     * */
    fun logoutUser(){
        editor.clear()
        editor.commit()

        val intent = Intent(context, AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}