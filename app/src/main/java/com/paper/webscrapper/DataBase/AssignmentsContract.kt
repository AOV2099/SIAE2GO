package com.paper.webscrapper.DataBase

import android.provider.BaseColumns

class AssignmentsContract {
    companion object{
        val VERSION = 1

        class Entrada: BaseColumns{

            companion object{

                val NOMBRE_TABLA = "assignments"

                val COLUMNA_ID = "id"
                val COLUMNA_SUBJECT = "subject"
                val COLUMNA_CONTENT = "content"
                val COLUMNA_DATE = "date"
                val COLUMNA_HOUR = "hour"
                val COLUMNA_COLOR = "color"
            }
        }
    }
}