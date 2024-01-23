package com.example.newsapp.presentation.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ActionMode
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.model.Article
import com.example.newsapp.presentation.viewmodel.HistoryFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeoutException


class HistoryFragment : Fragment() {
    val historyFragmentViewModel: HistoryFragmentViewModel by viewModel()
    private var articlesFromHistoryRecycler: RecyclerView? = null
    private val adapter = NewsAdapter()
    private var searchJob: Job? = null
    private var actionMode: ActionMode? = null
    private var selectedArticle: Article? = null
    private var selectedPosition = 0
    private var startTime: Long = 0
    private var isLongClickHandled = false

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

    private var gestureDetector: GestureDetectorCompat? = null
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
        //initGestureDetector()
        historyFragmentViewModel.getArticlesFromHistory()
        observeOnArticlesFromHistory()
        observeOnSearchedArticles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        searchJob?.cancel()
    }

    private fun setUpRecycler() {
        articlesFromHistoryRecycler?.layoutManager = LinearLayoutManager(requireContext())
        articlesFromHistoryRecycler?.adapter = adapter
        articlesFromHistoryRecycler?.addItemDecoration(LastItemMarginRecyclerDecorator())

        adapter.setOnArticleClickListener { article ->
            val bundle = Bundle()
            bundle.putSerializable("article", article)
            findNavController().navigate(R.id.action_historyFragment_to_newsDetailFragment, bundle)
        }

        adapter.setOnTouchListener { motionEvent, article, position ->
            selectedArticle = article
            selectedPosition = position
            //gestureDetector?.onTouchEvent(motionEvent) ?: false
            handleEvent(motionEvent)
            true
        }

        adapter.setOnLongClickListener { article, position ->
            selectedArticle = article
            selectedPosition = position
            startAction()
        }
    }

    private fun initGestureDetector(){
        gestureDetector = GestureDetectorCompat(requireContext(), object : GestureDetector.SimpleOnGestureListener(){
            override fun onLongPress(e: MotionEvent) {
                //super.onLongPress(e)
                startAction()
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                val bundle = Bundle()
                bundle.putSerializable("article", selectedArticle)
                findNavController().navigate(R.id.action_historyFragment_to_newsDetailFragment)
                return true
            }
        })


        gestureDetector?.setIsLongpressEnabled(true)

        //GestureDetectorCompat(requireContext(), object : GestureDetector.)
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
                    selectedArticle?.let {article ->
                        historyFragmentViewModel.deleteArticle(article)
                        articlesFromHistoryRecycler?.adapter?.notifyItemRemoved(selectedPosition)
                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.article_delete_message),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
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

    private fun handleEvent(event: MotionEvent){
        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                startTime = System.currentTimeMillis();
                isLongClickHandled = false
                handler.postDelayed(longClickRunnable, 1000L);
            }
            MotionEvent.ACTION_UP -> {
                try {
                    val duration = System.currentTimeMillis() - startTime
                    handler.removeCallbacks(longClickRunnable)
                    if (!isLongClickHandled) {
                        // Обработка одиночного клика здесь
                        val bundle = Bundle()
                        bundle.putSerializable("article", selectedArticle)
                        findNavController().navigate(R.id.action_historyFragment_to_newsDetailFragment)
                    }
                }catch (e: TimeoutException){

                }
                finally {
                    handler.removeCallbacks(longClickRunnable)
                }
            }

            MotionEvent.ACTION_CANCEL -> {
                startTime = 0
                handler.removeCallbacks(longClickRunnable)
            }
        }
    }

    val handler: Handler = Handler(Looper.getMainLooper())
    private val longClickRunnable = Runnable {
        handleLongPress()
        isLongClickHandled = true
        if (!isLongClickHandled) {
            // Обработка долгого нажатия здесь
        }
    }

    private fun handleLongPress() {
        startAction()
    }
}