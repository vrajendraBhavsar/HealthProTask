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
//            val tz: TimeZone = TimeZone.getTimeZone("UTC")
//            val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'")
//            df.timeZone = tz
//            try {
//                return df.parse(dateStr).toString()
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
//            return ""
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val outputFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = inputFormat.parse(dateStr)
            return outputFormat.format(date)
        }
    }

    fun notifySuccess(dataList: UserActivitiesResponse) {
//        val list: List<UserActivitiesResponse.Activity> = dataList.activities
//        for (item in list.indices){
//            activitiesList.add(list[item])
//        }
//        Log.d(TAG, "notifySuccess: paging list: $list")
        activitiesList = dataList.activities
        notifyDataSetChanged()
    }
}

//class InstagramPhotoListAdapter internal constructor(var context: Context) : PagedListAdapter<GitHubDataModel?, InstagramPhotoListAdapter.ItemViewHolder?>(DIFF_CALLBACK) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val view: View = LayoutInflater.from(context).inflate(R.layout.activity_list_item, parent, false)
//        return ItemViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
//        val item: GitHubDataModel = getItem(position)
//
//        val cd = ColorDrawable(context.getResources().getColor(R.color.pp_bg_color))
//        if (item != null) {
//            Picasso.with(context)
//                .load(item.media_url as String)
//                .transform(CropTransformation(256))
//                .placeholder(cd)
//                .into(holder.profileImage)
//        }
//    }
//
//    override fun onCurrentListChanged(previousList: PagedList<GitHubDataModel>?, currentList: PagedList<GitHubDataModel>?) {
//        super.onCurrentListChanged(previousList, currentList)
//    }
//
//    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        var profileImage: ImageView? = null
//
//        init {
//            profileImage = itemView.findViewById<View>(R.id.fb_profile_img) as ImageView
//        }
//    }
//
//    companion object {
//        private val DIFF_CALLBACK: DiffUtil.ItemCallback<GitHubDataModel> = object : DiffUtil.ItemCallback<GitHubDataModel>() {
//            override fun areItemsTheSame(oldItem: GitHubDataModel newItem: GitHubDataModel: Boolean {
//                return oldItem.id === newItem.id
//            }
//            override fun areContentsTheSame(oldItem: GitHubDataModel newItem: GitHubDataModel: Boolean {
//                return oldItem.equals(newItem)
//            }
//        }
//    }
//}