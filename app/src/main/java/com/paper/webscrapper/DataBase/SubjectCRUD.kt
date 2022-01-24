package com.paper.webscrapper.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class SubjectCRUD(context: Context) {

    private var helper: DataBaseHelper? = null

    init {
        helper = DataBaseHelper(context)
    }

    fun newSubject(item: Subject){
        //Abrir base de datos en modo escritura
        val db: SQLiteDatabase? = helper?.writableDatabase
        val values = ContentValues()

        //mapeo de columnas con valores a insertar
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_ID, item.id)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_DAY, item.day)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT, item.subject)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR, item.startHour)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR, item.endHour)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_SALON, item.salon)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_COLOR, item.color)

        //insertar nueva fila en la tabla
        val newRowId = db?.insert(SubjectsContract.Companion.Entrada.NOMBRE_TABLA, null, values)
        db?.close()
    }

    fun getSubjects(): ArrayList<Subject> {
        val items : ArrayList<Subject> = ArrayList()

        //abrir db en modo lectura
        val db: SQLiteDatabase  = helper?.readableDatabase!!

        //Especificar columnas que quiero consultar
        val columnas = arrayOf(
            SubjectsContract.Companion.Entrada.COLUMNA_ID,
            SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT,
            SubjectsContract.Companion.Entrada.COLUMNA_DAY,
            SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_SALON,
            SubjectsContract.Companion.Entrada.COLUMNA_COLOR
        )

        // Crear un cursor para recorrer la tabla
        val c: Cursor = db.query(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            null, //NOS DICE SI DESEAMOS FILTAR POR ALGUN ORDEN
            null,
            null,
            null,
            null
        )

        // Hacer el recorrido del cursor en la tabla
        while (c.moveToNext()){
            items.add(
                Subject(
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ID)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_DAY)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SALON)),
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_COLOR))
                )
            )
        }

        // cerrar DB
        db.close()

        return items
    }

    fun getSubjectsByDay(dayKey: String):  ArrayList<Subject>{

        val items : ArrayList<Subject> = ArrayList()
        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            SubjectsContract.Companion.Entrada.COLUMNA_ID,
            SubjectsContract.Companion.Entrada.COLUMNA_DAY,
            SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT,
            SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_SALON,
            SubjectsContract.Companion.Entrada.COLUMNA_COLOR
        )

        val c:Cursor = db.query(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            " day = ?",
            arrayOf(dayKey),
            null,
            null,
            "start_hour ASC" //ACOMODA POR ORA DE INICIO DE MANERA DESCENDENTE
        )

        // Hacer el recorrido del cursor en la tabla
        while (c.moveToNext()){
            items.add(
                Subject(
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ID)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_DAY)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SALON)),
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_COLOR))
                )
            )
        }


        db.close()
        return items

    }


    fun getSubjectsBySubjectAndStartHour(subject: String, starHour: String):ArrayList<Subject>{
        val items : ArrayList<Subject> = ArrayList()
        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            SubjectsContract.Companion.Entrada.COLUMNA_ID,
            SubjectsContract.Companion.Entrada.COLUMNA_DAY,
            SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT,
            SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR,
            SubjectsContract.Companion.Entrada.COLUMNA_SALON,
            SubjectsContract.Companion.Entrada.COLUMNA_COLOR
        )

        val c:Cursor = db.query(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            " subject = ? AND start_hour = ?",
            arrayOf(subject , starHour),
            null,
            null,
            null
        )

        // Hacer el recorrido del cursor en la tabla
        while (c.moveToNext()){
            items.add(
                Subject(
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ID)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_DAY)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR)),
                    c.getString(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_SALON)),
                    c.getInt(c.getColumnIndexOrThrow(SubjectsContract.Companion.Entrada.COLUMNA_COLOR))
                )
            )
        }

        db.close()
        return items
    }


    fun deleteSubjectsBySubjectAndStartHour(subject: String, starHour: String){
        val db:SQLiteDatabase = helper?.writableDatabase!!

        db.delete(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            " subject = ? AND start_hour = ?",
            arrayOf(subject , starHour)
        )

        db.close()
    }



    fun updateSubjects(subject: String, starHour: String, endHour: String, salon: String, color: Int, subjectPREV: String, starHourPREV: String){

        val db:SQLiteDatabase = helper?.writableDatabase!!
        val values = ContentValues()

        values.put(SubjectsContract.Companion.Entrada.COLUMNA_SUBJECT, subject)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_STARTHOUR, starHour)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_ENDHOUR, endHour)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_SALON, salon)
        values.put(SubjectsContract.Companion.Entrada.COLUMNA_COLOR, color)

        db.update(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            values,
            " subject = ? AND start_hour = ? ",
            arrayOf(subjectPREV , starHourPREV)
        )

        db.close()
    }

    fun deleteSubject(item: Subject){

        val db:SQLiteDatabase = helper?.writableDatabase!!

        db.delete(
            SubjectsContract.Companion.Entrada.NOMBRE_TABLA,
            "id = ?",
            arrayOf(item.id.toString()))

        db.close()

    }

}