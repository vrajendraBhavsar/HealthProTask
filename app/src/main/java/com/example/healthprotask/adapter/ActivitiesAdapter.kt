package com.example.healthprotask.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.DataViewBinding
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class ActivitiesAdapter : RecyclerView.Adapter<ActivitiesAdapter.DataViewHolder>() {
    private var activitiesList: MutableList<UserActivitiesResponse.Activity> = ArrayList()
    private val TAG = ActivitiesAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(DataViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(activities = activitiesList[position])
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

    class DataViewHolder(private val binding: DataViewBinding) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(activities: UserActivitiesResponse.Activity){
            binding.tvName.text = "activity: ${activities.activityName}"
            binding.tvCalories.text = "Calories: ${activities.calories}"
            binding.tvSpeed.text = "Speed: ${activities.speed}"
            binding.tvDistance.text = "Distance: ${activities.distance}"
            binding.tvDistanceUnit.text = "(${activities.distanceUnit})"
            binding.tvStartTime.text = "StartTime: ${(activities.startTime)}"
            binding.tvSteps.text = "Steps: ${activities.steps}"
        }

        @SuppressLint("SimpleDateFormat")
        fun fromISO8601UTCString(dateStr: String?): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(dateStr)
            return outputFormat.format(date)
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