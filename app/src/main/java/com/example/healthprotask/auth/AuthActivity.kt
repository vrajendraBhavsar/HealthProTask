package com.example.healthprotask.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.healthprotask.R
import com.example.healthprotask.auth.ui.LoginFragment
import com.example.healthprotask.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //1
//        if (savedInstanceState == null){
//            supportFragmentManager.beginTransaction()
//                .replace(binding.container.id, LoginFragment.newInstance())
//                .commit()
//        }
        //2

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

//        navController.navigate(R.id.loginFragment)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

//    override fun onBackPressed() {
//        val webView = WebView(this)
//        if (webView.canGoBack()) {
//            webView.goBack()
//        }
//        else {
//            super.onBackPressed()
//        }
//    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        val webView = WebView(this)
//        if (event.action == KeyEvent.ACTION_DOWN) {
//            when (keyCode) {
//                KeyEvent.KEYCODE_BACK -> {
//                    if (webView.canGoBack()) {
//                        webView.goBack()
//                    }
//                    else {
//                        return super.onKeyDown(keyCode, event)
//                    }
//                    return true
//                }
//            }
//        }
//        return super.onKeyDown(keyCode, event)
//    }
}