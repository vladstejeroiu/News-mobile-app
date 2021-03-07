package com.example.prm03

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_browser.*
import kotlinx.android.synthetic.main.activity_browser.emailText
import kotlinx.android.synthetic.main.activity_feed.*

//This class is for viewing an article by going on the browser
// when clicking on an article from the soure
class BrowserActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browser)

        emailText.text = intent.getStringExtra("email")

        var url = intent.getStringExtra("url")
        webView.webViewClient = WebViewClient()
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true

    }

    //Simple method to go back from an article
    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }
        else{
            finish()
        }
    }
}
