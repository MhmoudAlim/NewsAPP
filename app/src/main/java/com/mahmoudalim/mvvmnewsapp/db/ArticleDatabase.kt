package com.mahmoudalim.mvvmnewsapp.db

import android.content.Context
import androidx.room.*
import com.mahmoudalim.mvvmnewsapp.models.Article


@Database(
    entities = [Article::class],
    version = 2
)
@TypeConverters(RoomTypeConverter::class)
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {
    // Volatile -> other threads can see when a thread changes this ArticleDao instance
        @Volatile
        private var instance: ArticleDatabase? = null

    // LOCK -> to synchronize the DB, make sure that is only a single instance of DB at once
        private val LOCK = Any()

    //operator -> fun() that is called everytime we create an instance of the object ArticleDatabase
    // synchronized -> everything happens inside it can't be accessed by other thread at the same time
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "article_db.db"
            ).build()
    }
}