package com.paper.webscrapper

import android.app.Activity
import android.content.Context
import android.util.TypedValue

object ThemeColors {

    fun getAccent(context: Context): Int{
        val typedValue = TypedValue()
        var theme = context?.theme
        theme?.resolveAttribute(R.attr.colorAccent, typedValue, true)
        var colorAccent = typedValue.data

        return  colorAccent
    }

    fun getPrimary(context: Context): Int{
        val typedValue = TypedValue()
        var theme = context?.theme

        theme?.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        var colorPrimary = typedValue.data

        return colorPrimary
    }

    fun getPrimaryDark(context: Context): Int{
        val typedValue = TypedValue()
        var theme = context?.theme

        theme?.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
        var colorPrimaryDark = typedValue.data

        return  colorPrimaryDark
    }

    fun getPrimaryLight(context: Context): Int{
        val typedValue = TypedValue()
        var theme = context?.theme

        theme?.resolveAttribute(R.attr.colorPrimaryLight, typedValue, true)
        var colorPrimaryDark = typedValue.data

        return  colorPrimaryDark
    }

    fun getHomeBg(context: Context): Int{

        val typedValue = TypedValue()
        var theme = context?.theme
        var resourse: Int? = null
        theme?.resolveAttribute(R.attr.themeName , typedValue, true)


        when(typedValue.string){

            "black" -> {
                resourse = R.drawable.bg_foto_alumno_black
            }
            "purple" -> {
                resourse = R.drawable.bg_foto_alumno_purple
            }
            "green" -> {
                resourse = R.drawable.bg_foto_alumno_green
            }
            "cyan" -> {
                resourse = R.drawable.bg_foto_alumno_cyan
            }
            "red" -> {
                resourse = R.drawable.bg_foto_alumno_red
            }
            "default" -> {
                resourse = R.drawable.bg_foto_alumno
            }
            else -> { // Note the block
                resourse = R.drawable.bg_foto_alumno
            }

        }

        return resourse!!
    }

}