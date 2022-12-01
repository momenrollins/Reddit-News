package com.momen.redditnews.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.momen.redditnews.R
import com.momen.redditnews.databinding.ItemNewsBinding
import com.momen.redditnews.model.Children

class NewsAdapter(private var listener: OnItemClickListener, private var newsList: ArrayList<Children>
) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(newsItem: Children)
    }

    class ViewHolder(
        private val binding: ItemNewsBinding,
        private val listener: OnItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(newsResult: Children) {
            binding.apply {
                newsTitleTv.text = newsResult.data.title
                newsItemCard.setOnClickListener {
                    listener.onItemClick(newsResult)
                }
                if (newsResult.data.url_overridden_by_dest?.contains(".png") == true || newsResult.data.url_overridden_by_dest?.contains(
                        ".jpg"
                    ) == true
                ) {
                    imgCard.visibility = View.VISIBLE
                    Glide.with(itemView)
                        .load(newsResult.data.url_overridden_by_dest)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                        .into(newsImageView)
                } else {
                    imgCard.visibility = View.GONE
                }
                if (newsResult.data.selftext.isNotEmpty()) {
                    newsSubtitleTv.visibility = View.VISIBLE
                    newsSubtitleTv.text = newsResult.data.selftext
                } else {
                    newsSubtitleTv.visibility = View.GONE
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

//    @SuppressLint("NotifyDataSetChanged")
//    fun updateList(list: List<Children>) {
//        newsList.clear()
//        newsList.addAll(list)
//        notifyDataSetChanged()
//    }
}