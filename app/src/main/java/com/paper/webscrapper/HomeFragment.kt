package com.paper.webscrapper

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.concurrent.thread


class HomeFragment : Fragment()  {

    var vista : View? = null
    var TAG_TAREA = "com.paper.webscrapper.HomeFragment.TAG_TAREA"
    var  imgFoto: ImageView? = null
    var tvNombre: TextView? = null
    var tvApellido: TextView? = null
    var tvCuenta: TextView? = null
    var tvCURP: TextView? = null
    var tvEscuela: TextView? = null
    var ivQR : ImageView? = null
    var cvFoto : CardView? = null
    var cvContainer : CardView? = null
    var constraintBg : ConstraintLayout? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        vista = inflater.inflate(R.layout.fragment_home, container,false)
        imgFoto = vista?.findViewById(R.id.imgFoto)
        tvNombre = vista?.findViewById(R.id.tvNombre)
        tvApellido = vista?.findViewById(R.id.tvApellido)
        tvCuenta = vista?.findViewById(R.id.tvCuenta)
        tvCURP = vista?.findViewById(R.id.tvCURP)
        tvEscuela = vista?.findViewById(R.id.tvEscuelaAct)
        ivQR = vista?.findViewById(R.id.ivAlumnoQR)
        cvFoto = vista?.findViewById(R.id.cardViewFoto)
        constraintBg = vista?.findViewById(R.id.constraintBgHome)
        imgFoto?.setImageBitmap(BitmapFactory.decodeByteArray(IMG_BYTE_ARRAY,0 , IMG_BYTE_ARRAY!!.size))


        //var bg = ThemeColors.getHomeBg(requireContext())
        //constraintBg?.setBackgroundResource(bg)

        var height = 1000
        var width = 1000

        thread { //qr

            var qrw  = QRCodeWriter()
            var bitMatrix = qrw.encode("NOMBRE: " + NAME +"\n"+
                    "BOLETA: " + USER +"\n"+
                    "CURP: " + CURP + "\n" +
                    "CARRERA: " + CARRERA +"\n",
                BarcodeFormat.QR_CODE, width, height)

            var bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565 )

            for(x in 0 until width ){
                for(y in 0 until height){
                    bitmap.setPixel(  x, y, if(bitMatrix.get(x,y)) Color.BLACK else Color.WHITE )
                }
            }

            activity?.runOnUiThread(){
                ivQR?.setImageBitmap(bitmap)
            }

        }

        try {
            tvNombre?.text = NAME.split(" ")[2] + " " + NAME.split(" ")[3].toString()
            tvApellido?.text = NAME.split(" ")[0] + " " + NAME.split(" ")[1].toString()

        }catch (e: java.lang.RuntimeException){
            tvNombre?.text = NAME
            tvApellido?.text = " "
        }

        tvCuenta?.text = "BOLETA: " + USER
        tvCURP?.text = "CURP: " + CURP
        tvEscuela?.text = "CARRERA: " + CARRERA


        return vista
    }


}