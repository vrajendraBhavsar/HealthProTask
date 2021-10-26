package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.healthprotask.R
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentLoginBinding
import com.example.healthprotask.utility.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var currentUserActivities: UserActivitiesResponse? = null
    lateinit var binding: FragmentLoginBinding
    val TAG = LoginFragment::class.java.simpleName

    @Inject
    lateinit var sessionManager: SessionManager //to handle session..
    private var bearerToken: String? = ""

    @Inject
    lateinit var okHttpClient: OkHttpClient

//    @Inject
//    lateinit var response: Response //OkHttpClient response..

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Session
        sessionManager = SessionManager(requireContext())
//// Handling 401 ..
//        if (response.code == 401) {
//            sessionManager.logoutUser()
//        } else {
            if (sessionManager.isLoggedIn()) {
                Log.d(TAG, ">>>bearer token from login session:>>> $bearerToken")
                findNavController().navigate(R.id.action_loginFragment_to_loginSuccessFragment)
            }
//        }

        //.................
        okHttpClient.newBuilder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $bearerToken")
                    .build()

                val response = chain.proceed(newRequest)
                Log.d("MyApp", "Code : " + response.code)

                if (response.code == 401){
                    sessionManager.logoutUser()
                } else{
                    if (sessionManager.isLoggedIn()){
                        Log.d(TAG, ">>>bearer token from login session:>>> $bearerToken")

                        findNavController().navigate(R.id.action_loginFragment_to_loginSuccessFragment)
                    }
                }
                return response
            }
        })
        //...................
//        if (sessionManager.isLoggedIn()){
//            val currentDay = SimpleDateFormat("yyyy-MM-dd").format(Date())
//            //1
////            bearerToken = sessionManager.getBearerToken()
//            Log.d(TAG, ">>>bearer token from login session:>>> $bearerToken")
////            /**
////             *  user activities api
////             **/
////            currentDay?.let { date ->
////                //2
////                bearerToken?.let { authViewModel.getUserActivities(bearerToken = it, beforeDate = date, sort = "desc",limit = 5,offset = 0, next = "", previous = "") }
////            }
//            //.....
//            findNavController().navigate(R.id.action_loginFragment_to_loginSuccessFragment)
//        }
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
            }

        binding.btnAuth.setOnClickListener {
            sessionManager.editor.clear().commit()
            findNavController(view).navigate(R.id.action_loginFragment_to_webViewFragment)
        }
        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}