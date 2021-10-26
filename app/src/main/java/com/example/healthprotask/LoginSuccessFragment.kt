package com.example.healthprotask

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.ui.AuthViewModel
import com.example.healthprotask.auth.ui.LoginFragment
import com.example.healthprotask.auth.ui.LoginFragmentDirections
import java.util.Timer
import kotlin.concurrent.schedule
import com.example.healthprotask.databinding.FragmentLoginSuccessBinding
import com.example.healthprotask.utility.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginSuccessFragment : Fragment() {
    //....
    private var currentUserActivities: UserActivitiesResponse? = null
    lateinit var binding: FragmentLoginSuccessBinding
    val TAG = LoginFragment::class.java.simpleName
    @Inject
    lateinit var sessionManager: SessionManager //to handle session..

    private val authViewModel by viewModels<AuthViewModel>() //vm
    private var bearerToken: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Session
        sessionManager = SessionManager(requireContext())
        if (sessionManager.isLoggedIn()){
            val currentDay = SimpleDateFormat("yyyy-MM-dd").format(Date())
            //1
            bearerToken = sessionManager.getBearerToken()
            Log.d(TAG, ">>>bearer token from login session:>>> $bearerToken")
            /**
             *  user activities api
             **/
            currentDay.let { date ->
                //2
                bearerToken?.let { authViewModel.getUserActivities(bearerToken = it, beforeDate = date, sort = "desc",limit = 5,offset = 0, next = "", previous = "") }
            }
            //after api call..we will get data in handleUserActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.lottieLoading.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login_success, container, false)
        binding = FragmentLoginSuccessBinding.bind(view)
        binding.lottieLoading.visibility = View.GONE

        //handle ui
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<UserActivitiesResponse>("userActivitiesResponse")
            ?.observe(viewLifecycleOwner) { userActivities ->
//                Toast.makeText(requireContext(), "userActivities: $userActivities", Toast.LENGTH_LONG).show()
                currentUserActivities = userActivities
            }

        binding.btnMyActivities.setOnClickListener {
            binding.lottieLoading.visibility = View.VISIBLE

            Handler().postDelayed({
                currentUserActivities?.let { userActivitiesResponse ->
                    Log.d(TAG, ">>>userActivities from btn click $userActivitiesResponse")
                    val action = LoginFragmentDirections.actionLoginFragmentToLoginSuccessFragment(userActivitiesResponse = userActivitiesResponse)
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }, 5000)

//            Timer("getList", false).schedule(4000) {

//            }
        }
        binding.btnLogout.setOnClickListener {
            Log.d(TAG, "logout: $bearerToken")
            sessionManager.logoutUser()
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
        if (userActivitiesResponse?.success == false){
            Log.d(TAG, "Login success, user activity response: $userActivitiesResponse")
            sessionManager.logoutUser()
        }else{
            binding.btnMyActivities.setOnClickListener {
                userActivitiesResponse?.let { userActivitiesResponse ->
                    val action = LoginSuccessFragmentDirections.actionLoginSuccessFragmentToActivitiesListFragment(userActivitiesResponse = userActivitiesResponse)
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
        }
    }
}