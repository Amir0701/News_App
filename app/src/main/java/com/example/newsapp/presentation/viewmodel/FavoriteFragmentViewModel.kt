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

    fun getFavoriteArticles() = viewModelScope.launch(Dispatchers.IO) {
        val favArticles= articlesRepository.getFavoriteArticles()
        val mappedArticles = favArticles.map(articleMapper::toArticle)
        _favoriteArticles.postValue(mappedArticles)
    }
}