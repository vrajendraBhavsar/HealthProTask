package com.example.healthproclienttask

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import java.lang.Exception
import java.lang.RuntimeException

class FitbitAuthAppliaction: Application() {
    private val CLIENT_SECRET = "18e4ce49090198ccda31336db7914e2f"
    private val SECURE_KEY = "CVPdQNAT6fBI4rrPLEn9x0+UV84DoqLFiNHpKOPLRW0="

    override fun onCreate() {
        super.onCreate()
    }

//    fun generateAuthenticationConfiguration(
//        context: Context,
//        mainActivityClass: Class<out Activity?>?
//    ): AuthenticationConfiguration? {
//        return try {
//            val ai = context.packageManager.getApplicationInfo(
//                context.packageName,
//                PackageManager.GET_META_DATA
//            )
//            val bundle = ai.metaData
//
//            // Load clientId and redirectUrl from application manifest
//            val clientId = bundle.getString("com.fitbit.healthproclienttask.CLIENT_ID")
//            val redirectUrl = bundle.getString("com.fitbit.healthproclienttask.REDIRECT_URL")
//            val CLIENT_CREDENTIALS = ClientCredentials(
//                clientId,
//                com.fitbit.healthproclienttask.FitbitAuthApplication.CLIENT_SECRET,
//                redirectUrl
//            )
//            AuthenticationConfigurationBuilder()
//                .setClientCredentials(CLIENT_CREDENTIALS)
//                .setEncryptionKey(com.fitbit.healthproclienttask.FitbitAuthApplication.SECURE_KEY)
//                .setTokenExpiresIn(2592000L) // 30 days
//                .setBeforeLoginActivity(Intent(context, mainActivityClass))
//                .addRequiredScopes(Scope.profile, Scope.settings)
//                .addOptionalScopes(activity, Scope.weight)
//                .setLogoutOnAuthFailure(true)
//                .build()
//        } catch (e: Exception) {
//            throw RuntimeException(e)
//        }
//    }
}