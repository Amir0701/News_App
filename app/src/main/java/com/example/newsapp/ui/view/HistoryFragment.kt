package com.example.newsapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.viewmodel.HistoryFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    val historyFragmentViewModel: HistoryFragmentViewModel by viewModel()
    private var articlesFromHistoryRecycler: RecyclerView? = null
    private val adapter = NewsAdapter()
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
        setUpRecycler()
        historyFragmentViewModel.getArticlesFromHistory()
        observeOnArticlesFromHistory()
    }

    private fun setUpRecycler(){
        articlesFromHistoryRecycler?.layoutManager = LinearLayoutManager(requireContext())
        articlesFromHistoryRecycler?.adapter = adapter
    }
    private fun observeOnArticlesFromHistory(){
        historyFragmentViewModel.articlesInHistory.observe(viewLifecycleOwner) { articles ->
            Log.i("size", articles.size.toString())
            adapter.setData(articles)
        }
    }
}