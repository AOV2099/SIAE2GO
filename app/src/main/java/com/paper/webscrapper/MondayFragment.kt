package com.paper.webscrapper

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paper.ejemplodb.RecyclerView.ClickListener
import com.paper.ejemplodb.RecyclerView.LongClickListener
import com.paper.webscrapper.DataBase.Subject
import com.paper.webscrapper.DataBase.SubjectCRUD


class DaySelectedMonday : Fragment() {
    var allowRefreshDaySelected = true
    //var TAG_TAREA = "com.paper.webscrapper.SubjectsFragment.TAG_TAREA"

    var lista: RecyclerView? = null
    var adaptador: AdaptadorCustomSubject? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var vista : View? = null
    var subjects: ArrayList<Subject>? = null
    var crud : SubjectCRUD? = null
    var currDay: String? = "Monday"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        if (allowRefreshDaySelected)
        {
            allowRefreshDaySelected = false;
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
        var vista = inflater.inflate(R.layout.fragment_day_selected, container, false)

        lista = vista?.findViewById(R.id.rvSubjectsList)
        lista?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        lista?.layoutManager = layoutManager

        //Actualiza dependiendo del theme

        crud = SubjectCRUD(requireContext()) //TODO AQUI IBA EL CONTEXTO THIS
        subjects = crud?.getSubjectsByDay(currDay.toString())// hace consulta a la deb y asigna los valores en el array

        adaptador = AdaptadorCustomSubject(subjects!!, object : ClickListener {
            override fun onClick(vista: View, index: Int) {
                //click
                val intent = Intent(context, DetalleSubject::class.java)
                intent.putExtra("TAG_SUBJECT", subjects!![index].subject)
                intent.putExtra("TAG_START_HOUR", subjects!![index].startHour)
                startActivity(intent)
            }
        }, object : LongClickListener {
            override fun longClick(vista: View, index: Int) {}
        })

        lista?.adapter = adaptador

        return vista
    }


}