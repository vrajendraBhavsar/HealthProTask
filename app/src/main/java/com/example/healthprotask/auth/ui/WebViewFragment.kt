package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.healthproclienttask.auth.ui.LoginFragment
import com.example.healthprotask.R
import com.example.healthprotask.databinding.FragmentWebVeiwBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection
import java.util.*

class WebViewFragment : Fragment() {
    lateinit var binding: FragmentWebVeiwBinding
    private val TAG = "HEALTHPROTEST"
    private val url = "https://www.fitbit.com/oauth2/authorize?response_type=code&client_id=23BKYF&redirect_uri=https%3A%2F%2Fwww.mindinventory.com%2F&scope=activity"
    private var htmlContent = ""

    var redirect: Boolean = false
    var completelyLoaded: Boolean = true   //when page is loaded completely ..it will be true

    companion object{
        fun newInstance() = WebViewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWebVeiwBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //....
        binding.wbWebView.webViewClient = object : WebViewClient(){
            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!completelyLoaded){
                    redirect = true
                }
                completelyLoaded = false
                //loading url

                view?.apply {
                    url?.let { loadUrl(it) }
                    settings.javaScriptEnabled = true

//                    addJavascriptInterface(object : Any() {
//                        @JavascriptInterface
//                        fun saveMetaTag(name: String?, value: String?) {
//                            //Save meta tag value in some dictionary
//                        }
//
//                        @JavascriptInterface
//                        fun onNoMoreMetaTags() {
//                            //Process meta tags
//                        }
//                    }, "metaLoader")

//                    loadUrl("javascript:var metaTags = document.getElementsByTagName('meta'); if (metaTags != null && metaTags.length > 0) {var i; for (i=0;i<metaTags.length;i++) { window.metaLoader.saveMetaTag(metaTags[i].name, metaTags[i].content);  };  }; window.metaLoader.onNoMoreMetaTags();");

                    //...
//                    val aURL = URL(url)
//                    val conn: URLConnection = aURL.openConnection()
//                    conn.connect()
//                    val `is`: InputStream = conn.getInputStream()
//                    htmlContent = convertToString(`is`)
                }
                Log.d(TAG, "shouldOverrideUrlLoading1: $url")
                Log.d(TAG, "htmlContent: $htmlContent")
                return true
            }

            @SuppressLint("SetJavaScriptEnabled")
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                if (!completelyLoaded){
                    redirect = true
                }
                completelyLoaded = false
                //loading url

                view?.apply {
                    url?.let { loadUrl(it) }
                    settings.javaScriptEnabled = true
                }
                Log.d(TAG, "shouldOverrideUrlLoading2: ${request?.url}")
                Log.d(TAG, "htmlContent: $htmlContent")
                 return super.shouldOverrideUrlLoading(view, request)

//                return if(request?.url?.lastPathSegment == "error.html") {
//                    view?.loadUrl(url)
//                    true
//                } else {
//                    false
//                }
            }

            fun convertToString(inputStream: InputStream?): String {
                val string = StringBuffer()
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                try {
                    while (reader.readLine().also { line = it } != null) {
                        string.append(line.toString() + "\n")
                    }
                } catch (e: IOException) {
                }
                return string.toString()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                completelyLoaded = false
                binding.progressbar.visibility = View.VISIBLE   //while loading data..its visible
                Log.d(TAG, "onPageStarted: $completelyLoaded")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!redirect){
                    completelyLoaded = true
                }
                if (completelyLoaded && !redirect){ //means page is completely loaded
                    Log.d(TAG, "onPageFinished: $completelyLoaded")
                    if (!url.isNullOrEmpty()){
                        Log.d(TAG, "onPageFinished: url link : $url")

//                        Handler().postDelayed({
//                            binding.progressbar.visibility = View.GONE
//                        }, 3000)

//                        GlobalScope.launch {
//                            delay(3000)
//                            Thread.sleep(3000)
//                            binding.progressbar.visibility = View.GONE
//                        }
//                        fragmentManager?.beginTransaction()?.remove(this@WebViewFragment)?.commit()
                    }
                }
                else{
                    redirect = false
                }
            }
        }
        //Mention chrome latest version...
        binding.wbWebView.settings.userAgentString = "Chrome/94.0.4606.71 Mobile"   //to avoid possible errors from occurring in latest versions
        binding.wbWebView.loadUrl(url)
    }
}