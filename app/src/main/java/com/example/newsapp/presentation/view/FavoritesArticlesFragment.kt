package com.example.newsapp.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import com.example.newsapp.presentation.viewmodel.FavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesArticlesFragment : Fragment() {
    val favoriteFragmentViewModel: FavoriteFragmentViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.favorites_fragment_title)
        observeOnFavoriteArticles()
    }

    private fun observeOnFavoriteArticles(){
        favoriteFragmentViewModel.favoriteArticles.observe(viewLifecycleOwner){favoriteArticles->
            Log.i("TAG", favoriteArticles.size.toString())
        }
    }
}