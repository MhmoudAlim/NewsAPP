package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import com.mahmoudalim.mvvmnewsapp.util.Resource

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private val TAG = "Breaking News Fragment"
    private lateinit var binding: FragmentBreakingNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        binding.paginationProgressBar
        binding.rvBreakingNews

        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> showProgressBar()

                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { errorMessage ->
                        Log.i(TAG, "Error : $errorMessage ")
                    }
                }
            }
        })
    }

    private fun hideProgressBar() {
         binding.paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
    }


    private fun setUpRecyclerView() {

        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}