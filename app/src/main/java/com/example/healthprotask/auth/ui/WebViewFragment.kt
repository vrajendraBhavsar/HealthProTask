package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.healthproclienttask.auth.ui.LoginFragment
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentWebVeiwBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var bearerToken: String? = null
    private var refreshToken: String? = null
    lateinit var binding: FragmentWebVeiwBinding
    private val TAG = WebViewFragment::class.java.simpleName
//    private val url =
////        "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity"
//"https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"
//    private val url: String = "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&code_challenge_method=S256&scope=activity%20nutrition%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight"

    //implicit grant flow -> we will directly get access_token
    private val url: String = "https://www.fitbit.com/oauth2/authorize?response_type=token&client_id=23BKYF&redirect_uri=https://www.mindinventory.com/&scope=activity%20nutrition%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"
    var redirect: Boolean = false
    var completelyLoaded: Boolean = true   //when page is loaded completely ..it will be true

    private val authViewModel by viewModels<AuthViewModel>() //vm

    companion object {
        fun newInstance() = WebViewFragment()
        const val REQUEST_KEY = "request_key"
        const val DATA_KEY = "data_key"
        const val REFRESH_GRANT_TYPE = "refresh_token"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWebVeiwBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //...method reference
        authViewModel.userActivitiesResponseLiveData.observe(viewLifecycleOwner, ::handleUserActivities)
//        authViewModel.accessTokenRequestResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRequest)
//        authViewModel.accessTokenRefreshResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRefresh)
        //....
        binding.wbWebView.webViewClient = object : WebViewClient() {
            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!completelyLoaded) {
                    redirect = true
                }
                completelyLoaded = false

                view?.apply {
                    url?.let { loadUrl(it) }
                    settings.javaScriptEnabled = true
                }
                Log.d(TAG, "shouldOverrideUrlLoading1: $url")
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
                            val accessToken: String? = uri.getQueryParameter("token")
                            Log.d(TAG, "onPageFinished: access_token = $accessToken")

                            //Bearer token
                            bearerToken = "Bearer $accessToken"
//                            bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM0JLWUYiLCJzdWIiOiI5TUZQNFAiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJ3YWN0IiwiZXhwIjoxNjM0NDcxNDQzLCJpYXQiOjE2MzM4NjY2NDN9.i0vnsriJov-UFdxcdW-ouS-6r1NCMKq_apNs11KTHWg"

                        bearerToken?.let { bearerToken ->
                            Log.d(TAG, "onPageFinished: Bearer token = $bearerToken")
                            authViewModel.getUserActivities(bearerToken = bearerToken)
                        }
                    }
                } else {
                    redirect = false
                }
                Log.d(TAG, "onPageFinished: finished")
            }
        }
        //Mention chrome latest version...
        binding.wbWebView.settings.userAgentString = "Chrome/94.0.4606.71 Mobile"   //to avoid possible errors from occurring in latest versions
        //loading url
        binding.wbWebView.loadUrl(url)
    }

    private fun handleUserActivities(userActivitiesResponse: UserActivitiesResponse?) {
        Toast.makeText(requireContext(), "User Data : ${userActivitiesResponse.toString()}", Toast.LENGTH_LONG).show()
        Log.d(TAG, "onPageFinished: UserData : ${userActivitiesResponse.toString()}")
    }

    @DelicateCoroutinesApi
    private fun handleUserProfile(profileResponse: ProfileResponse?) {
        //when accessTokenRequestResponse is failed and need to refresh access token ,use Refresh token
//        if (profileResponse?.success == false){
//                /**
//                 *  Refresh token api
//                 **/
//                    authViewModel.refreshToken(
//                        authorization = bearerToken,
//                        grantType = REFRESH_GRANT_TYPE,
//                        refreshToken = refreshToken
//                    )
//                }
//            }
//        }
        Toast.makeText(requireContext(), "User Data : ${profileResponse.toString()}", Toast.LENGTH_LONG).show()
        Log.d(TAG, "onPageFinished: UserData : ${profileResponse.toString()}")
    }

//    private fun getUserProfile(accessToken: String): ProfileResponse? {
//        Log.d(TAG, "getUserProfile: started")
//        val profileResponseVar: ProfileResponse? = null
//        //making Profile api req
//        bearerToken = "Bearer $accessToken"
//        //val bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM0JLWUYiLCJzdWIiOiI5TUZQNFAiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJzZXQgcmFjdCBybG9jIHJ3ZWkgcmhyIHJwcm8gcm51dCByc2xlIiwiZXhwIjoxNjMzNTQ5OTc2LCJpYXQiOjE2MzM1MjExNzZ9.3g0rWW39QlZ8Lq_owLEPPqd_jxKqGIrF4ltao15xe84"
//
//            /**
//             *  Refresh token api
//             **/
//        bearerToken?.let { bearerToken ->
//            authViewModel.getUserProfile(bearerToken = bearerToken)
//        }
//        Log.d(TAG, "getUserProfile: finished")
//        return profileResponseVar
//    }
}