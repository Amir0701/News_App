package com.example.newsapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.newsapp.R
import com.example.newsapp.ui.viewmodel.HistoryFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment() {
    val historyFragmentViewModel: HistoryFragmentViewModel by viewModel()
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
        observeOnArticlesFromHistory()
    }

    private fun observeOnArticlesFromHistory(){
        historyFragmentViewModel.articlesInHistory.observe(viewLifecycleOwner) { articles ->
            Log.i("size", articles.size.toString())
        }
    }
}