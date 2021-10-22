package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.healthprotask.R
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentLoginBinding
import com.example.healthprotask.utility.SessionManager
import com.example.healthprotask.utility.SessionManager.Companion.PREF_NAME
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var currentUserActivities: UserActivitiesResponse? = null
    lateinit var binding: FragmentLoginBinding
    val TAG = LoginFragment::class.java.simpleName
    @Inject
    lateinit var sessionManager: SessionManager //to handle session..

    private val authViewModel by viewModels<AuthViewModel>() //vm

    private var bearerToken: String? = ""
    private val PRIVATE_MODE = Context.MODE_PRIVATE
    private val TOKEN_KEY: String = "bearer-token-key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Session
        sessionManager = SessionManager(requireContext())
        if (sessionManager.isLoggedIn()){
            val currentDay = SimpleDateFormat("yyyy-MM-dd").format(Date())

            //getting sharedPreference data(Bearer Token) from Session manger
//            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
//            bearerToken = sharedPref.getString(TOKEN_KEY, "")
            //1
            bearerToken = sessionManager.getBearerToken()
            Log.d(TAG, ">>>bearer token from login session:>>> $bearerToken")
            /**
             *  user activities api
             **/
            currentDay?.let { date ->
                //2
                bearerToken?.let { authViewModel.getUserActivities(bearerToken = it, beforeDate = date, sort = "desc",limit = 5,offset = 0, next = "", previous = "") }
            }
            //after api call..we will get data in handleUserActivity()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)

        //handle ui
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<UserActivitiesResponse>("userActivitiesResponse")
            ?.observe(viewLifecycleOwner) { userActivities ->
//                Toast.makeText(requireContext(), "userActivities: $userActivities", Toast.LENGTH_LONG).show()
                currentUserActivities = userActivities
                if (currentUserActivities != null){
                    binding.btnAuth.visibility = View.GONE
                    binding.btnMyActivities.visibility = View.VISIBLE
                    binding.btnLogout.visibility = View.VISIBLE
                    binding.rlBeforeLogin.visibility = View.GONE
                    binding.lottieAfterLogin.visibility = View.VISIBLE
                    binding.lottieBeforeLogin.visibility = View.GONE
                    binding.divider.visibility = View.GONE
                }
            }

        binding.btnAuth.setOnClickListener {
//            //getting sharedPreference data(Bearer Token) from WebView
//            val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
//            bearerToken = sharedPref.getString(TOKEN_KEY, "")
//            Log.d(TAG, "bearer token form login session: $bearerToken")
//
//            bearerToken?.let { bearer ->
//                sessionManager.createAuthSession(bearer)    //stored token in shared preference.
//            }
            findNavController(view).navigate(R.id.action_loginFragment_to_webViewFragment)
        }

        binding.btnMyActivities.setOnClickListener {
                currentUserActivities?.let { userActivitiesResponse ->
                    Log.d(TAG, ">>>userActivities from btn click $userActivitiesResponse")
                    val action = LoginFragmentDirections.actionLoginFragmentToActivitiesListFragment(userActivitiesResponse = userActivitiesResponse)
                    findNavController(requireView()).navigate(action)
                }
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //method reference
        authViewModel.userActivitiesResponseLiveData.observe(viewLifecycleOwner, ::handleUserActivity)
    }

    private fun handleUserActivity(userActivitiesResponse: UserActivitiesResponse?) {
        if (userActivitiesResponse != null){
            binding.btnAuth.visibility = View.GONE
            binding.btnMyActivities.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.VISIBLE
            binding.rlBeforeLogin.visibility = View.GONE
            binding.lottieAfterLogin.visibility = View.VISIBLE
            binding.lottieBeforeLogin.visibility = View.GONE
            binding.divider.visibility = View.GONE
        }
        binding.btnMyActivities.setOnClickListener {
            userActivitiesResponse?.let { userActivitiesResponse ->
                val action = LoginFragmentDirections.actionLoginFragmentToActivitiesListFragment(userActivitiesResponse = userActivitiesResponse)
                findNavController(requireView()).navigate(action)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        childFragmentManager.clearFragmentResultListener(requestKey) // cleared listener
    }
}