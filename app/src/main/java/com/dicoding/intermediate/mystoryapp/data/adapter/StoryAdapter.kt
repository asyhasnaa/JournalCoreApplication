package com.dicoding.intermediate.mystoryapp.data.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.intermediate.mystoryapp.data.response.ListStoryItem
import com.dicoding.intermediate.mystoryapp.databinding.ListItemBinding
import com.dicoding.intermediate.mystoryapp.ui.detail.DetailActivity

class StoryAdapter(private val storyList: List<ListStoryItem>) :
    RecyclerView.Adapter<StoryAdapter.StoryListViewHolder>() {

    inner class StoryListViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.itemLayout.setOnClickListener(this)
        }

        fun bind(storyItem: ListStoryItem) {
            binding.tvName.text = storyItem.name
            binding.tvDescription.text = storyItem.description

            Glide.with(itemView.context)
                .load(storyItem.photoUrl)
                .fitCenter()
                .into(binding.ivStoryImage)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val storyItem = storyList[position]
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("storyItem", storyItem)
                }

                val imagePair = Pair.create<View, String>(binding.ivStoryImage, "story_image")
                val namePair = Pair.create<View, String>(binding.tvName, "name")
                val descriptionPair =
                    Pair.create<View, String>(binding.tvDescription, "description")

                val optionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity, imagePair, namePair, descriptionPair
                    )
                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryListViewHolder {
        val binding =
            ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return storyList.size
    }

    override fun onBindViewHolder(holder: StoryListViewHolder, position: Int) {
        val storyItem = storyList[position]
        holder.bind(storyItem)
    }
}
