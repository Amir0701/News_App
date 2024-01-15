package com.example.newsapp.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.newsapp.R

class NewsDetailFragment : Fragment() {
    private var newsDetailImage: ImageView? = null
    private var newsDetailTitle: TextView? = null
    private var newsDetailDescription: TextView? = null
    private var newsDetailText: TextView? = null
    private var newsDetailPublishedAt: TextView? = null
    private var newsDetailLinkToArticle: TextView? = null
    private var newsDetailLinkDescription: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
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
}