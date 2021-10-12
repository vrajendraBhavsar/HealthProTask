package com.example.healthprotask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.healthprotask.R

class ActivitiesAdapter(val context: Context): RecyclerView.Adapter<ActivitiesAdapter.DataViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(LayoutInflater.from(context).inflate(R.layout.data_view, parent, false))
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
//        holder.fullName.text = dataList[position].
    }

    override fun getItemCount(): Int {
        return 0
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val fullName = itemView.findViewById<TextView>(R.id.tvFullName)
//        val url = itemView.findViewById<TextView>(R.id.tvUrl)
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