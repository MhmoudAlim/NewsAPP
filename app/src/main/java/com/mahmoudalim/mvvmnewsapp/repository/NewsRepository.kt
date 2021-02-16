package com.mahmoudalim.mvvmnewsapp.repository

import com.mahmoudalim.mvvmnewsapp.api.RetrofitInstance
import com.mahmoudalim.mvvmnewsapp.db.ArticleDatabase
import com.mahmoudalim.mvvmnewsapp.models.Article

class NewsRepository(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchNews(searchQuery, pageNumber)

    suspend fun coronaNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.coronaNews(searchQuery, pageNumber)


    suspend fun insertOrUpdate(article: Article) =
        db.getArticleDao().insertOrUpdate(article)

    //no need for a suspend function because it returns a LiveData
    fun getSavedNews() = db.getArticleDao().getAllArticles()


    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}

