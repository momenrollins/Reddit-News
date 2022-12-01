package com.momen.redditnews.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.momen.redditnews.databinding.FragmentMainBinding
import com.momen.redditnews.model.Children


class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: NewsAdapter
    private val newsList = ArrayList<Children>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val listener = object : NewsAdapter.OnItemClickListener {
            override fun onItemClick(newsItem: Children) {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToDetailsFragment(
                        newsItem.data,
                        newsItem.data.title
                    )
                )
            }
        }
        adapter = NewsAdapter(listener, newsList)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)
        binding.newsList.adapter = adapter
        viewModel.getNews()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getNews()
        }
        binding.tryAgain.setOnClickListener {
            viewModel.getNews()
        }
        lifecycleScope.launchWhenCreated {
            viewModel.newsData.collect {
                binding.refreshLayout.isRefreshing = false
                binding.progressBar.visibility = View.GONE
                if (it.status) {
                    binding.tryAgain.visibility = View.GONE
                    binding.newsList.visibility = View.VISIBLE
                    newsList.clear()
                    newsList.addAll(it.data!!.children)
                    adapter.notifyDataSetChanged()
                } else {
                    binding.tryAgain.visibility = View.VISIBLE
                    binding.newsList.visibility = View.GONE
                }
            }
        }


        return binding.root
    }

}