package com.mahmoudalim.mvvmnewsapp.repository

import com.mahmoudalim.mvvmnewsapp.api.RetrofitInstance
import com.mahmoudalim.mvvmnewsapp.db.ArticleDatabase

 class NewsRepository(val db: ArticleDatabase) {

  suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
   RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)


}