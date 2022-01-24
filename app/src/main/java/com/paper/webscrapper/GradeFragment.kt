package com.paper.webscrapper

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_grades.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.lang.RuntimeException
import java.net.SocketTimeoutException
import kotlin.concurrent.thread


var carrera = ""
var title = ""
var plantel = ""
var ingreso = ""
var llave = ""
var llaveFacu = ""
var promedio = ""



class GradeFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var nivelInstitu = 0//Verifica si la institucion será mediaSuperior o superior

        if (titleEventUpdated == "Prepa/CCH" ){
            nivelInstitu = 0
        }
        if (titleEventUpdated == "Facultad"){
            nivelInstitu = 1
        }

            retriveWebInfo(nivelInstitu)
            //cambiarImagenCarga(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        var vista = inflater.inflate(R.layout.fragment_grades, container,false)

        return vista
    }

    private fun retriveWebInfo(valFacu: Int){
        thread {
            if(Network.hayRed(activity as AppCompatActivity)){
                try {

                    var response = Jsoup.connect("https://www.dgae-siae.unam.mx/www_gate.php")
                        .timeout(5000)// tiempo maximo de conexión o fallo
                        .data("acc", "aut")
                        .data("usr_logi", USER)
                        .data("usr_pass", PASS)
                        .method(Connection.Method.POST)
                        .execute()

                    var ckie = response.cookies()

                    var document = Jsoup.connect("https://www.dgae-siae.unam.mx/www_try.php")
                        .cookies(ckie)
                        .get()

                    //title = document.title() //TITULO DE LA PAG

                    if (numKeys >= 2) {//Valida si hay mas de una institucion o nivel académico
                        llave = document.select("[name = llave]")[valFacu].`val`().toString()
                    } else {
                        llave = document.select("[name = llave]").`val`().toString()
                    }


                    var documentCalif =
                        Jsoup.connect("https://www.dgae-siae.unam.mx/www_try.php?cta=" + USER + "&llave=" + llave + "&acc=hsa")
                            .timeout(5000)// tiempo maximo de conexión o fallo
                            .cookies(ckie)
                            .get()


                    var str = documentCalif.text()

                    var tableCalif =
                        documentCalif.getElementsByTag("tbody")[9].toString()  //el 5 es para los promedios

                    plantel = documentCalif.select("td.CellTns[colspan = 2] > b")[0].text()
                    carrera = documentCalif.select("td.CellTns[colspan = 2] > b")[1].text()
                    promedio = documentCalif.select("td.CellTns > p > b").text()
                    ingreso = documentCalif.select("td.CellTns > b")[2].text()


                    var tableCalifRows = documentCalif.select(" tr  [align=center] ")
                    var tableCalifCols = documentCalif.select(" tr  [align=center] > td")


                    //var lista2d = ArrayList< ArrayList<String> >()
                    val listaTr = ArrayList<ArrayList<Any>>()

                    for (tr in tableCalifRows) {

                        var listTd = ArrayList<Any>()
                        //println("--------------> ROW")

                        for (td in tr.children()) {

                            listTd.add(td.text())
                            // println("TD > " + td.text())
                        }
                        listaTr.add(listTd)
                    }

                    activity?.runOnUiThread {

                        tvPromedio.text = (promedio)
                        tvPlantel.text = (plantel)
                        tvIngreso.text = ("AÑO DE INGRESO: " + ingreso + ".")
                        tvCarrera.text = (carrera + ".")


                        var arrayListRow = ArrayList<Any>()

                        var col = TextView(activity?.applicationContext)
                        listaTr.remove(listaTr.last())//texto indeseado

                        for ((index, tRow) in listaTr.withIndex()) {

                            var tr = TableRow(activity?.applicationContext)

                            if (index >= 15) {

                                if (listaTr[index].size == 1) { //TITULOS
                                    //columna completa
                                    var resourse: Resources = this.resources
                                    //println("------>" + listaTr[index])
                                    arrayListRow.add(listaTr[index].toString())
                                    var col = TextView(activity?.applicationContext)
                                    col.textSize = 15f
                                    col.setTextColor(resourse.getColor(R.color.white))
                                    col.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    //col.elevation = 10f
                                    col.text = listaTr[index].toString()
                                    //col.width = trHeader!!.width


                                    tr.gravity = 1
                                    val typedValue = TypedValue()
                                    var theme = context?.theme

                                    tr.setBackgroundColor(ThemeColors.getAccent(requireContext()))
                                    tr.addView(col)


                                } else { //CONTENIDO DE MATERIAS
                                    //columnas divididas
                                    var arrayListCol = ArrayList<String>()
                                    var resourse: Resources = this.resources

                                    arrayListCol.add(listaTr[index][4].toString())//MATERIA
                                    arrayListCol.add(listaTr[index][5].toString())//CALIF
                                    arrayListCol.add(listaTr[index][6].toString())//EXAMEN
                                    arrayListCol.add(listaTr[index][9].toString())//GRUPO

                                    arrayListRow.add(arrayListCol)

                                    var colMateria = TextView(activity?.applicationContext)
                                    var colCalif = TextView(activity?.applicationContext)
                                    var colExamen = TextView(activity?.applicationContext)
                                    var colGrupo = TextView(activity?.applicationContext)

                                    colMateria.textSize = 15f
                                    //colMateria.width = 200
                                    //colMateria.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    colMateria.setPadding(25, 0, 0, 0)
                                    colMateria.text = listaTr[index][4].toString()

                                    colCalif.textSize = 15f
                                    //colCalif.width = 100
                                    colCalif.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    colCalif.text = listaTr[index][5].toString()

                                    colExamen.textSize = 15f
                                    //colExamen.width = 100
                                    colExamen.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    colExamen.text = listaTr[index][6].toString()

                                    colGrupo.textSize = 15f
                                    //colGrupo.width = 100
                                    colGrupo.textAlignment = View.TEXT_ALIGNMENT_CENTER
                                    colGrupo.text = listaTr[index][9].toString()

                                    tr.background = resourse.getDrawable(R.drawable.border)

                                    tr.addView(colMateria)
                                    tr.addView(colCalif)
                                    tr.addView(colExamen)
                                    tr.addView(colGrupo)

                                }
                                uiVisible()
                                tlTableCalif?.addView(tr)
                            }

                        }
                        //println(arrayListRow)
                    }
                }catch (e: RuntimeException){
                    println("ERROR--------------->$e")
                    activity?.runOnUiThread{
                        Toast.makeText(context, "Error con cuentas", Toast.LENGTH_SHORT).show()
                    }
                }catch (e: SocketTimeoutException){
                    println("ERROR--------------->$e")
                    activity?.runOnUiThread{
                        Toast.makeText(context, "Imposible conectar con el SIAE", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                activity?.runOnUiThread{
                    Toast.makeText(context, "Sin conexión a Internet", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }

    fun uiVisible(){
        constraintLoading?.visibility = View.GONE
        //uiCalif.visibility = View.VISIBLE //MUESTRA LA TABLA LLENA CON EL UI
        layoutContent?.visibility = View.VISIBLE

    }

}