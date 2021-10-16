package com.example.healthprotask

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthprotask.adapter.ActivitiesAdapter
import com.example.healthprotask.auth.model.DistanceResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.ui.AuthViewModel
import com.example.healthprotask.auth.ui.WebViewFragment
import com.example.healthprotask.databinding.FragmentActivitiesListBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ActivitiesListFragment : Fragment() {
    private lateinit var adapter: ActivitiesAdapter
    lateinit var binding: FragmentActivitiesListBinding
    val args: ActivitiesListFragmentArgs by navArgs()   //

    var offset: String? = null
    var distanceList: DistanceResponse? = null
    //for manual pagination
    var page = 1
    var limit = 5   //will load till it gets next 5 data

    private val TAG = ActivitiesListFragment::class.java.simpleName

    private var PRIVATE_MODE = Context.MODE_PRIVATE
    private val PREF_NAME = "bearer-token"
    private val TOKEN_KEY: String = "bearer-token-key"

    private val authViewModel by viewModels<AuthViewModel>() //vm

//    override fun onResume() {
//        super.onResume()
//        //getting distance list for drop down
//        if (distanceList != null){
//            distanceList?.let { distanceResponse ->
//                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, distanceResponse.activitiesDistance)
//                binding.tvAutoComplete.setAdapter(arrayAdapter)
//            }
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userActivitiesResponseLiveData.observe(viewLifecycleOwner, ::handleUserActivity)
        authViewModel.distanceLiveData.observe(viewLifecycleOwner, ::handleDistance)
    }

    private fun handleUserActivity(userActivitiesResponse: UserActivitiesResponse?) {
        val uri: Uri = Uri.parse(userActivitiesResponse?.pagination?.next)
        offset = uri.getQueryParameter("offset")
        userActivitiesResponse?.let { adapter.notifiySuccess(it) }
    }

    private fun handleDistance(distanceResponse: DistanceResponse?) {
        distanceList = distanceResponse
        //getting distance list for drop down
        distanceResponse?.let { disResponse ->
            var dateList: MutableList<String> = ArrayList()

            for (date in disResponse.activitiesDistance){
                if (date.value != "0.0"){
                    dateList.add(date.dateTime)
                    Log.d(TAG, "handleDistance: ${date.dateTime}")
                }
            }

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_list, dateList)  //Adapter
            binding.tvAutoComplete.setAdapter(arrayAdapter)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_activities_list, container, false)
        binding = FragmentActivitiesListBinding.bind(view)

        val activitiesList: UserActivitiesResponse = args.userActivitiesResponse    //getting UserActivitiesResponse model
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())  //manager

        //getting Offset
        val uri: Uri = Uri.parse(activitiesList.pagination.next)
        offset = uri.getQueryParameter("offset")

        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        //getting sharedPreference data(Bearer Token) from WebView
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val bearerToken = sharedPref.getString(TOKEN_KEY, "default_value")

        binding.tvAutoComplete.setOnItemClickListener { adapterView, view, position, id ->
            var date: String = adapterView.getItemAtPosition(position).toString()
            bearerToken?.let { authViewModel.getUserActivities(bearerToken = it, date, sort = "desc",limit = 5,offset = 0, next = "", previous = "") }
        }

        //getting distance for drop down
        bearerToken?.let { authViewModel.getDistance(it, date) }

        adapter = ActivitiesAdapter()    //adapter
        adapter.notifiySuccess(activitiesList)  //added list data
        binding.recyclerView.adapter = adapter
        binding.progressBar.visibility = View.GONE
        binding.svNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (nestedScrollView != null) {
                    if (scrollY == nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight){
                        binding.progressBar.visibility = View.VISIBLE
                        //when reaches to last item position
                        //get next page data
                        Log.d(TAG, "onCreateView: bearer-token $bearerToken")
//                        //getting Offset
//                        val uri: Uri = Uri.parse(activitiesList.pagination.next)
//                        offset = uri.getQueryParameter("offset")
                        //getting new before date
//                        val uri: Uri = Uri.parse(url)
//                        val before: String? = uri.getQueryParameter("code")

                        if (bearerToken != null) {
                            offset?.toInt()?.let { authViewModel.getUserActivities(bearerToken = bearerToken, date, sort = "desc",limit = 5,offset = it, next = activitiesList.pagination.next, previous = "") }
//                              WebViewFragment().getUserActivities(bearerToken = bearerToken, currentDay = date, next = activitiesList.pagination.next, previous = activitiesList.pagination.previous)
                        }
                    }
                }
        })
        //dropdown list
        bearerToken?.let { bearerToken ->
            authViewModel.getUserActivities(bearerToken = bearerToken, date, sort = "desc",limit = 100,offset = 0, next = activitiesList.pagination.next, previous = "")
        }


        getData(page, limit)
        return view
    }

    private fun getData(page: Int, limit: Int) {
        //api call for next data set
    }
}