package com.example.prm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_favorites.*

//Class for implementing the activity of the favorites articles feed
class FavoritesActivity : AppCompatActivity() {

    lateinit var favAdapter : MyFavAdapter
    lateinit var favFeeds:List<Feed>
    lateinit var email:String

    var dbService = DatabaseService()

    //A nice way to shown the articles, same as in the main menu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        email = intent.getStringExtra("email").toString()

        favFeeds = dbService.readFavFeeds(email)

        favrecyclerView.layoutManager = LinearLayoutManager(this)
        favAdapter = MyFavAdapter(this)
        favrecyclerView.adapter = favAdapter

        refreshFavAdapter(favFeeds)
    }

    //method to return when bakc is pressed
    override fun onBackPressed() {
        super.onBackPressed()
        finishActivity(1)
    }

    //method to refresh the adapter
    private fun refreshFavAdapter(list: List<Feed>) {
        favAdapter.refresh(list)
    }

    //method to implement the option to select articles as read and grey them out
    public fun watchCLicked(position: Int){
        var intent = Intent(this,BrowserActivity::class.java)

        intent.putExtra("url",favFeeds.get(position).link)
        intent.putExtra("email",email)

        startActivity(intent)
    }

    //method implementing the option to choose an article as favorite
    //and deselecting them as a favorite
    public fun favClicked(position: Int){
        var feed = favFeeds.get(position)


        //delete
        dbService.deleteFavorite(email,feed)

        favFeeds = dbService.readFavFeeds(email)

        refreshFavAdapter(favFeeds)
    }

    //method implementing the notifications button option
    public fun NotClicked(position: Int){
       NotificationActivity().startActivity(intent)
}
}
