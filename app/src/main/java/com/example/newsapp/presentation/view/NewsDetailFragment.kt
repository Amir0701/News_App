package com.example.newsapp.presentation.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
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
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.appcompat.view.menu.MenuItemImpl
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.presentation.viewmodel.NewsDetailFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

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
    private var isFavorite = false
    val newsDetailViewModel: NewsDetailFragmentViewModel by viewModel()
    private var menuItem: MenuItem? = null
    private var menus: Menu? = null

    private val menuProvider = object: MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
            menuInflater.inflate(R.menu.favorite_menu, menu)
            menus = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when(menuItem.itemId){
                android.R.id.home ->{
                    findNavController().navigateUp()
                }

                R.id.favorite ->{
                    //newsDetailViewModel.isFavorite = !newsDetailViewModel.isFavorite
                    isFavorite = !isFavorite
                    if(newsDetailViewModel.isFavorite.value == true){
                        menuItem.setIcon(R.drawable.star_clicked)
                    }
                    else
                        menuItem.setIcon(R.drawable.star)
                }
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
        observeOnIsFavoriteFlag()
    }

    override fun onStart() {
        super.onStart()
        newsDetailViewModel.isInFavorite(navArg.article.url)
        newsDetailViewModel.addArticleToHistory(navArg.article)
    }

    private fun initViews(view: View){
        newsDetailImage = view.findViewById(R.id.detail_news_image)
        newsDetailTitle = view.findViewById(R.id.detail_news_title)
        newsDetailDescription = view.findViewById(R.id.detail_news_description)
        newsDetailText = view.findViewById(R.id.detail_news_text)
        newsDetailLinkToArticle = view.findViewById(R.id.detail_news_link_to_article)
        newsDetailPublishedAt = view.findViewById(R.id.detail_news_published_at)
        newsDetailLinkDescription = view.findViewById(R.id.detail_news_link_desc)
        menuItem = view.findViewById(R.id.favorite)
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

    @SuppressLint("RestrictedApi")
    private fun observeOnIsFavoriteFlag(){
        newsDetailViewModel.isFavorite.observe(viewLifecycleOwner) {isFavorite ->
            this.isFavorite = isFavorite
            Log.i("TAG", "Fav")
//            val men = ActionMenuItem(requireContext(), ActionMenuItem.SHOW_AS_ACTION_ALWAYS, R.id.favorite,
//                MenuItem.SHOW_AS_ACTION_ALWAYS, ActionMenuItem.SHOW_AS_ACTION_ALWAYS, "Favorite")
//            menuProvider.onMenuItemSelected(men)

            val menIt = menus?.getItem(R.id.favorite)
            menuItem?.let{
                Log.i("TAG", "Menu")
            }

            //(activity as MainActivity).supportActionBar?.
            if(isFavorite)
                menIt?.setIcon(R.drawable.star_clicked)
            else
                menIt?.setIcon(R.drawable.star)
        }
    }
}