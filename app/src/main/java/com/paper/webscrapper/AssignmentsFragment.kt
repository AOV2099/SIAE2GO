package com.paper.webscrapper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paper.ejemplodb.RecyclerView.ClickListener
import com.paper.ejemplodb.RecyclerView.LongClickListener
import com.paper.webscrapper.DataBase.Assignment
import com.paper.webscrapper.DataBase.AssignmentCRUD
import java.util.*
import kotlin.collections.ArrayList


var allowRefreshAssignment = true

class AssignmentsFragment : Fragment() {

    var TAG_TAREA = "com.paper.webscrapper.AssignmentsFragment.TAG_TAREA"

    var lista: RecyclerView? = null
    var adaptador: AdaptadorCustomAssignment? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var vista : View? = null
    var fab : FloatingActionButton? = null
    var assignments: ArrayList<Assignment>? = null
    var crud : AssignmentCRUD? = null
    var layoutBg : CoordinatorLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        if (allowRefreshAssignment)
        {
            allowRefreshAssignment = false;
            getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_assignments, container, false)

        fab = vista?.findViewById<FloatingActionButton>(R.id.fabAssignment)
        lista = vista?.findViewById(R.id.rvTareasLista)
        layoutBg = vista?.findViewById(R.id.corlayBackgroundAssignment)
        lista?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        lista?.layoutManager = layoutManager

        //Actualiza dependiendo del theme
        layoutBg?.setBackgroundColor(ThemeColors.getPrimaryLight(requireContext()))


        fab?.setOnClickListener {
            startActivity(Intent(context, NewAssignment::class.java))
        }

        crud = AssignmentCRUD(requireContext()) //TODO AQUI IBA EL CONTEXTO THIS
        assignments = crud?.getAssignments()// hace consulta a la deb y asigna los valores en el array


        if (assignments?.size!! > 0){//TODO: SETEA LA ALARMA CON EL NUMERO DE TAREAS PENDIENTES
            //Toast.makeText(context ,"hay tareas", Toast.LENGTH_SHORT).show()

            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 20)//8 pm
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0) //para alarma con timePicker

            startAlarm(cal, assignments?.size!!)

        }

        adaptador = AdaptadorCustomAssignment(assignments!!, object : ClickListener {
            override fun onClick(vista: View, index: Int) {
                //click
                val intent = Intent(context, DetalleAssignment::class.java)
                intent.putExtra("ID", assignments!![index].id?.toInt())
                startActivity(intent)
            }
        }, object : LongClickListener {
            override fun longClick(vista: View, index: Int) {}
        })

        lista?.adapter = adaptador


        return vista
    }


    fun startAlarm(c: Calendar, tareas: Int){
        var alarmManager: AlarmManager  = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent : Intent = Intent(context, ReminderBroadcast::class.java)
        intent.putExtra(TAG_TAREA, tareas.toString())
        var pendingIntent :PendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmManager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            c.timeInMillis,
            1000 * 60 * 60 * 12, //medio dia
            pendingIntent
        )

        //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)//alarma una vez a hora definida
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.timeInMillis,AlarmManager.INTERVAL_HALF_DAY , pendingIntent)// alarma repetitiva cada 12 hrs
    }

    fun cancelAlarm(){
        var alarmManager: AlarmManager  = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent : Intent = Intent(context, ReminderBroadcast::class.java)
        var pendingIntent :PendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

        alarmManager.cancel(pendingIntent)
    }


    fun reColor(){
        val typedValue = TypedValue()
        var theme = activity?.theme
        theme?.resolveAttribute(R.attr.colorAccent, typedValue, true)
        var colorAccent = typedValue.data

        theme?.resolveAttribute(R.attr.colorPrimary, typedValue, true)
        var colorPrimary = typedValue.data

        theme?.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true)
        var colorPrimaryDark = typedValue.data

    }


}