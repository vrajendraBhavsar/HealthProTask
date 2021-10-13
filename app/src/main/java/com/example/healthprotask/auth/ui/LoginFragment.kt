package com.example.healthprotask.auth.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.healthprotask.R
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var currentUserActivities: UserActivitiesResponse? = null
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
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        binding = FragmentLoginBinding.bind(view)

        binding.btnAuth.setOnClickListener {
            val webViewFragment = WebViewFragment.newInstance()
            val bundle = Bundle()
            bundle.putString(WebViewFragment.REQUEST_KEY, requestKey) // bundle ma key set tari
            webViewFragment.arguments = bundle

//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.container, webViewFragment)
//                .addToBackStack("WebViewFragment")
//                .commit()
            findNavController(view).navigate(R.id.action_loginFragment_to_webViewFragment)
        }

        binding.btnMyActivities.setOnClickListener {
                currentUserActivities?.let { userActivitiesResponse ->
                    val action = LoginFragmentDirections.actionLoginFragmentToActivitiesListFragment(userActivitiesResponse = userActivitiesResponse)
                    findNavController(requireView()).navigate(action)
                }
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        childFragmentManager.setFragmentResultListener(requestKey, this){ _, bundle ->
//            val code = bundle.getString(WebViewFragment.DATA_KEY, "Unknown")
//            Log.d(TAG, "onViewCreated: bundle code: $code")
//            Toast.makeText(requireContext(), "code : $code", Toast.LENGTH_LONG).show()
//        }

        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<UserActivitiesResponse>("userActivitiesResponse")
            ?.observe(viewLifecycleOwner) { userActivities ->
//                Toast.makeText(requireContext(), "userActivities: $userActivities", Toast.LENGTH_LONG).show()
                currentUserActivities = userActivities
                if (currentUserActivities != null){
                    binding.btnAuth.visibility = View.GONE
                    binding.btnMyActivities.visibility = View.VISIBLE

                    binding.tvMultiPurposeInfo.text = "Want to logout? Click here."
                    binding.tvMultiPurposeInfo.setOnClickListener {
                        //log out functionality
                    }
                }
            }

    }
    override fun onDestroy() {
        super.onDestroy()
//        childFragmentManager.clearFragmentResultListener(requestKey) // cleared listener
    }
}