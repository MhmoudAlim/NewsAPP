package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import com.mahmoudalim.mvvmnewsapp.util.Resource
import kotlinx.android.synthetic.main.fragment_breaking_news.*
import retrofit2.Response

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    val TAG = "Breaking News Fragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                     Log.i(TAG ,"Error : $errorMessage " )
                    }
                }
            }
        })
    }

    private  fun hideProgressBar(){
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private  fun showProgressBar(){
        paginationProgressBar.visibility = View.VISIBLE
    }



    private fun setUpRecyclerView() {

        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}