package com.example.prm03

//Class implementing constructor for the type of data appearing for an article preview
class Feed() {

    lateinit var title: String
    lateinit var desc: String
    lateinit var date: String
    lateinit var img_url: String
    lateinit var link: String

    init {
        title = ""
        desc = ""
        date = ""
        img_url = ""
        link = ""

    }



    constructor(title:String,desc:String,date:String,img_url:String,link:String):this(){
        this.title = title
        this.desc = desc
        this.date = date
        this.img_url = img_url
        this.link = link

    }

}