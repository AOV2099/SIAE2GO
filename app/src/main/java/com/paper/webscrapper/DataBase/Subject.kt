package com.paper.webscrapper.DataBase

class Subject (  day: String?, subject: String?, startHour: String?, endHour: String?, salon: String?, color: Int?  ) {

    var id : Int? = null
    var subject: String? = null
    var day: String? = null
    var startHour: String? = null
    var endHour: String? = null
    var salon: String? = null
    var color: Int? = null


    init {
        this.subject = subject
        this.day = day
        this.startHour = startHour
        this.endHour = endHour
        this.salon = salon
        this.color = color
    }

    constructor(id: Int?, day: String?, subject: String?,  startHour: String?, endHour: String?, salon: String?, color: Int?) : this(subject, day, startHour, endHour, salon, color){
        this.id = id
    }

}
