package com.example.prm03

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_layout.view.*

//Class creating the looks of the main page feed with the preview data for the articles
class ItemViewHolder(view: View, activity: FeedActivity) : RecyclerView.ViewHolder(view) {

    lateinit var picasso: Picasso
    var activity = activity

    init {
        picasso = Picasso.Builder(activity).build()

        itemView.watchBtn.setOnClickListener {
            activity.watchCLicked(adapterPosition)

            itemView.dateText.text = "Viewed"
            //bw image
            var cm = ColorMatrix()
            cm.setSaturation(0f)
            var cmcf = ColorMatrixColorFilter(cm)
            itemView.imageView.setColorFilter(cmcf)
            itemView.titleText.setTextColor(Color.GRAY)
            itemView.descriptionText.setTextColor(Color.GRAY)
            itemView.feedImage.setColorFilter(cmcf)

        }

        itemView.favBtn.setOnClickListener {

            activity.favClicked(adapterPosition)

            Toast.makeText(activity, "Added to favorites",
                Toast.LENGTH_SHORT).show()
        }

    }

    //Method to set the data appearing in a preview
    fun setInfo(feed: Feed){
        itemView.titleText.text = feed.title
        itemView.descriptionText.text = feed.desc
        itemView.dateText.text = feed.date

        //image setting
        picasso.load(feed.img_url).into(itemView.feedImage)

        itemView.favBtn.background = activity.applicationContext.getDrawable(R.drawable.ic_favorite_black_24dp)

    }
}

//Class to implement the adapter for the main page feed
//With methods according to its usage, for example, to view the items or refresh the feed
class MyAdapter(activity: FeedActivity) : RecyclerView.Adapter<ItemViewHolder>() {
    var activity: FeedActivity = activity

    var feeds = listOf<Feed>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
            .let { ItemViewHolder(it,activity) }

    }

    override fun getItemCount(): Int {
        return feeds.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.setInfo(feeds.get(position))

    }

    fun refresh(newList: List<Feed>){
        this.feeds = newList
        notifyDataSetChanged()

    }
}