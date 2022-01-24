package com.paper.webscrapper

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_theme.*


class ThemeFragment : Fragment() {

    val SHARED_PREFS = "shared_prefs"
    val TEXT_THEME = "text_theme"
    var text_theme: String ? = null
    var textSize = 24.0f
    var textSize_small = 14.0f

    var vista : View? = null

    var btnThemeDefault : Button? = null
    var btnThemeUNAM : Button? = null
    var btnThemeBlack : Button? = null
    var btnThemePurple : Button? = null
    var btnThemeRed : Button? = null
    var btnThemeGreen : Button? = null
    var btnThemeCyan : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.fragment_theme, container, false)
        // Inflate the layout for this fragment

        btnThemeDefault = vista?.findViewById(R.id.btnThemeBlue)
        btnThemeUNAM = vista?.findViewById(R.id.btnThemeUNAM)
        btnThemeBlack = vista?.findViewById(R.id.btnThemeBlack)
        btnThemePurple = vista?.findViewById(R.id.btnThemePurple)
        btnThemeRed = vista?.findViewById(R.id.btnThemeRed)
        btnThemeGreen = vista?.findViewById(R.id.btnThemeGreen)
        btnThemeCyan = vista?.findViewById(R.id.btnThemeCyan)

        loadData()
        updateViews()


        btnThemeDefault?.setOnClickListener {
            saveData("default")
            updateTextOnTouch()
            btnThemeDefault?.textSize = textSize

            showAlertDialog()
        }

        btnThemeUNAM?.setOnClickListener {
            saveData("unam")
            updateTextOnTouch()
            btnThemeUNAM?.textSize = textSize

            showAlertDialog()
        }


        btnThemeBlack?.setOnClickListener {
            saveData("black")
            updateTextOnTouch()
            btnThemeBlack?.textSize = textSize

            showAlertDialog()
        }

        btnThemePurple?.setOnClickListener {
            saveData("purple")
            updateTextOnTouch()
            btnThemePurple?.textSize = textSize

            showAlertDialog()
        }

        btnThemeRed?.setOnClickListener {
            saveData("red")
            updateTextOnTouch()
            btnThemeRed?.textSize = textSize

            showAlertDialog()
        }

        btnThemeGreen?.setOnClickListener {
            saveData("green")
            updateTextOnTouch()
            btnThemeGreen?.textSize = textSize

            showAlertDialog()
        }

        btnThemeCyan?.setOnClickListener {
            saveData("cyan")
            updateTextOnTouch()
            btnThemeCyan?.textSize = textSize

            showAlertDialog()
        }

        return vista
    }


    fun saveData(strTema: String){
        var sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        var editor = sharedPreferences?.edit()
        editor?.putString(TEXT_THEME, strTema)
        editor?.apply()
    }

    fun loadData(){
        var sharedPreferences = activity?.getSharedPreferences(SHARED_PREFS, AppCompatActivity.MODE_PRIVATE)
        text_theme = sharedPreferences?.getString(TEXT_THEME, "default").toString()
    }

    fun updateViews(){
        when(text_theme){

            "default" -> {
                btnThemeDefault?.textSize = textSize

            }

            "unam" -> {
                btnThemeUNAM?.textSize = textSize

            }

            "black" -> {
                btnThemeBlack?.textSize = textSize

            }

            "purple" -> {
                btnThemePurple?.textSize = textSize

            }

            "red" -> {
                btnThemeRed?.textSize = textSize

            }

            "green" -> {
                btnThemeGreen?.textSize = textSize
            }

            "cyan" -> {
                btnThemeCyan?.textSize = textSize
            }

        }
    }

    fun updateTextOnTouch(){
        btnThemeDefault?.textSize = textSize_small

        btnThemeUNAM?.textSize = textSize_small

        btnThemeBlack?.textSize = textSize_small

        btnThemePurple?.textSize = textSize_small

        btnThemeRed?.textSize = textSize_small

        btnThemeGreen?.textSize = textSize_small

        btnThemeCyan?.textSize = textSize_small

    }

    fun showAlertDialog(){
        var alertDialog = AlertDialog.Builder(requireContext())
        alertDialog.setMessage("Es necesario reiniciar la app para concluir los cambios. \n¿Desea reiniciar ahora?")
        alertDialog.setPositiveButton("Sí", DialogInterface.OnClickListener { dialog, which ->
            Runtime.getRuntime().exit(0)
        })

        alertDialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })

        var dialog = alertDialog.create()
        dialog.show()
    }

}