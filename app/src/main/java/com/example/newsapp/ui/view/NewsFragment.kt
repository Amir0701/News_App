package com.example.newsapp.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.newsapp.R
import com.example.newsapp.ui.common.Resource
import com.example.newsapp.ui.viewmodel.NewsFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment : Fragment() {
    val newsFragmentViewModel: NewsFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnArticles()
        newsFragmentViewModel.getArticles("politics")
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
                        Log.i("TAG", "In Success")
                        resource.data?.let { result->
                            Snackbar.make(requireView(), "Data was read", Snackbar.LENGTH_LONG).show()
                            Log.i("TAG", result.articles[0].title)
                        }
                    }
                }
            }
        })
    }
}