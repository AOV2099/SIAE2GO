package com.paper.webscrapper.DataBase

class Assignment(subject: String?, content: String?, date: String?, hour: String?, color: Int?){
//class Assignment(subject: String?, content: String?, date: String?, hour: String?, color: Int?){ constructor(subject: String?, content: String?, date: String?, hour: String?, color: Int?) : this(id, subject, content, date, hour, color)

    var id : Int? = null
    var subject : String? = null
    var content : String? = null
    var date : String? = null
    var hour : String? = null
    var color : Int? = null

    init {
        //this.id = id
        this.subject = subject
        this.content = content
        this.date = date
        this.hour = hour
        this.color = color
    }

    constructor(id: Int?,subject: String?, content: String?, date: String?, hour: String?, color: Int?) : this(subject,content,date,hour,color){
        this.id = id
    }

}