package com.example.note

class Note {

    var id:String?=null
    var title:String?=null
    var note:String?=null
    var timestamp:String?=null

    constructor()
    {

    }
    constructor(id:String,title:String,note:String,timestamp:String)
    {
        this.id=id
        this.title=title
        this.note=note
        this.timestamp=timestamp

    }
}