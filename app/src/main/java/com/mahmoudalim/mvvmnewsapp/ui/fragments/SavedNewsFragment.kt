package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.dapter.NewsAdapter
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentArticleBinding
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentSavedNewsBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news){

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSavedNewsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSavedNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        setUpRecyclerView()

        newsAdapter.setonItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article" , it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articlesFragment,bundle)
        }


        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })
    }

    private fun setUpRecyclerView() {

        newsAdapter = NewsAdapter()
        binding.rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}