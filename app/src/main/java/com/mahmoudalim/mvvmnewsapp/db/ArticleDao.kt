package com.mahmoudalim.mvvmnewsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahmoudalim.mvvmnewsapp.models.Article


@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles (): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}