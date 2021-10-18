package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.healthproclienttask.utility.NetworkUtility
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.ResultData
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentWebViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var accessToken: String? = ""
    private var currentDay: String? = null
    private var bearerToken: String? = null
    private var refreshToken: String? = null
    lateinit var binding: FragmentWebViewBinding
    private val TAG = WebViewFragment::class.java.simpleName
    private val url =
        "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"
//    private val url = "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"

    var redirect: Boolean = false
    var completelyLoaded: Boolean = true   //when page is loaded completely ..it will be true

    private var PRIVATE_MODE = Context.MODE_PRIVATE
    private val PREF_NAME = "bearer-token"
    private val TOKEN_KEY: String = "bearer-token-key"

    private val authViewModel by viewModels<AuthViewModel>() //vm

    companion object {
        fun newInstance() = WebViewFragment()
        const val REQUEST_KEY = "request_key"
        const val DATA_KEY = "data_key"
        const val GRANT_TYPE = "authorization_code"
        const val REFRESH_GRANT_TYPE = "refresh_token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebViewBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //...method reference
        authViewModel.userProfileResponseLiveData.observe(viewLifecycleOwner, ::handleUserProfile)
        authViewModel.accessTokenRequestResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRequest)
        authViewModel.accessTokenRefreshResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRefresh)
        authViewModel.userActivitiesResponseLiveData.observe(viewLifecycleOwner, ::handleUserActivity)
        //....
        binding.wbWebView.webViewClient = object : WebViewClient() {
            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!completelyLoaded) {
                    redirect = true
                }
                completelyLoaded = false
                //loading url

                view?.apply {
                    url?.let { loadUrl(it) }
                    settings.javaScriptEnabled = true
                }
                Log.d(TAG, "shouldOverrideUrlLoading1: $url")
//                Log.d(TAG, "htmlContent: $htmlContent")
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d(TAG, "shouldOverrideUrlLoading: started")
                if (!completelyLoaded) {
                    redirect = true
                }
                completelyLoaded = false
                //loading url
                view?.apply {
                    url?.let { loadUrl(it) }
                    settings.javaScriptEnabled = true
                }
                Log.d(TAG, "shouldOverrideUrlLoading2: ${request?.url}")
                Log.d(TAG, "shouldOverrideUrlLoading2: ended")
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.d(TAG, "onPageStarted: started")
                completelyLoaded = false
                binding.progressbar.visibility = View.VISIBLE   //while loading data..its visible
//                Log.d(TAG, "onPageStarted: $completelyLoaded")
                Log.d(TAG, "onPageStarted: ended")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d(TAG, "onPageFinished: started")
                if (!redirect) {
                    completelyLoaded = true
                }
                if (completelyLoaded && !redirect) { //means page is completely loaded
                    Log.d(TAG, "onPageFinished: $completelyLoaded")
                    if (!url.isNullOrEmpty()) {
                        Log.d(TAG, "onPageFinished: url link : $url")
                        val uri: Uri = Uri.parse(url)
                        val code: String? = uri.getQueryParameter("code")
                        Log.d(TAG, "onPageFinished: code = $code")

                            val base64 = getBase64("${NetworkUtility.Client_ID}:${NetworkUtility.Client_SECRET}")
                            Log.d(TAG, "getBase64: $base64")
                            val authorizationString = "Basic $base64"
                            Log.d(TAG, "authoriaztion String ..Basic XOXO : $authorizationString")

                            /**
                             *  Token api req
                             **/
                            if (code != null && accessToken == "") {
                                authViewModel.requestToken(
                                    authorization = authorizationString,
                                    clientId = NetworkUtility.Client_ID,
                                    grantType = GRANT_TYPE,
                                    redirectUri = NetworkUtility.REDIRECT_URL,
                                    code = code
                                )
                            }else {
                                Log.d(TAG, "onPageFinished: code is empty")
                            }
                    }
                } else {
                    redirect = false
                }
                Log.d(TAG, "onPageFinished: finished")
            }
        }
        //Mention chrome latest version...
        binding.wbWebView.settings.userAgentString =
            "Chrome/94.0.4606.71 Mobile"   //to avoid possible errors from occurring in latest versions
        binding.wbWebView.loadUrl(url)
    }

    private fun handleUserActivity(userActivitiesResponse: UserActivitiesResponse?) {
        //going back to login fragment
        if (userActivitiesResponse != null){
            val navController = findNavController()
            navController.previousBackStackEntry?.savedStateHandle?.set("userActivitiesResponse", userActivitiesResponse)
            navController.popBackStack()
            Toast.makeText(requireContext(), "Authorized successfully", Toast.LENGTH_SHORT).show()
        }
//        Toast.makeText(requireContext(), "User Data : ${userActivitiesResponse.toString()}", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onPageFinished: userProfileData : ${userActivitiesResponse.toString()}")
    }

    private fun handleAccessTokenRefresh(resultData: ResultData<AccessTokenRequestResponse>?) {
        when(resultData) {
            is ResultData.Success -> {
                val tokenRefreshResponse : AccessTokenRequestResponse? = resultData.data
                bearerToken = "Bearer ${tokenRefreshResponse?.access_token}"
                /**
                 *  User profile api call
                 **/
//              getUserProfile(bearerToken)
            }
            is ResultData.Failed -> {
                resultData.message
            }
            is ResultData.Exception -> {}
            is ResultData.Loading -> {}
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun handleAccessTokenRequest(resultData: ResultData<AccessTokenRequestResponse>?) {
        when (resultData) {
            is ResultData.Loading -> {
            }
            is ResultData.Success -> {
                val accessTokenRequestResponse: AccessTokenRequestResponse? = resultData.data
                Log.d(TAG, "ResultData.Success: ${accessTokenRequestResponse.toString()}")

                //retrieve access-token and refresh-token from response
                accessToken = accessTokenRequestResponse?.access_token
                refreshToken = accessTokenRequestResponse?.refresh_token
                Log.d(TAG, "accessToken: $accessToken")
                Log.d(TAG, "refreshToken: $refreshToken")
//              val userProfileResponse: ProfileResponse? = accessToken?.let { getUserProfile(it) } //to use common fun for user api call

                val bearerToken = "Bearer $accessToken"
                Log.d(TAG, "bearerToken: $bearerToken")
                currentDay = SimpleDateFormat("yyyy-MM-dd").format(Date())
                Log.d(TAG, "handleAccessTokenRequest: currentDay : $currentDay")

                //Shared Preference
                val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
                val editor = sharedPref.edit()
                editor.putString(TOKEN_KEY, bearerToken)
                editor.apply()

                /**
                 *  user activities api
                 **/
                    currentDay?.let { date ->
                        authViewModel.getUserActivities(bearerToken = bearerToken, beforeDate = date, sort = "desc",limit = 5,offset = 0, next = "", previous = "")
//                        getUserActivities(date, "", "")
                }
            }
            is ResultData.Failed -> {
                Log.d(TAG, "ResultData.Failed: ${resultData.message}")
            }
            is ResultData.Exception -> {
            }
        }
    }

    @DelicateCoroutinesApi
    private fun handleUserProfile(profileResponse: ProfileResponse?) {
        //when accessTokenRequestResponse is failed and need to refresh access token ,use Refresh token
        if (profileResponse?.success == false) {
            /**
             *  Refresh token api
             **/
            refreshToken?.let { refreshToken ->
                bearerToken?.let { bearerToken ->
                    authViewModel.refreshToken(
                        authorization = bearerToken,
                        grantType = REFRESH_GRANT_TYPE,
                        refreshToken = refreshToken
                    )
                }
            }
        }
        Toast.makeText(requireContext(), "User Data : ${profileResponse.toString()}", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onPageFinished: userProfileData : ${profileResponse.toString()}")
    }

    fun getUserActivities(bearerToken: String, currentDay: String, next: String?, previous: String?) {
        Log.d(TAG, "getUserProfile: started")
//        var userActivitiesResponse: UserActivitiesResponse? = null
        /**
         *  user activities api
         **/
        bearerToken?.let { authViewModel.getUserActivities(bearerToken = it, beforeDate = currentDay, sort = "desc",limit = 5,offset = 0, next = next, previous = previous) }
        Log.d(TAG, "getUserProfile: finished")
//        return userActivitiesResponse
    }

    private fun getBase64(s: String): String? {
        Log.d(TAG, "getBase64: started")
        var data = ByteArray(0)
        try {
            data = s.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        } finally {
            Log.d(TAG, "getBase64: ended")
            return Base64.encodeToString(data, Base64.NO_WRAP)
        }
    }
}