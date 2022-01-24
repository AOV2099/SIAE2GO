package com.paper.webscrapper.DataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context): SQLiteOpenHelper(context, AssignmentsContract.Companion.Entrada.NOMBRE_TABLA, null, AssignmentsContract.Companion.VERSION) {

    companion object{
        val CREATE_ASSIGNMENTS_TABLE = "CREATE TABLE " + AssignmentsContract.Companion.Entrada.NOMBRE_TABLA +
                " (" + AssignmentsContract.Companion.Entrada.COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT + " TEXT, " +
                AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT + " TEXT, " +
                AssignmentsContract.Companion.Entrada.COLUMNA_DATE + " TEXT," +
                AssignmentsContract.Companion.Entrada.COLUMNA_HOUR + " TEXT," +
                AssignmentsContract.Companion.Entrada.COLUMNA_COLOR + " INTEGER" + " )"
        val REMOVE_ASSIGNMENTS_TABLE = "DROP TABLE IF EXIST " + AssignmentsContract.Companion.Entrada.NOMBRE_TABLA

        val CREATE_SUBJECTS_TABLE = "CREATE TABLE " + SubjectsContract.Companion.Entrada.NOMBRE_TABLA +
                " (" + SubjectsContract.Companion.Entrada.COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SubjectsContract.Companion.Entrada.COLUMNA_DAY + " TEXT, " +
                SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT + " TEXT, " +
                SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR + " TEXT, " +
                SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR + " TEXT," +
                SubjectsContract.Companion.Entrada.COLUMNA_SALON + " TEXT," +
                SubjectsContract.Companion.Entrada.COLUMNA_COLOR + " INTEGER" + " )"
        val REMOVE_SUBJECTS_TABLE = "DROP TABLE IF EXIST " + SubjectsContract.Companion.Entrada.NOMBRE_TABLA

    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL(CREATE_ASSIGNMENTS_TABLE)
        db?.execSQL(CREATE_SUBJECTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        db?.execSQL(REMOVE_ASSIGNMENTS_TABLE)
        db?.execSQL(REMOVE_SUBJECTS_TABLE)
        onCreate(db)
    }

}