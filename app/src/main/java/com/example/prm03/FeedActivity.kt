package com.example.prm03

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.android.synthetic.main.activity_feed.view.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

//Class implementing the activity of the news feed in the main menu
class FeedActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var adapter : MyAdapter

    lateinit var feeds:List<Feed>
    lateinit var favFeeds:List<Feed>
    var dbService = DatabaseService()

    //The feed is showed nicely one article at a time
    //With plenty of information in each preview
    //An user can scroll through the articles
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this)
        loadFeed()
        recyclerView.adapter = adapter
        auth = Firebase.auth
        emailText.text = auth.currentUser?.email.toString()
        favFeeds = dbService.readFavFeeds(auth.currentUser?.email.toString())

    }

    //method to refresh the adapter
    private fun refreshAdapter(list: List<Feed>) {
        adapter.refresh(list)
    }

    //method to implement the log out option
    public fun logoutClicked(view: View){
        signOut()

        finish()

        val intent = Intent(this,LoginActivity::class.java)

        startActivity(intent)
    }

    //method to perform the log out
    public fun signOut() {
        auth.signOut()

    }

    //method with options when the user clicks his own email
    public fun emailClicked(view: View){
        var alert = AlertDialog.Builder(this)
        alert.setMessage("Do you want to log out?")
        alert.setCancelable(false)
        alert.setPositiveButton("Log out", DialogInterface.OnClickListener{ dialog, which -> logoutClicked(view) })
        alert.setNegativeButton("Cancel",DialogInterface.OnClickListener{ dialog, which -> dialog.cancel() })

        var a = alert.create()
        a.setTitle("Confirm your action")
        a.show()
    }

    //method to load the news feed on the screen
    public fun loadFeed(){
        var list = MyXMLParse().execute().get()

        refreshAdapter(list)

        feeds = list
    }

    //method to implement the option to select articles as read and grey them out
    public fun watchCLicked(position: Int){
        var intent = Intent(this,BrowserActivity::class.java)

        intent.putExtra("url",feeds.get(position).link)
        intent.putExtra("email",auth.currentUser?.email)

        startActivity(intent)
    }

    //method implementing the option to choose an article as favorite
    public fun favClicked(position: Int){
        var feed = feeds.get(position)
        
        if(isInFav(feed)){
            //delete
            dbService.deleteFavorite(auth.currentUser?.email!!,feed)
        }
        else{
            //add
            dbService.addFavorite(auth.currentUser?.email!!,feed)
        }
        favFeeds = dbService.readFavFeeds(auth.currentUser?.email!!)
        refreshAdapter(feeds)
    }

    //method to check if an article is already favourite
    public fun isInFav(feed:Feed):Boolean{
        for(f in favFeeds){
            if(f.link.equals(feed.link))
                return true
        }
        return false
    }

    //method to for an user to see his favorite articles list
    fun myFavClicked(view: View){
        var intent = Intent(this,FavoritesActivity::class.java)
        intent.putExtra("email",auth.currentUser?.email.toString())

        startActivityForResult(intent,1)

    }

    //method to refresh the data shown depending on the action taken by the user
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        favFeeds = dbService.readFavFeeds(auth.currentUser?.email!!)
        refreshAdapter(feeds)
    }

}
