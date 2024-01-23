package com.example.newsapp.presentation.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.data.model.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val articleImageView: ImageView = itemView.findViewById(R.id.news_image)
        private val articleTitle: TextView = itemView.findViewById(R.id.news_title)
        private val articleDescription: TextView = itemView.findViewById(R.id.news_description)
        fun bind(article: Article){
            articleImageView.clipToOutline = true
            Glide
                .with(articleImageView.context)
                .load(article.urlToImage)
                .into(articleImageView)

            articleTitle.text = article.title
            articleDescription.text = article.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_card, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDiffer.currentList.size
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentArticle = listDiffer.currentList[position]
        holder.bind(currentArticle)

        holder.itemView.setOnClickListener {
            onArticleClickListener?.invoke(currentArticle)
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(currentArticle, position) ?: false
            true
        }

        holder.itemView.setOnTouchListener { view, motionEvent ->
            onTouchListener?.invoke(motionEvent, currentArticle, position) ?: false
        }
    }

    fun setData(articles: List<Article>){
        listDiffer.submitList(articles)
    }

    private val diffUtil: DiffUtil.ItemCallback<Article> = object: DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    private val listDiffer = AsyncListDiffer<Article>(this, diffUtil)

    private var onArticleClickListener: ((article: Article) -> Unit)? = null
    fun setOnArticleClickListener(clickListener: (article: Article) -> Unit){
        onArticleClickListener = clickListener
    }

    private var onTouchListener: ((motionEvent: MotionEvent, article: Article, position: Int) -> Boolean)? = null
    fun setOnTouchListener(onTouchListener: (motionEvent: MotionEvent, article: Article, position: Int) -> Boolean){
        this.onTouchListener = onTouchListener
    }

    private var onLongClickListener:((article: Article, position: Int) -> Unit)? = null

    fun setOnLongClickListener(onLongClickListener: (article: Article, position: Int) -> Unit){
        this.onLongClickListener = onLongClickListener
    }

    fun removeAt(pos: Int){
        val list = listDiffer.currentList.toMutableList()
        list.removeAt(pos)
        listDiffer.submitList(list)
    }
}