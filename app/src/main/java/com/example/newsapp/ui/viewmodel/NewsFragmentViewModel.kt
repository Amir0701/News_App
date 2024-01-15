package com.example.newsapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Result
import com.example.newsapp.domain.repository.ArticlesRepository
import com.example.newsapp.ui.common.InternetConnection
import com.example.newsapp.ui.common.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsFragmentViewModel(
    private val articlesRepository: ArticlesRepository,
    private val internetConnection: InternetConnection
): ViewModel() {
    private val _articlesLiveData: MutableLiveData<Resource<Result>> = MutableLiveData()
    val articlesLiveData: LiveData<Resource<Result>> = _articlesLiveData

    var selectedCategory: String = ""
    fun getArticles(q: String, page: Int = 1) = viewModelScope.launch(Dispatchers.IO){
        if(internetConnection.hasInternetConnection()){
            _articlesLiveData.postValue(Resource.Loading())
            val response = articlesRepository.getArticles(q, page)
            val resource = getResponse(response)
            _articlesLiveData.postValue(resource)
        }
        else{
            _articlesLiveData.postValue(Resource.NoInternetConnection())
        }
    }

    private fun <T>getResponse(response: Response<T>): Resource<T>{
        if(response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message(), null)
    }
}