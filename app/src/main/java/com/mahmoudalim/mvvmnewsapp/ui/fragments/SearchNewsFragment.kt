package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mahmoudalim.mvvmnewsapp.*
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentSearchNewsBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import com.mahmoudalim.mvvmnewsapp.util.Constants.Companion.SEARCH_DELAY_TIME
import com.mahmoudalim.mvvmnewsapp.util.Resource
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private val TAG = "Search News Fragment"
    private lateinit var binding: FragmentSearchNewsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchNewsBinding.bind(view)

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Search News"

        binding.searchBar.slideDown(1000L, 10L)
        binding.etArrow.visibility = View.GONE
        binding.etSearch.visibility = View.GONE
        binding.IvSearch.visibility = View.VISIBLE
        binding.searchTV.visibility = View.VISIBLE


        binding.iconSearch.setOnClickListener() {
            binding.etSearch.visibility = View.VISIBLE
            binding.etArrow.visibility = View.VISIBLE
            binding.etSearch.slideInRight(100L, 10L)

            binding.searchTV.visibility = View.GONE
        }

        binding.etArrow.setOnClickListener() {
            binding.etSearch.slideOutLift(200L, 20L)
            binding.etSearch.visibility = View.GONE
            binding.searchTV.visibility = View.VISIBLE
            binding.etArrow.visibility = View.GONE
            view.hideKeyboard()
            binding.rvSearchNews.removeAllViews()
        }

        viewModel = (activity as NewsActivity).viewModel

        setUpRecyclerView()

        var coroutineJob: Job? = null
        binding.etSearch.addTextChangedListener {
            binding.rvSearchNews.visibility = View.VISIBLE
            binding.IvSearch.visibility = View.GONE

            coroutineJob?.cancel()
            coroutineJob = MainScope().launch {
                delay(SEARCH_DELAY_TIME)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        binding.searchTV.text = it.toString()
                        viewModel.searchNews(it.toString())
                        view.hideKeyboard()
                        binding.IvSearch.visibility = View.GONE

                    }
                }
            }
        }


        viewModel.searchNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> showProgressBar()

                is Resource.Success -> {
                    hideProgressBar()
                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        if (newsResponse.articles.toList().isEmpty())
                            Toasty.info(
                                activity as NewsActivity,
                                "No Result, try another Keyword!",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        binding.rvSearchNews.setPadding(0, 0, 0, 0)
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
                R.id.action_searchNewsFragment_to_articlesFragment, bundle
            )
        }

    }

    var isScrolling = false
    private val recyclerScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0 && isScrolling) {
                binding.searchBar.visibility = View.GONE
            } else {
                binding.searchBar.visibility = View.VISIBLE

            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }


    }


    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(recyclerScrollListener)

        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvSearchNews.visibility = View.VISIBLE
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDetach() {
        super.onDetach()
        onDestroyView()
    }


}
