package com.momen.redditnews.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.momen.redditnews.R
import com.momen.redditnews.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        binding.apply {
            if (args.details.url_overridden_by_dest?.contains(".png") == true || args.details.url_overridden_by_dest?.contains(
                    ".jpg"
                ) == true
            ) {
                newsImageView.visibility = View.VISIBLE
                Glide.with(requireContext())
                    .load(args.details.url_overridden_by_dest)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.mipmap.ic_launcher).placeholder(R.mipmap.ic_launcher)
                    .into(newsImageView)
            } else {
                newsImageView.visibility = View.GONE
            }
            if (args.details.selftext.isNotEmpty()) {
                newsSubtitleTv.visibility = View.VISIBLE
                subtitle = args.details.selftext
            } else {
                newsSubtitleTv.visibility = View.GONE
            }
        }
        return binding.root
    }
}