package com.example.newsapp.presentation.viewmodel

import android.util.Log
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
    private val _searchedArticles = MutableLiveData<List<Article>>()
    val searchedArticles: LiveData<List<Article>> = _searchedArticles
    var isSearched = false
    private var histories: List<Article> = emptyList()
    fun getArticlesFromHistory() = viewModelScope.launch(Dispatchers.IO){
        val articlesFromHistory = articlesRepository.getArticlesFromHistory()
        val articles = articlesFromHistory.map(mapper::toArticle)
        _articlesInHistory.postValue(articles)
        histories = articles
    }

    fun searchArticles(query: String) = viewModelScope.launch(Dispatchers.IO){
        val filteredArticlesList = mutableListOf<Article>()
        histories.forEach {article ->
            if(article.title.contains(query, ignoreCase = true) ||
                article.description.contains(query, ignoreCase = true) ||
                article.content.contains(query, ignoreCase = true)
            ){
                filteredArticlesList.add(article)
            }
        }

        _searchedArticles.postValue(filteredArticlesList)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        val articleEntity = mapper.toArticleEntity(article)
        articlesRepository.deleteArticle(articleEntity)
    }
}