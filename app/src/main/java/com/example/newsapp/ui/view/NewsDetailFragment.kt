package com.example.newsapp.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article

class NewsDetailFragment : Fragment() {
    private var newsDetailImage: ImageView? = null
    private var newsDetailTitle: TextView? = null
    private var newsDetailDescription: TextView? = null
    private var newsDetailText: TextView? = null
    private var newsDetailPublishedAt: TextView? = null
    private var newsDetailLinkToArticle: TextView? = null
    private var newsDetailLinkDescription: TextView? = null
    private var actionBar: ActionBar? = null
    private val navArg by navArgs<NewsDetailFragmentArgs>()

    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == android.R.id.home) {
                Log.i("Home", "Home clicked")
                findNavController().navigateUp()
            }

            return true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(menuProvider)
        actionBar = (activity as MainActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        initViews(view)
        setData()
    }

    private fun initViews(view: View){
        newsDetailImage = view.findViewById(R.id.detail_news_image)
        newsDetailTitle = view.findViewById(R.id.detail_news_title)
        newsDetailDescription = view.findViewById(R.id.detail_news_description)
        newsDetailText = view.findViewById(R.id.detail_news_text)
        newsDetailLinkToArticle = view.findViewById(R.id.detail_news_link_to_article)
        newsDetailPublishedAt = view.findViewById(R.id.detail_news_published_at)
        newsDetailLinkDescription = view.findViewById(R.id.detail_news_link_desc)
    }

    private fun setData(){
        val selectedArticle = navArg.article
        Glide.with(requireContext())
            .load(selectedArticle.urlToImage)
            .into(newsDetailImage!!)

        newsDetailTitle?.text = selectedArticle.title
        newsDetailDescription?.text = selectedArticle.description
        newsDetailText?.text = selectedArticle.content
        newsDetailLinkDescription?.text = "Read about article there"
        newsDetailPublishedAt?.text = selectedArticle.publishedAt
        newsDetailLinkToArticle?.text = selectedArticle.url
    }
    override fun onDestroyView() {
        super.onDestroyView()
        actionBar?.setDisplayHomeAsUpEnabled(false)
        requireActivity().removeMenuProvider(menuProvider)
    }
}