package com.paper.webscrapper

import android.graphics.drawable.GradientDrawable
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paper.ejemplodb.RecyclerView.ClickListener
import com.paper.ejemplodb.RecyclerView.LongClickListener
import com.paper.webscrapper.DataBase.Assignment


class AdaptadorCustomAssignment(items:ArrayList<Assignment>, var listener: ClickListener, var longClickListener: LongClickListener): RecyclerView.Adapter<AdaptadorCustomAssignment.ViewHolder>() {


    var items: ArrayList<Assignment>? = null
    var multiSeleccion = false

    var itemsSeleccionados:ArrayList<Int>? = null
    var viewHolder: ViewHolder? = null

    init {
        this.items = items
        itemsSeleccionados = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.template_assignment,parent,false)
        viewHolder = ViewHolder(vista, listener, longClickListener)

        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return items?.count()!!
    }


    class ViewHolder(vista: View, listener: ClickListener, longClickListener: LongClickListener): RecyclerView.ViewHolder(vista), View.OnClickListener, View.OnLongClickListener{

        var vista = vista

        var subject: TextView? = null //TODO AQUI CAMBIAR LOS PISHIS PARAMETROS POR LOS DE LA TAREA
        var id: TextView?= null
        var date: TextView? = null
        var hour: TextView? = null
        var listener: ClickListener? = null
        var longListener: LongClickListener? = null
        var layoutBg : LinearLayout? = null
        //var colorBg: Int? = null
        var gradientDraw : GradientDrawable? = null

        init {

            subject = vista.findViewById(R.id.tvNombreAssignment)
            date = vista.findViewById(R.id.tvFechaAssignment)
            hour = vista.findViewById(R.id.tvHoraAssignment)
            id = vista.findViewById(R.id.tvIdAssignment)
            layoutBg = vista.findViewById(R.id.linlayTemplateAssignment)

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


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items?.get(position)

        holder.gradientDraw?.setColor(item?.color!!) // el color del boton
       /* holder.subject?.text = ( "Materia: " + item?.subject)
        holder.id?.text = ( "No. de tarea: "+ item?.id)
        holder.date?.text = ( "Entrega: " + item?.date)
        holder.hour?.text = ( "Hora: " + item?.hour)*/

        holder.subject?.text = (item?.subject)
        holder.id?.text = ("No. de tarea: "+ item?.id)

        if (item?.date.toString() == ""){
            holder.date?.text = ("Entrega: n/a")
        }else{
            holder.date?.text = ("Entrega: " + item?.date)
        }

        if (item?.hour.toString() == ""){
            holder.hour?.text = ("Hora: n/a")
        }else{
            holder.hour?.text = ("Hora: " + item?.hour)
        }

        if(itemsSeleccionados?.contains(position)!!){//TODO: REVISAR
            //holder.vista.setBackgroundColor(Color.GRAY)
        }else{
            //holder.vista.setBackgroundColor(Color.WHITE)
        }
    }

    fun iniciarActionMode(){
        multiSeleccion = true
    }

    fun destruirActionMode(){
        multiSeleccion = false
        itemsSeleccionados?.clear()
        notifyDataSetChanged()
    }

    fun terminarActionMode(){
        // eliminar elementos seleccionados
        for(item in itemsSeleccionados!!){
            itemsSeleccionados?.remove(item)
        }
        multiSeleccion = false
        notifyDataSetChanged()
    }

    fun seleccionarItem(index:Int){
        if(multiSeleccion){
            if(itemsSeleccionados?.contains(index)!!){
                itemsSeleccionados?.remove(index)
            }else{
                itemsSeleccionados?.add(index)
            }

            notifyDataSetChanged()
        }
    }

    fun obtenerNumeroElementosSeleccionados():Int{
        return itemsSeleccionados?.count()!!
    }

    fun eliminarSeleccionados(){
        if(itemsSeleccionados?.count()!! > 0){
            var itemsEliminados = ArrayList<Assignment>()

            for(index in itemsSeleccionados!!){
                itemsEliminados.add(items?.get(index)!!)
            }

            items?.removeAll(itemsEliminados)
            itemsSeleccionados?.clear()
        }
    }



}