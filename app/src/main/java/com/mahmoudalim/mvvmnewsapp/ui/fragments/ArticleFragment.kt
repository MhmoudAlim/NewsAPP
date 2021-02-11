package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentArticleBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel


class ArticleFragment : Fragment(R.layout.fragment_article){

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        binding.webView
        binding.fab
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

     binding.fab.setOnClickListener {
         viewModel.saveArticle(article)
         Snackbar.make(view , "Article Saved Successfully" , Snackbar.LENGTH_SHORT).show()
     }

    }
}