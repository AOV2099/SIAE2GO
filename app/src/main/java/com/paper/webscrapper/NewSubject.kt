package com.paper.webscrapper

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.paper.webscrapper.DataBase.Subject
import com.paper.webscrapper.DataBase.SubjectCRUD
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener
import java.text.SimpleDateFormat
import java.util.*


class NewSubject : AppCompatActivity() {

    var toolbar: Toolbar? = null
    var crud : SubjectCRUD? = null

    val SHARED_PREFS = "shared_prefs"
    val TEXT_THEME = "text_theme"
    var text_theme: String ? = null

    var etId: EditText? = null
    var etMateria: EditText? = null
    var etStartHour: EditText? = null
    var etEndHour: EditText? = null
    var etSalon: EditText? = null
    var btnColor: Button? = null
    var colorBg: Int? = null
    var cbLunes: CheckBox? = null
    var cbMartes: CheckBox? = null
    var cbMiercoles: CheckBox? = null
    var cbJueves: CheckBox? = null
    var cbViernes: CheckBox? = null
    var cbSabado: CheckBox? = null
    var cbDomingo: CheckBox? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        updateTheme()

        setContentView(R.layout.activity_new_subject)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //color NAVBAR
            getWindow().setNavigationBarColor(ThemeColors.getPrimary(this));
        }
        //setSupportActionBar(toolbar)
        window.statusBarColor = ThemeColors.getPrimary(this)

        toolbar = findViewById(R.id.toolbarNewSubject)
        toolbar?.setTitle("Nueva Materia")
        //toolbar?.setBackgroundColor(ThemeColors.getAccent(this))

        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        
        //----------------FIN DEL ACTION BAR---------

         etId = findViewById<EditText>(R.id.etIDsubject)
         etMateria = findViewById<EditText>(R.id.etMateriaSubject)
         etStartHour = findViewById<EditText>(R.id.etStartHourSubject)
         etEndHour = findViewById<EditText>(R.id.etEndHourSubject)
         etSalon = findViewById<EditText>(R.id.etSalonSubject)
         btnColor = findViewById<Button>(R.id.btnColorSheetSubject)

         cbLunes = findViewById<CheckBox>(R.id.cbMonday)
         cbMartes = findViewById<CheckBox>(R.id.cbTuesday)
         cbMiercoles = findViewById<CheckBox>(R.id.cbWednesday)
         cbJueves = findViewById<CheckBox>(R.id.cbThursday)
         cbViernes = findViewById<CheckBox>(R.id.cbFriday)
         cbSabado = findViewById<CheckBox>(R.id.cbSaturday)
         cbDomingo = findViewById<CheckBox>(R.id.cbSunday)

        var gradientDraw: GradientDrawable? = null
        gradientDraw = btnColor?.background?.mutate() as GradientDrawable
        gradientDraw.setColor(resources.getColor(R.color.gray)) ///default color
        colorBg = (resources.getColor(R.color.gray))

        val btnAddSubject = findViewById<Button>(R.id.btnAddSubject)


        //db
        crud = SubjectCRUD(this)

        etStartHour?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, mHourOfDay, mMinute ->

                cal.set(Calendar.HOUR_OF_DAY, mHourOfDay)
                cal.set(Calendar.MINUTE, mMinute)
                //set to text view
                etStartHour?.setText( SimpleDateFormat("HH:mm").format(cal.time) )
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true ).show()
        }

        etEndHour?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, mHourOfDay, mMinute ->

                cal.set(Calendar.HOUR_OF_DAY, mHourOfDay)
                cal.set(Calendar.MINUTE, mMinute)
                //set to text view
                etEndHour?.setText( SimpleDateFormat("HH:mm").format(cal.time) )
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true ).show()
        }


        btnColor?.setOnClickListener {
            val colorPicker = ColorPicker(this)
            //colorPicker.setDefaultColorButton(-10965321) // set the colorButton to check by default
            colorPicker.show()

            colorPicker.setOnChooseColorListener(object : OnChooseColorListener {
                override fun onChooseColor(position: Int, color: Int) {

                    gradientDraw?.setColor(color)
                    colorBg = color
                }

                override fun onCancel() {
                    // put code
                }
            })
        }


        btnAddSubject.setOnClickListener{

            if(etMateria?.text.toString() == "" || etStartHour?.text.toString() == "" || etEndHour?.text.toString() == ""){
                Toast.makeText(this, "Se requiere agregar materia, hora de inicio y fin", Toast.LENGTH_SHORT).show()
            }else {

                if (cbLunes?.isChecked!!)
                    createNewSubject("Monday")
                if (cbMartes?.isChecked!!)
                    createNewSubject("Tuesday")
                if (cbMiercoles?.isChecked!!)
                    createNewSubject("Wednesday")
                if (cbJueves?.isChecked!!)
                    createNewSubject("Thursday")
                if (cbViernes?.isChecked!!)
                    createNewSubject("Friday")
                if (cbSabado?.isChecked!!)
                    createNewSubject("Saturday")
                if (cbDomingo?.isChecked!!)
                    createNewSubject("Sunday")

                onBackPressed()
               //
                // allowRefreshDaySelected = true
            }
        }

    }

    fun createNewSubject(dayKey: String){
        if (etSalon?.text.toString() == ""){
            etSalon?.setText("n/a") 
        }

        crud?.newSubject(
            Subject(
                dayKey,
                etMateria?.text.toString(),
                etStartHour?.text.toString(),
                etEndHour?.text.toString(),
                etSalon?.text.toString(),
                colorBg
            )
        )

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