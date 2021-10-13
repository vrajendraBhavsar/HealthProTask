package com.example.healthprotask

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthprotask.adapter.ActivitiesAdapter
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.FragmentActivitiesListBinding

class ActivitiesListFragment : Fragment() {
    lateinit var binding: FragmentActivitiesListBinding
    val args: ActivitiesListFragmentArgs by navArgs()   //
    //for manual pagination
    var page = 1
    var limit = 5   //will load till it gets next 5 data

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_activities_list, container, false)
        binding = FragmentActivitiesListBinding.bind(view)

        val activitiesList: UserActivitiesResponse = args.userActivitiesResponse    //getting list data
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())  //manager

        val adapter = ActivitiesAdapter()    //adapter
        adapter.notifiySuccess(activitiesList)  //added list data
        binding.recyclerView.adapter = adapter
        binding.recyclerView.setOnScrollChangeListener { view, i, i2, i3, i4 ->

        }

        getData(page, limit)
        return view
    }

    private fun getData(page: Int, limit: Int) {
        //api call for next data set
    }
}