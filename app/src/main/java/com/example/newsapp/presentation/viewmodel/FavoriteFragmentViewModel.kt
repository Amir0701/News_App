package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.model.Article
import com.example.newsapp.domain.repository.ArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteFragmentViewModel(
    private val articlesRepository: ArticlesRepository,
    private val articleMapper: ArticleMapper) : ViewModel() {
    private val _favoriteArticles = MutableLiveData<List<Article>>()
    val favoriteArticles: LiveData<List<Article>> = _favoriteArticles
    private var allFavoriteArticles: List<Article> = emptyList()

    fun getFavoriteArticles() = viewModelScope.launch(Dispatchers.IO) {
        val favArticles= articlesRepository.getFavoriteArticles()
        val mappedArticles = favArticles.map(articleMapper::toArticle)
        _favoriteArticles.postValue(mappedArticles)
        allFavoriteArticles = mappedArticles
    }

    fun searchArticlesInFavorites(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val filteredArticles = mutableListOf<Article>()
        allFavoriteArticles.forEach{ article ->
            if(article.title.contains(query, ignoreCase = true) ||
                article.description.contains(query, ignoreCase = true) ||
                article.content.contains(query, ignoreCase = true)
            ){
                filteredArticles.add(article)
            }
        }

        _favoriteArticles.postValue(filteredArticles)
    }
}