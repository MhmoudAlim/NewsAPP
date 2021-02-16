package com.mahmoudalim.mvvmnewsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mahmoudalim.mvvmnewsapp.util.MyApplication
import com.mahmoudalim.mvvmnewsapp.models.Article
import com.mahmoudalim.mvvmnewsapp.models.NewsResponse
import com.mahmoudalim.mvvmnewsapp.repository.NewsRepository
import com.mahmoudalim.mvvmnewsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()

    // pagination is done here not in Fragment so that current page number don't reset in case of rotation
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    val coronaNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var coronaNewsPage = 1
    var coronaNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("eg")
        coronaNews("covid")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)

    }

    fun coronaNews(searchQuery: String) = viewModelScope.launch {
        safeCoronaNewsCall(searchQuery)

    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
       safeSearchNewsCall(searchQuery)
    }


    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { breakingResultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = breakingResultResponse
                } else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = breakingResultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: breakingResultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { searchResultResponse ->
                return Resource.Success(searchNewsResponse ?: searchResultResponse)
            }
        }
        return Resource.Error(response.message())

    }


    private fun handCoronaNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { coronaResultResponse ->
                return Resource.Success(coronaNewsResponse ?: coronaResultResponse)
            }
        }
        return Resource.Error(response.message())

    }

    private suspend fun safeSearchNewsCall(searchQuery: String){
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            }else{
                searchNews.postValue(Resource.Error("Network Error"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> searchNews.postValue(Resource.Error("Network Error"))
                else -> searchNews.postValue(Resource.Error("An Error Occurred"))
            }}
    }

    private suspend fun safeCoronaNewsCall(searchQuery: String){
        coronaNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.coronaNews(searchQuery, coronaNewsPage)
                coronaNews.postValue(handCoronaNewsResponse(response))
            }else{
                coronaNews.postValue(Resource.Error("Network Error"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> coronaNews.postValue(Resource.Error("Network Error"))
                else -> coronaNews.postValue(Resource.Error("An Error Occurred"))
            }}
    }


    private suspend fun safeBreakingNewsCall(countryCode: String){
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))
            }else{
                breakingNews.postValue(Resource.Error("Network Error"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Error"))
                else -> breakingNews.postValue(Resource.Error("An Error Occurred"))
        }}
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.insertOrUpdate(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

//        for api level 23 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                else -> false
            }
//            for api level < 23
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_MOBILE -> true
                    TYPE_WIFI -> true
                    else -> false
                }
            }
        }
        return false
    }

}