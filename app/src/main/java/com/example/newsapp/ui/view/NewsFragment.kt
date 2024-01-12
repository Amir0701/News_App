package com.example.newsapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.common.Resource
import com.example.newsapp.ui.viewmodel.NewsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {
    val newsFragmentViewModel: NewsFragmentViewModel by viewModel()
    private val newsAdapter = NewsAdapter()
    private var recyclerView: RecyclerView? = null
    private var tabLayout: TabLayout? = null
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
        setUpRecyclerView()
        observeOnArticles()
        newsFragmentViewModel.getArticles("politics")
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

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.NoInternetConnection -> {

                    }

                    is Resource.Success -> {
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
    }
}