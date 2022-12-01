package com.momen.redditnews.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momen.redditnews.model.NewsRoot
import com.momen.redditnews.network.ApiClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val apiClient = ApiClient.apiInterface
    val newsData: MutableSharedFlow<NewsRoot> = MutableSharedFlow()
    fun getNews() {
        viewModelScope.launch {
            try {
                val newsResponse = apiClient.getNews().also { news -> news.status = true }
                newsData.emit(newsResponse)
            } catch (e: Exception) {
                newsData.emit(NewsRoot(status = false, data = null))
                e.printStackTrace()
            }
        }
    }
}