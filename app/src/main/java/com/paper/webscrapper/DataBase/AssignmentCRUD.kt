package com.paper.webscrapper.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class AssignmentCRUD(context: Context) {

    private var helper: DataBaseHelper? = null

    init {
        helper = DataBaseHelper(context)
    }

    fun newAssignment(item : Assignment){
        //Abrir base de datos en modo escritura
        val db = helper?.writableDatabase
        val values = ContentValues()

        //mapeo de columnas con valores a insertar
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_ID, item.id)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT, item.subject)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT, item.content)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_DATE, item.date)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_HOUR, item.hour)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_COLOR, item.color)

        //insertar nueva fila en la tabla
        val newRowId = db?.insert(AssignmentsContract.Companion.Entrada.NOMBRE_TABLA, null, values)
        db?.close()
    }

    fun getAssignments() : ArrayList<Assignment>{
        val items : ArrayList<Assignment> = ArrayList()

        //abrir db en modo lectura
        val db: SQLiteDatabase  = helper?.readableDatabase!!

        //Especificar columnas que quiero consultar
        val columnas = arrayOf(
            AssignmentsContract.Companion.Entrada.COLUMNA_ID,
            AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT,
            AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT,
            AssignmentsContract.Companion.Entrada.COLUMNA_DATE,
            AssignmentsContract.Companion.Entrada.COLUMNA_HOUR,
            AssignmentsContract.Companion.Entrada.COLUMNA_COLOR
        )

        // Crear un cursor para recorrer la tabla
        val c: Cursor = db.query(
            AssignmentsContract.Companion.Entrada.NOMBRE_TABLA,
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
                Assignment(
                    c.getInt(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_ID)),
                    c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT)),
                    c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT)),
                    c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_DATE)),
                    c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_HOUR)),
                    c.getInt(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_COLOR))
                )
            )
        }

        // cerrar DB
        db.close()

        return items
    }

    fun getAssignment(id:Int): Assignment {
        var item: Assignment? = null

        val db:SQLiteDatabase = helper?.readableDatabase!!

        val columnas = arrayOf(
            AssignmentsContract.Companion.Entrada.COLUMNA_ID,
            AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT,
            AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT,
            AssignmentsContract.Companion.Entrada.COLUMNA_DATE,
            AssignmentsContract.Companion.Entrada.COLUMNA_HOUR,
            AssignmentsContract.Companion.Entrada.COLUMNA_COLOR
        )

        val c:Cursor = db.query(
            AssignmentsContract.Companion.Entrada.NOMBRE_TABLA,
            columnas,
            " id = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        while(c.moveToNext()){
            item = Assignment(
                c.getInt(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_ID)),
                c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT)),
                c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT)),
                c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_DATE)),
                c.getString(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_HOUR)),
                c.getInt(c.getColumnIndexOrThrow(AssignmentsContract.Companion.Entrada.COLUMNA_COLOR))
            )
        }
        c.close()

        return item!!
    }

    fun updateAssignment(item: Assignment){

        val db:SQLiteDatabase = helper?.writableDatabase!!

        val values = ContentValues()
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_ID, item.id)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_SUBJECT, item.subject)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_CONTENT, item.content)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_DATE, item.date)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_HOUR, item.hour)
        values.put(AssignmentsContract.Companion.Entrada.COLUMNA_COLOR, item.color)

        db.update(
            AssignmentsContract.Companion.Entrada.NOMBRE_TABLA,
            values,
            "id = ?",
            arrayOf(item.id.toString()))

        db.close()
    }

    fun deleteAssignment(item: Assignment){
        val db:SQLiteDatabase = helper?.writableDatabase!!

        db.delete(
            AssignmentsContract.Companion.Entrada.NOMBRE_TABLA,
            "id = ?",
            arrayOf(item.id.toString()))

        db.close()
    }

}