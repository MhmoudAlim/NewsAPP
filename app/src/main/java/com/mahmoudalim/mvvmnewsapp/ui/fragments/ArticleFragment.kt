package com.mahmoudalim.mvvmnewsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.mahmoudalim.mvvmnewsapp.R
import com.mahmoudalim.mvvmnewsapp.databinding.FragmentArticleBinding
import com.mahmoudalim.mvvmnewsapp.ui.NewsActivity
import com.mahmoudalim.mvvmnewsapp.ui.NewsViewModel
import es.dmoral.toasty.Toasty


class ArticleFragment : Fragment(R.layout.fragment_article){

    lateinit var viewModel: NewsViewModel
    val args: ArticleFragmentArgs by navArgs()
    private lateinit var binding: FragmentArticleBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding.webView
        binding.fab
        binding.articlesProgress
        viewModel = (activity as NewsActivity).viewModel

        val article = args.article

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            binding.articlesProgress.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE

        }

        binding.fab.setOnClickListener {
         viewModel.saveArticle(article)
         Toasty.success(activity as NewsActivity, "Article Saved Successfully", Toast.LENGTH_SHORT, true).show();

     }

    }

    override fun onDetach() {
        super.onDetach()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }
}