package com.example.newsapp.presentation.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.presentation.common.Resource
import com.example.newsapp.presentation.viewmodel.NewsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {
    val newsFragmentViewModel: NewsFragmentViewModel by viewModel()
    private val newsAdapter = NewsAdapter()
    private var recyclerView: RecyclerView? = null
    private var tabLayout: TabLayout? = null
    private var progressBar: ProgressBar? = null
    private var constraintLayout: ConstraintLayout? = null
    private var searchJob: Job? = null
    private var fetchJob: Job? = null

    val sharedPreferences: SharedPreferences by inject()

    private val menuProvider = object : MenuProvider{
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if(menuItem.itemId == R.id.search){
                val searchView = menuItem.actionView as androidx.appcompat.widget.SearchView?

                searchView?.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        searchJob?.cancel()
                        p0?.let {query ->
                            searchJob = newsFragmentViewModel.getArticles(query)
                        }

                        return true
                    }

                    override fun onQueryTextChange(p0: String?): Boolean {
                        return false
                    }
                })
            }

            return false
        }
    }
    private val categories = listOf(
        R.string.politics,
        R.string.sport,
        R.string.nature,
        R.string.science
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setUpRecyclerView()
        observeOnArticles()
        setUpTabLayout()
        requireActivity().addMenuProvider(menuProvider)
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.news_fragment_title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().removeMenuProvider(menuProvider)
        searchJob?.cancel()
        fetchJob?.cancel()
        fetchJob = null
        searchJob = null
    }
    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.news_recycler)
        tabLayout = view.findViewById(R.id.category_tab_layout)
        progressBar = view.findViewById(R.id.progress_bar)
        constraintLayout = view.findViewById(R.id.parent_constraint)
    }
    private fun observeOnArticles(){
        newsFragmentViewModel.articlesLiveData.observe(viewLifecycleOwner, Observer {
            newsFragmentViewModel.articlesLiveData.value?.let { resource->
                when(resource){
                    is Resource.Error -> {
                        progressBar?.visibility = View.GONE
                        Log.i("error", resource.message.toString())
                        Snackbar.make(requireView(), resource.message.toString(), Snackbar.LENGTH_LONG).show()
                    }

                    is Resource.Loading -> {
                        if(fetchJob?.isActive == true || searchJob?.isActive == true){
                            progressBar?.visibility = View.VISIBLE
                        }
                        else{
                            progressBar?.visibility = View.GONE
                        }
                    }

                    is Resource.NoInternetConnection -> {
                        progressBar?.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            resources.getString(R.string.no_internet_connection),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Success -> {
                        progressBar?.visibility = View.GONE
                        resource.data?.let { result->
                            newsAdapter.setData(result.articles)
                        }
                    }
                }
            }
        })
    }

    private fun setUpRecyclerView(){
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = newsAdapter

        newsAdapter.setOnArticleClickListener {selectedArticle->
            val bundle = Bundle()
            bundle.putSerializable("article", selectedArticle)
            findNavController().navigate(R.id.action_newsFragment_to_newsDetailFragment, bundle)
        }
    }

    private fun setUpTabLayout(){
        var selectedTab: TabLayout.Tab? = null

        for(category in categories){
            val checkedStatus = sharedPreferences.getBoolean(resources.getString(category), false)
            if(checkedStatus){
                val categoryTitle = resources.getString(category)
                val newTab = tabLayout?.newTab() ?: TabLayout.Tab()
                newTab.text = categoryTitle
                tabLayout?.addTab(newTab)

                if(newsFragmentViewModel.selectedCategory == categoryTitle){
                    selectedTab = newTab
                }
            }
        }

        if(selectedTab != null)
            tabLayout?.selectTab(selectedTab)
        else
            newsFragmentViewModel.selectedCategory = ""

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                fetchJob = newsFragmentViewModel.getArticles(tab?.text.toString())
                newsFragmentViewModel.selectedCategory = tab?.text.toString()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        tabLayout?.let {tabLayout ->
            fetchJob?.cancel()
            if(tabLayout.tabCount > 0){
                if(newsFragmentViewModel.selectedCategory.isEmpty()){
                    val tab = tabLayout.getTabAt(tabLayout.selectedTabPosition)
                    fetchJob = newsFragmentViewModel.getArticles(tab?.text.toString())
                    newsFragmentViewModel.selectedCategory = tab?.text.toString()
                } else {

                }
            }
            else{
                tabLayout.visibility = View.INVISIBLE
                changeRecyclerViewConstraints()
                fetchJob = newsFragmentViewModel.getArticles("all")
            }
        }
    }

    private fun changeRecyclerViewConstraints(){
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            R.id.news_recycler,
            ConstraintSet.TOP,
            R.id.parent_constraint,
            ConstraintSet.TOP,
            0
        )
        constraintSet.applyTo(constraintLayout)
    }
}