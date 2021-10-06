package com.example.healthproclienttask.auth.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.healthprotask.R
import com.example.healthprotask.auth.ui.AuthViewModel
import com.example.healthprotask.auth.ui.WebViewFragment
import com.example.healthprotask.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    val TAG = LoginFragment::class.java.simpleName

    companion object{
        fun newInstance() = LoginFragment()
        val requestKey = UUID.randomUUID().toString()   //request key for fragment result listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        childFragmentManager.setFragmentResultListener(requestKey, this){ _, bundle ->
            val code = bundle.getString(WebViewFragment.DATA_KEY, "Unknown")
            Log.d(TAG, "onViewCreated: bundle code: $code")
            Toast.makeText(requireContext(), "code : $code", Toast.LENGTH_LONG).show()
        }

        binding.btnAuth.setOnClickListener {
            val webViewFragment = WebViewFragment.newInstance()
            val bundle = Bundle()
            bundle.putString(WebViewFragment.REQUEST_KEY, requestKey) // bundle ma key set tari
            webViewFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, webViewFragment)
                .addToBackStack("WebViewFragment")
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        childFragmentManager.clearFragmentResultListener(requestKey) // cleared listener
    }
}