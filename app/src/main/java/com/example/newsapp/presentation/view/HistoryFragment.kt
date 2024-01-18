package com.example.newsapp.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
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
    private var actionMode: ActionMode? = null

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

    private var gestureDetector: GestureDetector? = null
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

        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener(){
            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                startAction()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        searchJob?.cancel()
    }

    private fun setUpRecycler(){
        articlesFromHistoryRecycler?.layoutManager = LinearLayoutManager(requireContext())
        articlesFromHistoryRecycler?.adapter = adapter
        adapter.setOnTouchListener {motionEvent ->
            gestureDetector?.onTouchEvent(motionEvent) ?: false
        }
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

    private fun startAction(){
        actionMode = requireActivity().startActionMode(object : ActionMode.Callback{
            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                (activity as MainActivity).supportActionBar?.hide()
                requireActivity().menuInflater.inflate(R.menu.delete_menu, p1)
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                if(p1?.itemId == R.id.delete){
                    p0?.finish()
                }
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                actionMode = null
                (activity as MainActivity).supportActionBar?.show()
            }
        })
    }
}