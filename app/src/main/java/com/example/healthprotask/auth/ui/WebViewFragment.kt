package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
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
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.healthproclienttask.auth.ui.LoginFragment
import com.example.healthproclienttask.utility.NetworkUtility
import com.example.healthprotask.auth.model.AccessTokenRequestResponse
import com.example.healthprotask.auth.model.ProfileResponse
import com.example.healthprotask.auth.model.ResultData
import com.example.healthprotask.databinding.FragmentWebVeiwBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URISyntaxException
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64.getUrlEncoder

@AndroidEntryPoint
class WebViewFragment : Fragment() {
    private var bearerToken: String? = null
    private var refreshToken: String? = null
    lateinit var binding: FragmentWebVeiwBinding
    private val TAG = WebViewFragment::class.java.simpleName
    private var codeVerifier: String? = null
    private var codeChallenge: String? = null
//    private val url =
////        "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity"
//"https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight&expires_in=604800"

    private val url: String = "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&code_challenge_method=S256&scope=activity%20nutrition%20heartrate%20location%20nutrition%20profile%20settings%20sleep%20social%20weight"

    var redirect: Boolean = false
    var completelyLoaded: Boolean = true   //when page is loaded completely ..it will be true

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
        binding = FragmentWebVeiwBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val requestKey = arguments?.getString(REQUEST_KEY, null)
        Log.d(TAG, "onViewCreated: requestKey: $requestKey")
        if (LoginFragment.requestKey.isNullOrEmpty()) {
            //fragmentManager?.beginTransaction()?.remove(this@WebViewFragment)?.commit()//back to parent fragment
            return
        }
        //...method reference
        authViewModel.userProfileResponseLiveData.observe(viewLifecycleOwner, ::handleUserProfile)
        authViewModel.accessTokenRequestResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRequest)
        authViewModel.accessTokenRefreshResponseLiveData.observe(viewLifecycleOwner, ::handleAccessTokenRefresh)
        //....
        binding.wbWebView.webViewClient = object : WebViewClient() {
            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!completelyLoaded) {
                    redirect = true
                }
                completelyLoaded = false
                    //loading url
                val codeVerifier: String = generateCodeVerifier()
                val codeChallenge: String? = generateCoedChallenger(codeVerifier = codeVerifier)
                val codeChallengeString = "code_challenge=$codeChallenge"
//                val uri: URI? = appendCodeChallengeUri(url = url, appendQuery = codeChallengeString)
                val urlConcate = "$url&$codeChallengeString"
                val uri: URI = URI(urlConcate)
                Log.d(TAG, "onViewCreated: uri $uri")

                view?.apply {
                    loadUrl(uri.toString())
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
                codeVerifier = generateCodeVerifier()
                codeVerifier?.let{
                    codeChallenge = generateCoedChallenger(codeVerifier = it)
                }
                val codeChallengeString = "code_challenge=$codeChallenge"
//                val uri: URI? = appendCodeChallengeUri(url = url, appendQuery = codeChallengeString)
                val urlConcate = "$url&$codeChallengeString"
                val uri: URI = URI(urlConcate)
                Log.d(TAG, "onViewCreated: uri $uri")

                view?.apply {
                    loadUrl(uri.toString())
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

                        if (requestKey != null) {
                            setFragmentResult(
                                requestKey,
                                Bundle().apply {
                                    putString(DATA_KEY, code)
                                }
                            )
                            val base64 = getBase64("${NetworkUtility.Client_ID}:${NetworkUtility.Client_SECRET}")
                            Log.d(TAG, "getBase64: $base64")
                            val authorizationString = "Basic $base64"
                            Log.d(TAG, "authoriaztion String ..Basic XOXO : $authorizationString")

                            /**
                             *  Token api req
                             **/
                            if (code != null) {
                                codeVerifier?.let {
                                    authViewModel.requestToken(
                                        authorization = authorizationString,
                                        clientId = NetworkUtility.Client_ID,
                                        grantType = GRANT_TYPE,
                                        redirectUri = NetworkUtility.REDIRECT_URL,
                                        code = code,
                                        codeVerifier = it
                                    )
                                }
                            }else {
                                Log.d(TAG, "onPageFinished: code is empty")
                            }
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
        val codeVerifier: String = generateCodeVerifier()
        val codeChallenge: String? = generateCoedChallenger(codeVerifier = codeVerifier)
        val codeChallengeString = "code_challenge=$codeChallenge"
//        val uri: URI? = appendCodeChallengeUri(url = url, appendQuery = codeChallengeString)
        val urlConcate = "$url&$codeChallengeString"
        val uri: URI = URI(urlConcate)
        Log.d(TAG, "onViewCreated: uri $uri")

        binding.wbWebView.loadUrl(uri.toString())
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

    private fun handleAccessTokenRequest(resultData: ResultData<AccessTokenRequestResponse>?) {
        when (resultData) {
            is ResultData.Loading -> {
            }
            is ResultData.Success -> {
                val accessTokenRequestResponse: AccessTokenRequestResponse? = resultData.data
                Log.d(TAG, "ResultData.Success: ${accessTokenRequestResponse.toString()}")

                //retrieve access-token and refresh-token from response
                val accessToken = accessTokenRequestResponse?.access_token
                refreshToken = accessTokenRequestResponse?.refresh_token
                Log.d(TAG, "accessToken: $accessToken")
                Log.d(TAG, "refreshToken: $refreshToken")
//              val userProfileResponse: ProfileResponse? = accessToken?.let { getUserProfile(it) } //to use common fun for user api call

              val bearerToken = "Bearer $accessToken"
//                bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyM0JLWUYiLCJzdWIiOiI5TUZQNFAiLCJpc3MiOiJGaXRiaXQiLCJ0eXAiOiJhY2Nlc3NfdG9rZW4iLCJzY29wZXMiOiJyc29jIHJhY3QgcnNldCBybG9jIHJ3ZWkgcmhyIHJwcm8gcm51dCByc2xlIiwiZXhwIjoxNjMzNjM1NjgyLCJpYXQiOjE2MzM2MDY4ODJ9.plmCeTX0d9IerKz7pXAKM13DeVIUiUl03W6zqlvhEwg"
                Log.d(TAG, "bearerToken: $bearerToken")

                    /**
                     *  Refresh token api
                     **/
                bearerToken?.let { bearerToken ->
                    authViewModel.getUserProfile(bearerToken = bearerToken)
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
        if (profileResponse?.success == false){
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)   //it determines the entropy - higher the number more secure it gets, should be between 32 and 96 bytes.
        secureRandom.nextBytes(bytes)
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier: String = Base64.encodeToString(bytes, encoding)   //generated string length will be  between 43 and 128 characters.
        return codeVerifier
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateCoedChallenger(codeVerifier: String): String? {
        val bytes = codeVerifier.toByteArray()  //converted from Base64 back into a byte array
        val messageDigest = MessageDigest.getInstance("SHA-256")    //hashed using the "SHA-256" algo
        messageDigest.update(bytes)
        val digest = messageDigest.digest()
        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeChallenge = Base64.encodeToString(digest, encoding)     //encoded back to Base64
        return codeChallenge
    }

    @Throws(URISyntaxException::class)
    fun appendCodeChallengeUri(url: String?, appendQuery: String): URI? {
        val oldUri = URI(url)
        var newQuery: String = oldUri.query
        if (newQuery == null) {
            newQuery = appendQuery
        } else {
            newQuery += "&$appendQuery"
        }
        return URI(
            oldUri.scheme, oldUri.authority,
            oldUri.path, newQuery, oldUri.fragment
        )
    }
}