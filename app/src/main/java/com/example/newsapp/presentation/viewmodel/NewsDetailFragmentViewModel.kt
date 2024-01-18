package com.example.newsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.mapper.ArticleMapper
import com.example.newsapp.data.model.Article
import com.example.newsapp.domain.repository.ArticlesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsDetailFragmentViewModel(private val articlesRepository: ArticlesRepository,
                                  private val articleMapper: ArticleMapper): ViewModel() {
    fun addArticleToHistory(article: Article) = viewModelScope.launch(Dispatchers.IO){
        val mappedArticleEntity = articleMapper.toArticleEntity(article)
        val articleEntity = mappedArticleEntity.copy(isInHistory = true)
        articlesRepository.addArticleToHistory(articleEntity)
    }
}