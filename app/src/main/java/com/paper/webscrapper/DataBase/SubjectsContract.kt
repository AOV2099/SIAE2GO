package com.paper.webscrapper.DataBase

import android.provider.BaseColumns

class SubjectsContract {
    companion object{
        val VERSION = 1

        class Entrada: BaseColumns{
            companion object{

                val NOMBRE_TABLA = "subjects"

                val COLUMNA_ID = "id"
                val COLUMNA_DAY = "day"
                val COLUMNA_SUBJECT = "subject"
                val COLUMNA_STARTHOUR = "start_hour"
                val COLUMNA_ENDHOUR = "end_hour"
                val COLUMNA_SALON = "salon"
                val COLUMNA_COLOR = "color"

            }
        }
    }
}