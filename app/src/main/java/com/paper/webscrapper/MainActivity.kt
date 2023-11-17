package com.paper.webscrapper

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.nodes.Document


var USER = ""
var PASS = ""
var NAME = ""
var CURP = ""
var THEME = "default"

var IMG_BYTE_ARRAY: ByteArray? = null
var numKeys = 0
var CARRERA = ""
var GRADES_DOC : Document? = null

var tlHeaderPrueba: TableRow? = null

class MainActivity : AppCompatActivity() {

   // val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //var gifFile = "file:///android_asset/loading.gif"
        tlHeaderPrueba = findViewById(R.id.trHeader)
    }


}