package com.paper.webscrapper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.paper.webscrapper.DataBase.Assignment
import com.paper.webscrapper.DataBase.AssignmentCRUD
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener
import java.text.SimpleDateFormat
import java.util.*


class NewAssignment : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var crud : AssignmentCRUD? = null

    val SHARED_PREFS = "shared_prefs"
    val TEXT_THEME = "text_theme"
    var text_theme: String ? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        updateTheme()

        setContentView(R.layout.activity_new_assignment)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //color NAVBAR
            getWindow().setNavigationBarColor(ThemeColors.getPrimary(this));
        }
        //setSupportActionBar(toolbar)
        window.statusBarColor = ThemeColors.getPrimary(this)

        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitle("Nueva Tarea")
        //toolbar?.setBackgroundColor(ThemeColors.getAccent(this))

        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        
        //----------------FIN DEL ACTION BAR---------

        val etId = findViewById<EditText>(R.id.etID)
        val etMateria = findViewById<EditText>(R.id.etMateria)
        val etContenido = findViewById<EditText>(R.id.etContenidoNewAssignment)
        val etFecha = findViewById<EditText>(R.id.etDateAssignment)
        val etHora = findViewById<EditText>(R.id.etTimeAssignment)

        val btnColor = findViewById<Button>(R.id.btnColorSheet)
        var gradientDraw: GradientDrawable? = null
        var colorBg: Int? = null
        gradientDraw = btnColor.background.mutate() as GradientDrawable
        gradientDraw.setColor(resources.getColor(R.color.gray)) ///default color
        colorBg = (resources.getColor(R.color.gray))

        val btnAddTarea = findViewById<Button>(R.id.btnAddTarea)
        val date_in = findViewById<EditText>(R.id.etDateAssignment)
        val time_in = findViewById<EditText>(R.id.etTimeAssignment)

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)

        //db
        crud = AssignmentCRUD(this)

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

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, mYear, mMonth, mDayOfMonth ->
                    //set to text view
                    var mDayOfMonthConverted = (mDayOfMonth).toString()
                    var mMonthConverted = ( mMonth + 1 ).toString()

                    if (mDayOfMonth < 10)
                        mDayOfMonthConverted = ("0$mDayOfMonthConverted")


                    if ((mMonth +1 ) < 10)
                        mMonthConverted = ("0$mMonthConverted")


                    date_in.setText("$mDayOfMonthConverted/$mMonthConverted/$mYear")
                }, year, month, day)
                //show dialog
                dpd.datePicker.minDate = (System.currentTimeMillis() - 1000)
                dpd.show()
        }


        btnColor.setOnClickListener {
            val colorPicker = ColorPicker(this)
            //colorPicker.setDefaultColorButton(-10965321) // set the colorButton to check by default
            colorPicker.show()

            colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {
                    // put code
                    //Toast.makeText(applicationContext, color.toString(),Toast.LENGTH_LONG).show()
                    //btnColor.setBackgroundColor(color)
                    gradientDraw?.setColor(color)
                    colorBg = color
                }

                override fun onCancel() {
                    // put code
                }
            })
        }


        btnAddTarea.setOnClickListener{

            if(etMateria.text.toString() == ""){
                Toast.makeText(this, "Se requiere agregar materia", Toast.LENGTH_SHORT).show()
            }else {

                crud?.newAssignment(
                    Assignment(
                        // etId.text.toString().toInt(),
                        //null,
                        etMateria.text.toString(),
                        etContenido.text.toString(),
                        etFecha.text.toString(),
                        etHora.text.toString(),
                        colorBg
                    )
                )
                //startActivity(Intent(this, AssignmentsFragment::class.java))
                onBackPressed()
                allowRefreshAssignment = true
            }
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