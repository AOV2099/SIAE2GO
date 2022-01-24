package com.paper.webscrapper

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.paper.webscrapper.DataBase.Subject
import com.paper.webscrapper.DataBase.SubjectCRUD
import petrov.kristiyan.colorpicker.ColorPicker
import petrov.kristiyan.colorpicker.ColorPicker.OnChooseColorListener
import java.text.SimpleDateFormat
import java.util.*


class DetalleSubject : AppCompatActivity() {

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
    var tvLunes: TextView? = null
    var tvMartes: TextView? = null
    var tvMiercoles: TextView? = null
    var tvJueves: TextView? = null
    var tvViernes: TextView? = null
    var tvSabado: TextView? = null
    var tvDomingo: TextView? = null
    var btnDeleteSubject: Button? = null
    var btnUpdateSubject: Button? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadData()
        updateTheme()

        setContentView(R.layout.activity_detalle_subject)

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

         etId = findViewById<EditText>(R.id.etIDSubjectDetalle)
         etMateria = findViewById<EditText>(R.id.etMateriaSubjectDetalle)
         etStartHour = findViewById<EditText>(R.id.etStartHourSubjectDetalle)
         etEndHour = findViewById<EditText>(R.id.etEndHourSubjectDetalle)
         etSalon = findViewById<EditText>(R.id.etSalonSubjectDetalle)
         btnColor = findViewById<Button>(R.id.btnColorSheetSubjectDetalle)

         tvLunes = findViewById(R.id.tvMondayDetalle)
         tvMartes = findViewById(R.id.tvTuesdayDetalle)
         tvMiercoles = findViewById(R.id.tvWednesdayDetalle)
         tvJueves = findViewById(R.id.tvThursdayDetalle)
         tvViernes = findViewById(R.id.tvFridayDetalle)
         tvSabado = findViewById(R.id.tvSaturdayDetalle)
         tvDomingo = findViewById(R.id.tvSundayDetalle)
         btnDeleteSubject = findViewById<Button>(R.id.btnDeleteSubject)
         btnUpdateSubject = findViewById<Button>(R.id.btnUpdateSubject)

        val tag_subject = intent.getStringExtra("TAG_SUBJECT")
        val tag_start_Hour = intent.getStringExtra("TAG_START_HOUR")

        var gradientDraw: GradientDrawable? = null
        gradientDraw = btnColor?.background?.mutate() as GradientDrawable
        gradientDraw.setColor(resources.getColor(R.color.gray)) ///default color
        colorBg = (resources.getColor(R.color.gray))


        //db
        crud = SubjectCRUD(this)
        val subjects = crud?.getSubjectsBySubjectAndStartHour(tag_subject, tag_start_Hour)

        //rellena con los datos de la db
        etId?.setText(subjects?.get(0)!!.id.toString(), TextView.BufferType.EDITABLE)
        gradientDraw.setColor(subjects?.get(0)!!.color!!.toInt()) // el color del boton
        colorBg = (subjects[0]!!.color!!.toInt())
        etMateria?.setText(subjects?.get(0)!!.subject.toString(), TextView.BufferType.EDITABLE)
        etStartHour?.setText(subjects?.get(0)!!.startHour.toString(), TextView.BufferType.EDITABLE)
        etEndHour?.setText(subjects?.get(0)!!.endHour.toString(), TextView.BufferType.EDITABLE)
        etSalon?.setText(subjects?.get(0)!!.salon.toString(), TextView.BufferType.EDITABLE)

        tvLunes?.visibility = View.GONE
        tvMartes?.visibility = View.GONE
        tvMiercoles?.visibility = View.GONE
        tvMiercoles?.visibility = View.GONE
        tvViernes?.visibility = View.GONE
        tvJueves?.visibility = View.GONE
        tvSabado?.visibility = View.GONE
        tvDomingo?.visibility = View.GONE

        for (subject in subjects){
            recolorDays(subject)
        }

        //LISTENERS
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


        btnUpdateSubject?.setOnClickListener{

            if(etMateria?.text.toString() == "" || etStartHour?.text.toString() == "" || etEndHour?.text.toString() == ""){
                Toast.makeText(this, "Se requiere agregar materia , hora de inicio y fin", Toast.LENGTH_SHORT).show()
            }else {
                    crud?.updateSubjects(
                        etMateria?.text.toString(),
                        etStartHour?.text.toString(),
                        etEndHour?.text.toString(),
                        etSalon?.text.toString(),
                        colorBg!!,
                        tag_subject,
                        tag_start_Hour
                    )

                onBackPressed()

            }
        }

        btnDeleteSubject?.setOnClickListener{
            crud?.deleteSubjectsBySubjectAndStartHour(tag_subject, tag_start_Hour)
            onBackPressed()
        }

    }

    fun recolorDays(subject: Subject){
        if(subject.day == "Monday")
            tvLunes?.visibility = View.VISIBLE
        if(subject.day == "Tuesday")
            tvMartes?.visibility = View.VISIBLE
        if(subject.day == "Wednesday")
            tvMiercoles?.visibility = View.VISIBLE
        if(subject.day == "Thursday")
            tvJueves?.visibility = View.VISIBLE
        if(subject.day == "Friday")
            tvViernes?.visibility = View.VISIBLE
        if(subject.day == "Saturday")
            tvSabado?.visibility = View.VISIBLE
        if(subject.day == "Sunday")
            tvDomingo?.visibility = View.VISIBLE
    }

    fun createNewSubject(dayKey: String){

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