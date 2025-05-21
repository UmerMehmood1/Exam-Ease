package com.picsart.studio.Student.Activities.fragmentForCourseDetailsActivity.adapters


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.picsart.studio.Models.Video
import com.picsart.studio.R

class InstructorCourseVideoAdapter(
    private val context: Context,
    private val videoList: List<Video?>
) : RecyclerView.Adapter<InstructorCourseVideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        val title: TextView = itemView.findViewById(R.id.videoTitle)
        val description: TextView = itemView.findViewById(R.id.videoDescription)
        val url: TextView = itemView.findViewById(R.id.videoUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videoList[position]
        holder.title.text = video?.title
        holder.description.text = video?.description
        holder.url.text = video?.url

        // Load video thumbnail with Glide
        Glide.with(context)
            .asBitmap()
            .load(video?.url)
            .apply(RequestOptions().frame(1000000)) // 1 second into the video
            .placeholder(R.drawable.available_courses) // Optional placeholder
            .error(android.R.drawable.stat_sys_warning)        // Optional error image
            .into(holder.thumbnail)

        // Open video on click
        holder.thumbnail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video?.url))
            context.startActivity(intent)
        }

        holder.url.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video?.url))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = videoList.size
}
