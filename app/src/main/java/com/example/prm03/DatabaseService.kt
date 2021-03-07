package com.example.prm03

import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

//Class implementing database service using Firebase
//Mainly for saving articles in the favorites category
class DatabaseService() {

    var database = FirebaseDatabase.getInstance()
    var list = mutableListOf<Feed>()

//This method get the user and the article and adds the respective article to
    //the user's favorites list
    fun addFavorite(email: String,feed: Feed){
        database = FirebaseDatabase.getInstance()
        var reference = database.getReference("Users")

        var userRef = reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                //method not implemented because not needed yet
                // Future point of reference, for possible errors
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    var key = getFeedKey(feed)
                    reference.child(getUserKey(email)).child("Feeds").child(key).setValue(feed)
                }
                else{
                    var user_key = getUserKey(email)
                    reference.child(user_key).push().child("email")
                    reference.child(user_key).child("email").setValue(email)
                    var key = getFeedKey(feed)
                    reference.child(user_key).child("Feeds").child(key).setValue(feed)
                }
            }

    })
    }

    //method to delete an articles from the favorites list
    fun deleteFavorite(email: String,feed: Feed){
        database = FirebaseDatabase.getInstance()
        var reference = database.getReference("Users")


        reference.child(getUserKey(email)).child("Feeds").child(getFeedKey(feed)).removeValue()
        list.remove(feed)
    }

    //Method to show the list with the saved articles as favorites
    fun readFavFeeds(email: String):List<Feed>{
        database = FirebaseDatabase.getInstance()
        var reference = database.getReference("Users")
        var feedsRef = reference.child(getUserKey(email)).child("Feeds").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                //method not implemented because not needed yet
                // Future point of reference, for possible errors
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for(nextSnapshot in snapshot.children){
                    var feed = nextSnapshot.getValue(Feed::class.java)
                    list.add(feed!!)
                }
            }
        })
        return list
    }


  //gets the special key assigned to a user
    fun getUserKey(email: String):String{
        var res = email.replace("@","").replace(".","")
        return res
    }

    //Gets the key assigned to current feed
    fun getFeedKey(feed:Feed):String{
        var res = feed.link.substring(feed.link.length-10,feed.link.length).replace(".","")
        return res
    }
}