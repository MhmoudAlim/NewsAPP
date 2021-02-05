package com.mahmoudalim.mvvmnewsapp.ui.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mahmoudalim.mvvmnewsapp.ui.models.Article


@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(article: Article) : Int

    @Query("SELECT * FROM Articles")
    fun getAllArticles (): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}