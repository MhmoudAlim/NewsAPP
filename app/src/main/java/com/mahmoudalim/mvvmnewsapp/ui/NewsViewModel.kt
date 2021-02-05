package com.mahmoudalim.mvvmnewsapp.ui.ui

import androidx.lifecycle.ViewModel
import com.mahmoudalim.mvvmnewsapp.repository.NewsRepository

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {
}