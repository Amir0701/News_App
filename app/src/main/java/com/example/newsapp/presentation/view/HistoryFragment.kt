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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.presentation.viewmodel.HistoryFragmentViewModel
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    val historyFragmentViewModel: HistoryFragmentViewModel by viewModel()
    private var articlesFromHistoryRecycler: RecyclerView? = null
    private val adapter = NewsAdapter()
    private var searchJob: Job? = null

    private val menuProvider = object: MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if(menuItem.itemId == R.id.search){
                val searchView = menuItem.actionView as SearchView?
                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        historyFragmentViewModel.isSearched = true
                        searchJob?.cancel()
                        searchJob = historyFragmentViewModel.searchArticles(query ?: "")
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                })
            }

            return false
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as  MainActivity).supportActionBar?.title = resources.getString(R.string.history_fragment_title)
        articlesFromHistoryRecycler = view.findViewById(R.id.article_history_recycler)
        requireActivity().addMenuProvider(menuProvider)
        setUpRecycler()
        historyFragmentViewModel.getArticlesFromHistory()
        observeOnArticlesFromHistory()
        observeOnSearchedArticles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        searchJob?.cancel()
    }

    private fun setUpRecycler(){
        articlesFromHistoryRecycler?.layoutManager = LinearLayoutManager(requireContext())
        articlesFromHistoryRecycler?.adapter = adapter
    }
    private fun observeOnArticlesFromHistory(){
        historyFragmentViewModel.articlesInHistory.observe(viewLifecycleOwner) { articles ->
            adapter.setData(articles)
        }
    }

    private fun observeOnSearchedArticles(){
        historyFragmentViewModel.searchedArticles.observe(viewLifecycleOwner){articles->
            articles?.let {
                if(historyFragmentViewModel.isSearched){
                    adapter.setData(it)
                    historyFragmentViewModel.isSearched = false
                }
            }
        }
    }
}