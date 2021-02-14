package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentCoronaNewsBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import com.mahmoudalim.mvvmnewsapp.util.Resource
import es.dmoral.toasty.Toasty


class CoronaNewsFragment : Fragment(R.layout.fragment_corona_news) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private val TAG = "Corona News Fragment"
    private lateinit var binding: FragmentCoronaNewsBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCoronaNewsBinding.bind(view)
        (activity as AppCompatActivity?)!!.supportActionBar!!.title = "COVID19 Latest News"
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.swipeToRefreshLayout.setOnRefreshListener {
            showProgressBar()
            viewModel.coronaNews("covid")
            setUpRecyclerView()
            hideProgressBar()
            binding.swipeToRefreshLayout.isRefreshing = false
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        binding.shimmerFrameLayout.startShimmer()

        viewModel = (activity as NewsActivity).viewModel

        viewModel.coronaNews.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> showProgressBar()

                is Resource.Success -> {
                    hideProgressBar()
                    binding.rvCoronaNews.visibility = View.VISIBLE

                    it.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    binding.errorImage.visibility = View.VISIBLE
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

        setUpRecyclerView()

        newsAdapter.setonItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_coronaNewsFragment_to_articlesFragment, bundle
            )
        }
    }


    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvCoronaNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)

        }
    }

    private fun hideProgressBar() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
    }

    private fun showProgressBar() {
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.shimmerFrameLayout.startShimmer()
    }
}