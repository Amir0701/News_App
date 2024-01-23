package com.example.newsapp.presentation.viewmodel

import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.data.db.model.ArticleEntity
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
    private var histories: List<ArticleEntity> = emptyList()
    private var historyMap: MutableMap<String, ArticleEntity> = LinkedHashMap()
    fun getArticlesFromHistory() = viewModelScope.launch(Dispatchers.IO){
        val articlesFromHistory = articlesRepository.getArticlesFromHistory()
        val articles = articlesFromHistory.map(mapper::toArticle)
        _articlesInHistory.postValue(articles)
        histories = articlesFromHistory
        articlesFromHistory.forEach { articleEntity ->
            historyMap[articleEntity.url] = articleEntity
        }
    }

    fun searchArticles(query: String) = viewModelScope.launch(Dispatchers.IO){
        val filteredArticlesList = mutableListOf<Article>()

        historyMap.forEach { (s, articleEntity) ->
            if(articleEntity.title.contains(query, ignoreCase = true) ||
                articleEntity.description.contains(query, ignoreCase = true) ||
                articleEntity.content.contains(query, ignoreCase = true)
            ){
                filteredArticlesList.add(mapper.toArticle(articleEntity))
            }
        }

        _searchedArticles.postValue(filteredArticlesList)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        val articleEntity = historyMap.getValue(article.url)
        if(articleEntity.isInFavorite){
            val deletedArticle = articleEntity.copy(isInHistory = false)
            articlesRepository.addArticleToHistory(deletedArticle)
        }else{
            articlesRepository.deleteArticle(articleEntity)
        }
        historyMap.remove(articleEntity.url)
    }

    fun deleteArticles(articles: List<Article>) = viewModelScope.launch(Dispatchers.IO) {
        articles.forEach {article ->
            val art = historyMap.getValue(article.url)
            if(art.isInFavorite){
                val deleteArticle = art.copy(isInHistory = false)
                launch {
                    articlesRepository.addArticleToHistory(deleteArticle)
                }
            }
            else{
                launch {
                    articlesRepository.deleteArticle(art)
                }
            }

            historyMap.remove(art.url)
        }
    }
}