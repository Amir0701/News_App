package com.example.newsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.model.Article
import com.example.newsapp.domain.repository.ArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragmentViewModel(
    private val articlesRepository: ArticlesRepository,
    private val mapper: ArticleMapper
): ViewModel() {
    private val _articlesInHistory = MutableLiveData<List<Article>>()
    val articlesInHistory: LiveData<List<Article>> = _articlesInHistory

    init {
        getArticlesFromHistory()
    }

    fun getArticlesFromHistory() = viewModelScope.launch(Dispatchers.IO){
        val articlesFromHistory = articlesRepository.getArticlesFromHistory()
        val articles = articlesFromHistory.map(mapper::toArticle)
        _articlesInHistory.postValue(articles)
    }
}