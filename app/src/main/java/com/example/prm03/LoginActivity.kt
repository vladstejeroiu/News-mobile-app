package com.example.prm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.sign

//Class implementing the login page functionality using firebase
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
    }

    //method for UI
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    //method to update the UI depending on type of user
    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null){
            //the user is checked as ok
            val intent = Intent(this,FeedActivity::class.java)

            startActivity(intent)
            finish()

        }
        else{
            //BAD case, something here was not necessary
            //could put a nice message in the future
        }

    }

    //Method for the user to create an account
    private fun createAccout(email: String, password: String){

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Success!",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Toast.makeText(baseContext, "Authentication failed.\n ${task.exception}",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    //Method for signing in, when an user already creted an account
    private fun signIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(baseContext, "Success!",
                        Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Toast.makeText(baseContext, "$task.exception",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    //Method with the option going forward and clicking the login option
    public fun loginClicked(view: View){
        if(emailText.text.length != 0 && passText.text.length != 0) {
            var email = emailText.text.toString()
            var pass = passText.text.toString()
            if (checkBox.isChecked) {
                //registration
                createAccout(email, pass)

            } else {
                //login
                signIn(email, pass)
            }
        }
        else{
            Toast.makeText(baseContext, "Type all data!",
                Toast.LENGTH_LONG).show()
        }
    }
}
