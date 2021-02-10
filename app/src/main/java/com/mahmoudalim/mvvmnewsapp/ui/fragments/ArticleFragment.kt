package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_breaking_news){

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
    }
}