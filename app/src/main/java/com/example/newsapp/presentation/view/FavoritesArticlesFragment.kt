package com.example.newsapp.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.presentation.viewmodel.FavoriteFragmentViewModel
import kotlinx.coroutines.Job
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesArticlesFragment : Fragment() {
    private val favoriteFragmentViewModel: FavoriteFragmentViewModel by viewModel()
    private var recyclerView: RecyclerView? = null
    private val adapter: NewsAdapter by inject()
    private var searchJob: Job? = null

    private val menuProvider = object: MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                R.id.search ->{
                    val searchView: SearchView = menuItem.actionView as SearchView
                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            searchJob?.cancel()
                            query?.let {q->
                                searchJob = favoriteFragmentViewModel.searchArticlesInFavorites(q)
                            }
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            return false
                        }
                    })
                    return true
                }
            }
            return false
        }
    }
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
        initViews(view)
        setUpRecyclerView()
        observeOnFavoriteArticles()
        favoriteFragmentViewModel.getFavoriteArticles()
        requireActivity().addMenuProvider(menuProvider)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        searchJob?.cancel()
    }

    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.favorite_recycler)
    }

    private fun setUpRecyclerView(){
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        recyclerView?.adapter = adapter
        recyclerView?.addItemDecoration(LastItemMarginRecyclerDecorator())
        adapter.setOnArticleClickListener {article ->
            val bundle = Bundle()
            bundle.putSerializable("article", article)
            findNavController().navigate(R.id.action_favoritesArticlesFragment_to_newsDetailFragment, bundle)
        }
    }

    private fun observeOnFavoriteArticles(){
        favoriteFragmentViewModel.favoriteArticles.observe(viewLifecycleOwner){favoriteArticles->
            adapter.setData(favoriteArticles)
        }
    }
}