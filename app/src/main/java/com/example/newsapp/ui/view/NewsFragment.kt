package com.example.newsapp.ui.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.common.Resource
import com.example.newsapp.ui.viewmodel.NewsFragmentViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {
    val newsFragmentViewModel: NewsFragmentViewModel by viewModel()
    private val newsAdapter = NewsAdapter()
    private var recyclerView: RecyclerView? = null
    private var tabLayout: TabLayout? = null
    private var progressBar: ProgressBar? = null

    val sharedPreferences: SharedPreferences by inject()

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
        recyclerView = view.findViewById(R.id.news_recycler)
        tabLayout = view.findViewById(R.id.category_tab_layout)
        progressBar = view.findViewById(R.id.progress_bar)
        setUpRecyclerView()
        observeOnArticles()
        newsFragmentViewModel.getArticles("politics")

        for(category in categories){
            val checkedStatus = sharedPreferences.getBoolean(resources.getString(category), false)
            if(checkedStatus){
                val newTab = tabLayout?.newTab() ?: TabLayout.Tab()
                newTab.text = resources.getString(category)
                tabLayout?.addTab(newTab)
            }
        }

        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                newsFragmentViewModel.getArticles(tab?.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.news_fragment_title)
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
                        progressBar?.visibility = View.VISIBLE
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
                            recyclerView?.scrollToPosition(0)
                        }
                    }
                }
            }
        })
    }

    private fun setUpRecyclerView(){
        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.adapter = newsAdapter

        newsAdapter.setOnArticleClickListener {
            findNavController().navigate(R.id.action_newsFragment_to_newsDetailFragment)
        }
    }
}