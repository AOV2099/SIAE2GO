package com.paper.webscrapper

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paper.ejemplodb.RecyclerView.ClickListener
import com.paper.ejemplodb.RecyclerView.LongClickListener
import com.paper.webscrapper.DataBase.Subject

class AdaptadorCustomSubject(items:ArrayList<Subject>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustomSubject.ViewHolder>()  {

    var items: ArrayList<Subject>? = null
    var multiSeleccion = false

    var itemsSeleccionados:ArrayList<Int>? = null
    var viewHolder: AdaptadorCustomSubject.ViewHolder? = null

    init {
        this.items = items
        itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.template_subject,parent,false)
        viewHolder = ViewHolder(vista, listener, longClickListener)

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }


    class ViewHolder(vista: View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener{

        var vista = vista

        var subject: TextView? = null //TODO AQUI CAMBIAR LOS PISHIS PARAMETROS POR LOS DE LA MATERIA
        var id: TextView?= null
        var day: TextView? = null
        var startHour: TextView? = null
        var endHour: TextView? = null
        var listener: ClickListener? = null
        var longListener: LongClickListener? = null
        var layoutBg : LinearLayout? = null
        var salon : TextView? = null
        //var colorBg: Int? = null
        var gradientDraw : GradientDrawable? = null

        init {

            subject = vista.findViewById(R.id.tvNombreSubject)
            day = vista.findViewById(R.id.tvDaySubject)
            startHour = vista.findViewById(R.id.tvStartHourSubject)
            endHour = vista.findViewById(R.id.tvEndHourSubject)
            id = vista.findViewById(R.id.tvIdSubject)
            layoutBg = vista.findViewById(R.id.linlayTemplateSubject)
            salon = vista.findViewById(R.id.tvSalon)

            gradientDraw  = layoutBg?.background?.mutate() as GradientDrawable

            this.listener = listener
            this.longListener = longClickListener

            vista.setOnClickListener(this)
            vista.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            this.listener?.onClick(v!!, adapterPosition)
        }

        override fun onLongClick(v: View?): Boolean {
            this.longListener?.longClick(v!!, adapterPosition)
            return true
        }

    }

    override fun onBindViewHolder(holder: AdaptadorCustomSubject.ViewHolder, position: Int) {

        val item = items?.get(position)

        holder.gradientDraw?.setColor(item?.color!!) // el color del botón

        holder.subject?.text = (item?.subject)
        holder.id?.text = ("id. de materia: "+ item?.id)
        //holder.day?.text = (item?.day.toString())
        holder.startHour?.text = ("Inicio: " + item?.startHour)

        when(item?.day){
            "Monday" -> holder.day?.text = ("Lunes")
            "Tuesday" -> holder.day?.text = ("Martes")
            "Wednesday" -> holder.day?.text = ("Miércoles")
            "Thursday" -> holder.day?.text = ("Jueves")
            "Friday" -> holder.day?.text = ("Viernes")
            "Saturday" -> holder.day?.text = ("Sabado")
            "Sunday" -> holder.day?.text = ("Domingo")
        }

        if (item?.endHour.toString() == ""){
            holder.endHour?.text = ("n/a")
        }else{
            holder.endHour?.text = ("Termino: " + item?.endHour)
        }

        if (item?.salon.toString() == ""){
            holder.salon?.text = ("Salón: n/a")
        }else{
            holder.salon?.text = ("Salón: " + item?.salon)
        }


        if(itemsSeleccionados?.contains(position)!!){//TODO: REVISAR
            //holder.vista.setBackgroundColor(Color.GRAY)
        }else{
            //holder.vista.setBackgroundColor(Color.WHITE)
        }
    }

}