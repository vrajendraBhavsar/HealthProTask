package com.example.healthprotask.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.healthprotask.auth.model.UserActivitiesResponse
import com.example.healthprotask.databinding.DataViewBinding

class ActivitiesAdapter : RecyclerView.Adapter<ActivitiesAdapter.DataViewHolder>() {
    private var activitiesList: List<UserActivitiesResponse.Activity> = listOf()

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
            binding.tvName.text = "activity: ${activities.name}"
            binding.tvCalories.text = "Calories: ${activities.calories.toString()}"
            binding.tvDescription.text = "Description: ${activities.description}"
            binding.tvDistance.text = "Distance: ${activities.distance.toString()}"
            binding.tvStartTime.text = "StartTime: ${activities.startTime}"
            binding.tvSteps.text = "Steps: ${activities.steps.toString()}"
        }
    }

    fun notifiySuccess(dataList: UserActivitiesResponse) {
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