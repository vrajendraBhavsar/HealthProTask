package com.example.healthprotask.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.healthprotask.R
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.DataViewBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ActivitiesAdapter : RecyclerView.Adapter<ActivitiesAdapter.DataViewHolder>() {
    private var activitiesList: MutableList<UserActivitiesResponse.Activity> = ArrayList()
    private val TAG = ActivitiesAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(DataViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(activities = activitiesList[position])
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    class DataViewHolder(private val binding: DataViewBinding) : RecyclerView.ViewHolder(binding.root){
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(activities: UserActivitiesResponse.Activity){
            when(activities.activityName){
                "Walk" -> binding.ivActivity.setImageResource(R.drawable.ic_walk)
                "Bike" -> binding.ivActivity.setImageResource(R.drawable.ic_bicycle)
                "Swim" -> binding.ivActivity.setImageResource(R.drawable.ic_swimming)
                "Run" -> binding.ivActivity.setImageResource(R.drawable.ic_run)
            }

            val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            val string1 = activities.startTime
            val startTimeDate: Date = df1.parse(string1)

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val outputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
            val parsedDate = inputFormat.parse(startTimeDate.toInstant().toString())
            val formattedDate = outputFormat.format(parsedDate)

            Log.d("DATEE", "bind: $formattedDate")
            Log.d("DATEE", "bind: ${startTimeDate.toInstant()}")

            binding.tvName.text = activities.activityName
            binding.tvStartTime.text = formattedDate
            binding.tvCalories.text = activities.calories.toString()
            binding.tvCaloriesUnit.text = "cal"
            binding.tvSpeed.text = String.format("%.2f", activities.speed)
            binding.tvPace.text = String.format("%.2f", activities.pace)
            binding.tvPaceUnit.text = "pace"
            binding.tvSteps.text = activities.steps.toString()
        }
    }

    fun notifySuccess(dataList: UserActivitiesResponse) {
        val list: List<UserActivitiesResponse.Activity> = dataList.activities
        for (item in list.indices){
            activitiesList.add(list[item])
        }
        Log.d(TAG, "notifySuccess: paging list: $list")
//        activitiesList = dataList.activities
        notifyDataSetChanged()
    }
}