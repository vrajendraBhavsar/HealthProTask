package com.example.healthprotask

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthprotask.adapter.ActivitiesAdapter
import com.example.healthprotask.auth.model.DistanceResponse
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.auth.ui.AuthViewModel
import com.example.healthprotask.databinding.FragmentActivitiesListBinding
import com.example.healthprotask.utility.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ActivitiesListFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private var bearerToken: String? = ""
    private lateinit var adapter: ActivitiesAdapter
    lateinit var binding: FragmentActivitiesListBinding
    val args: ActivitiesListFragmentArgs by navArgs()   //
    var activitiesList: UserActivitiesResponse? = null

    @Inject
    lateinit var sessionManager: SessionManager //to handle session..

    var offset: String? = null
    var distanceList: DistanceResponse? = null

    //for manual pagination
    var page = 1
    var limit = 5   //will load till it gets next 5 data

    private val TAG = ActivitiesListFragment::class.java.simpleName

    private val authViewModel by viewModels<AuthViewModel>() //vm

    //for date picker
    var day = 0
    var month = 0
    var year = 0

    var savedDay = 0    //will contain updated info
    var savedMonth = 0
    var savedYear = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authViewModel.userActivitiesResponseLiveData.observe(
            viewLifecycleOwner,
            ::handleUserActivity
        )
    }

    private fun handleUserActivity(userActivitiesResponse: UserActivitiesResponse?) {
        if (userActivitiesResponse?.pagination?.next != null) {
            val uri: Uri = Uri.parse(userActivitiesResponse.pagination.next)
            offset = uri.getQueryParameter("offset")
            userActivitiesResponse.let {
                adapter.notifySuccess(it)
                Log.d(TAG, "handleUserActivity: next: ${it.pagination}")
            }
        }
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_activities_list, container, false)
        binding = FragmentActivitiesListBinding.bind(view)

        activitiesList = args.userActivitiesResponse    //getting UserActivitiesResponse model
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())  //manager

        //getting Offset
        val uri: Uri = Uri.parse(activitiesList?.pagination?.next)
        offset = uri.getQueryParameter("offset")

        val date = SimpleDateFormat("yyyy-MM-dd").format(Date())    //to get today's date.
        /**
         *  date picker
         **/
        pickDate()

        adapter = ActivitiesAdapter()    //adapter
        activitiesList?.let { adapter.notifySuccess(it) }  //added list data
        binding.recyclerView.adapter = adapter
        binding.progressBar.visibility = View.GONE
        binding.svNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { nestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (nestedScrollView != null) {
                if (scrollY == nestedScrollView.getChildAt(0).measuredHeight - nestedScrollView.measuredHeight) {
                    //when reaches to last item position
                    //get next page data
                    Log.d(TAG, "onCreateView: bearer-token $bearerToken")
                    if (activitiesList == null) {
                        binding.progressBar.visibility = View.VISIBLE
                        offset?.toInt()?.let {
                            bearerToken?.let { bearerToken ->
                                authViewModel.getUserActivities(
                                    bearerToken = bearerToken,
                                    date,
                                    sort = "desc",
                                    limit = 5,
                                    offset = it,
                                    next = activitiesList?.pagination?.next,
                                    previous = ""
                                )
                            }
                        }
                    }
                }
            }
        })
        return view
    }

    //fun to make sure that info/date on calender is up to date..
    private fun getDateCalender() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {
        binding.tvAutoComplete.setOnClickListener {
            getDateCalender()
            /**
             * date picker dialog
             **/
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dateOfMonth: Int) {
        savedDay = dateOfMonth
        savedMonth = month
        savedYear = year
//we can use these saved-variables in api call ,e.g. savedYear-savedMonth-savedDay => 2021-10-18
        getDateCalender()
        bearerToken?.let {
            authViewModel.getUserActivities(
                bearerToken = it,
                "$savedYear-${savedMonth + 1}-${savedDay + 1}",
                sort = "desc",
                limit = 100,
                offset = 0,
                next = "",
                previous = ""
            )
        }
    }
}