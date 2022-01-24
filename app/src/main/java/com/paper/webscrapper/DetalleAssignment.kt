package com.paper.webscrapper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.load.engine.Resource
import com.paper.webscrapper.DataBase.Assignment
import com.paper.webscrapper.DataBase.AssignmentCRUD
import petrov.kristiyan.colorpicker.ColorPicker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DetalleAssignment : AppCompatActivity() {

    var toolbarDetalle: Toolbar? = null
    var crud : AssignmentCRUD? = null

    val SHARED_PREFS = "shared_prefs"
    val TEXT_THEME = "text_theme"
    var text_theme: String ? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        updateTheme()

        setContentView(R.layout.activity_detalle_assignment)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // color NAVBAR
            getWindow().setNavigationBarColor(ThemeColors.getPrimary(this));
        }
        //setSupportActionBar(toolbar)
        window.statusBarColor = ThemeColors.getPrimary(this)

        toolbarDetalle = findViewById(R.id.toolbarDetalle)
        toolbarDetalle?.setTitle("Detalle Tarea")
        //toolbarDetalle?.setBackgroundColor(this.resources.getColor(R.color.colorAccent))

        val indexID = intent.getIntExtra("ID",0)

        setSupportActionBar(toolbarDetalle)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        //----------------FIN DEL ACTION BAR---------

        val etId = findViewById<EditText>(R.id.etIDdetalle)
        val etMateria = findViewById<EditText>(R.id.etMateriaDetalle)
        val etFecha = findViewById<EditText>(R.id.etDateAssignmentDetalle)
        val etContenido = findViewById<EditText>(R.id.etContenidoAssignmentDetalle)
        val etHora = findViewById<EditText>(R.id.etTimeAssignmentDetalle)

        val btnActualizar = findViewById<Button>(R.id.btnActualizarTarea)
        val btnEliminar = findViewById<Button>(R.id.btnEliminarTarea)
        val btnColor = findViewById<Button>(R.id.btnColorSheetDetalle)

        var colorBg: Int? = null
        var gradientDraw : GradientDrawable = btnColor.background.mutate() as GradientDrawable

        val date_in = findViewById<EditText>(R.id.etDateAssignmentDetalle)
        val time_in = findViewById<EditText>(R.id.etTimeAssignmentDetalle)

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)


        crud = AssignmentCRUD(this)
        val assignment = crud?.getAssignment(indexID)

        etId.setText(assignment!!.id.toString(), TextView.BufferType.EDITABLE)
        etMateria.setText(assignment.subject, TextView.BufferType.EDITABLE)
        etFecha.setText(assignment.date, TextView.BufferType.EDITABLE)
        etHora.setText(assignment.hour, TextView.BufferType.EDITABLE)
        etContenido.setText(assignment.content, TextView.BufferType.EDITABLE)
        gradientDraw.setColor(assignment.color!!.toInt()) // el color del boton
        colorBg = (assignment.color!!.toInt())


        time_in.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, mHourOfDay, mMinute ->

                cal.set(Calendar.HOUR_OF_DAY, mHourOfDay)
                cal.set(Calendar.MINUTE, mMinute)
                //set to text view
                time_in.setText( SimpleDateFormat("HH:mm").format(cal.time) )
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true ).show()
        }

        date_in.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ view, mYear, mMonth, mDayOfMonth ->
                //set to text view
                var mDayOfMonthConverted = (mDayOfMonth).toString()
                var mMonthConverted = ( mMonth + 1 ).toString()

                if (mDayOfMonth < 10)
                    mDayOfMonthConverted = ("0$mDayOfMonthConverted")


                if (( mMonth + 1 ) < 10)
                    mMonthConverted = ("0$mMonthConverted")


                date_in.setText("$mDayOfMonthConverted/$mMonthConverted/$mYear")
            }, year, month, day)
            //show dialog
            dpd.datePicker.minDate = (System.currentTimeMillis() - 1000)
            dpd.show()
        }


        btnColor.setOnClickListener {
            val colorPicker = ColorPicker(this)
            colorPicker.show()
            colorPicker.setOnChooseColorListener(object : ColorPicker.OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    // put code
                    //Toast.makeText(applicationContext, color.toString(),Toast.LENGTH_SHORT).show()
                    //btnColor.setBackgroundColor(color)
                    gradientDraw.setColor(color)
                    colorBg = color
                }

                override fun onCancel() {
                    // put code
                }
            })
        }


        btnActualizar.setOnClickListener{

            if(etMateria.text.toString() == ""){
                Toast.makeText(this, "Se requiere materia", Toast.LENGTH_SHORT).show()
            }else {

                crud?.updateAssignment(
                    Assignment(
                        //etId.text.toString().toInt(),
                        indexID,
                        etMateria.text.toString(),
                        etContenido.text.toString(),
                        etFecha.text.toString(),
                        etHora.text.toString(),
                        colorBg
                    )
                )
                //startActivity(Intent(this, NavActivity::class.java))
                onBackPressed()
                allowRefreshAssignment = true
            }
        }

        btnEliminar.setOnClickListener{
            crud?.deleteAssignment(Assignment(
                //etId.text.toString().toInt(),
                indexID,
                etMateria.text.toString(),
                etContenido.text.toString(),
                etFecha.text.toString(),
                etHora.text.toString(),
                colorBg))
            //startActivity(Intent(this, NavActivity::class.java))
            onBackPressed()
            allowRefreshAssignment = true
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun loadData(){
        var sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        text_theme = sharedPreferences.getString(TEXT_THEME, "default").toString()
    }

    fun updateTheme(){
        when(text_theme){

            "default" -> {
                setTheme(R.style.AppTheme)
            }

            "unam" -> {
                setTheme(R.style.AppThemeUNAM)
            }

            "black" -> {
                setTheme(R.style.AppThemeBlack)
            }

            "purple" -> {
                setTheme(R.style.AppThemePurple)
            }

            "red" -> {
                setTheme(R.style.AppThemeRed)
            }

            "green" -> {
                setTheme(R.style.AppThemeGreen)
            }

            "cyan" -> {
                setTheme(R.style.AppThemeCyan)
            }

            else -> {
                setTheme(R.style.AppTheme)
            }

        }
    }
}