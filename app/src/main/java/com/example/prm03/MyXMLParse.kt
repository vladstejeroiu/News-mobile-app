package com.example.prm03

import android.os.AsyncTask
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.URL

//Class the parse the data coming the news source, transformed as an xml
class MyXMLParse(): AsyncTask<Void, Void, List<Feed>>() {

    lateinit var url: URL
    lateinit var parser: XmlPullParser
    lateinit var inputStream: InputStream
    lateinit var feed: Feed

//Metohd to parse the data and sort it depending on its nature type and role in the article
    override fun doInBackground(vararg params: Void?): List<Feed> {
      // url = URL("http://newsapi.org/v2/everything?q=bitcoin&from=
      // 2020-10-18&sortBy=publishedAt&apiKey=7ecc2189b0044a509fafb2bba5d15b7e")

        url = URL("https://www.buzzfeed.com/world.xml")

        parser = XmlPullParserFactory.newInstance().newPullParser()
        inputStream = url.openConnection().getInputStream()

        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(inputStream, null);

        var feeds = mutableListOf<Feed>()

        var eventType = parser.eventType

        while (eventType != XmlPullParser.END_DOCUMENT){
            var tag_name = ""

            when(eventType){
                XmlPullParser.START_TAG -> {
                    tag_name = parser.name

                    when(tag_name){
                        "item" -> {feed = Feed("","","","","")}
                        "description" -> {
                            if(this::feed.isInitialized) {
                                feed.desc = parser.nextText()
                            }
                        }
                        "title" -> {
                            if(this::feed.isInitialized) {
                                feed.title = parser.nextText()
                            }
                        }
                        "pubDate" -> {
                            if(this::feed.isInitialized) {
                                feed.date = parser.nextText().substring(0,16)
                            }
                        }
                        "media:thumbnail" -> {
                            if(this::feed.isInitialized) {
                                feed.img_url = parser.getAttributeValue(null, "url")
                            }
                        }
                        "link" -> {
                            if(this::feed.isInitialized) {
                                feed.link = parser.nextText()
                            }
                        }
                    }

                }

                XmlPullParser.END_TAG ->{
                    tag_name = parser.name
                    if(tag_name.equals("item")){
                        feeds.add(feed)
                    }
                }

            }

            eventType = parser.next()
        }
        return getDescText(feeds)
    }

    //Present the descriptive text of the preview in a nice way
    fun getDescText(list: List<Feed>):List<Feed>{
        for(feed in list){
            var desc = feed.desc

            var regex = "\\<h1\\>.*\\<\\/h1\\>".toRegex()
            var res = regex.find(desc)?.value.toString()
            res = res.replace("\\<h1\\>".toRegex(),"")
            res = res.replace("\\<\\/h1\\>".toRegex(),"")

            feed.desc = res
        }
        return list
    }
}