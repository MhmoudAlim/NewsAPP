package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import com.mahmoudalim.mvvmnewsapp.util.Constants.Companion.Query_DEFAULT_PAGE_SIZE
import com.mahmoudalim.mvvmnewsapp.util.Resource
import es.dmoral.toasty.Toasty


class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private val TAG = "Breaking News Fragment"
    private lateinit var binding: FragmentBreakingNewsBinding
    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        binding.paginationProgressBar
        binding.rvBreakingNews
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> showProgressBar()

                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPAges = newsResponse.totalResults / Query_DEFAULT_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPAges
                        if (isLastPage)
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    it.message?.let { errorMessage ->
                        Log.i(TAG, "Error : $errorMessage ")
                        Toasty.error(
                            activity as NewsActivity,
                            "Error : $errorMessage occurred!",
                            Toast.LENGTH_SHORT,
                            true
                        ).show();

                    }
                }
            }
        })

        newsAdapter.setonItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articlesFragment, bundle
            )
        }
    }

    private val recyclerScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
                (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isLAtAstItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Query_DEFAULT_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotLastPage && isLAtAstItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling && newsAdapter.itemCount < 100

//            if ( dy>0) {
//                (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
//            } else if  (dy<0){
//                (activity as AppCompatActivity?)!!.supportActionBar!!.show()
//            }

            if (shouldPaginate) {
                viewModel.getBreakingNews("eg")
                isScrolling = false
            }
        }
    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(recyclerScrollListener)
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }
}