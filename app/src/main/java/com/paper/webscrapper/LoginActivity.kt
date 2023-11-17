package com.paper.webscrapper

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.SocketTimeoutException
import kotlin.concurrent.thread
import kotlin.math.log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.CookieJar
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy
import okhttp3.Headers
import okhttp3.FormBody
import okhttp3.Headers.Companion.headersOf




class LoginActivity : AppCompatActivity() {

    var nombre : String? = null

    //private lateinit var loginViewModel: LoginViewModel
    var subRellay1: RelativeLayout? =  null
    var rellay2: RelativeLayout? =  null

    val SHARED_PREFS = "shared_prefs"
    val TEXT_USER = "text_user"
    val TEXT_PASS = "text_pass"
    val CHECKBOX1 = "check_box"

    //Shared preferences
    var cbStadeOnOff: Boolean? = null
    var text_user: String ? = null
    var text_pass: String ? = null

    var progressBar: ProgressBar? = null
    var etUser : EditText? = null
    var etPassword : EditText? = null
    var etCaptcha : EditText? = null
    var cbClaves: CheckBox? = null
    var btnLogin: Button? = null
    var ivCaptcha: ImageView? =  null

    val BASE_URL: String = "https://www.dgae-siae.unam.mx/"

    // Configurar CookieJar
    val myCookieJar = MyCookieJar()

    val okHttpClient = OkHttpClient.Builder()
        .cookieJar(myCookieJar)
        .build()

    var picasso: Picasso? = null

    // Crea una instancia de Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    //var btnForgotPass: Button? = null

    //var logoUNAM: ImageView? = null

    var layoutParams = RelativeLayout.CENTER_VERTICAL

    var handler: Handler = Handler()
    var runnable = Runnable(){

            //logoUNAM?.translationX =800f
    subRellay1?.visibility = View.VISIBLE
    rellay2?.visibility = View.VISIBLE

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //logoUNAM = findViewById(R.id.imLogoUNAMLogin)


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.gradientEnd));
        }

        //CAMBIA EL COLOR DEL STATUS BAR SOLO EN ESTA ACTIVIDAD
        var window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.gradientStart)

        progressBar = findViewById(R.id.progressBarLogIn)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        picasso = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(okHttpClient))
            .build()

        subRellay1 = findViewById(R.id.subRellay1)
        rellay2 = findViewById(R.id.rellay2)

        etUser = findViewById<EditText>(R.id.etUser)
        etPassword = findViewById<EditText>(R.id.etPassword)
        etCaptcha = findViewById(R.id.etCaptcha)
        btnLogin = findViewById<Button>(R.id.btnLogin)
        //btnForgotPass = findViewById(R.id.btnForgotPassword)
        cbClaves = findViewById<CheckBox>(R.id.cbRecordar)
        ivCaptcha = findViewById(R.id.ivCaptcha)


        var resourse : Resources = this.resources

        handler.postDelayed(runnable, 1500)

        btnLogin?.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    btnLogin?.setTextColor(resourse.getColor(R.color.gradientEnd))
                    btnLogin?.setBackgroundResource(R.drawable.btn_bg_fill)
                }

                MotionEvent.ACTION_UP -> {
                    btnLogin?.setTextColor(Color.WHITE)
                    btnLogin?.setBackgroundResource(R.drawable.btn_bg)
                }

            }
            v?.onTouchEvent(event) ?: true
        }

        btnLogin?.setOnClickListener {
            if (etUser?.text?.isBlank()!! || etPassword?.text?.isBlank()!! || etCaptcha?.text.isNullOrBlank()){

                Toast.makeText(this, "Por favor completa los datos", Toast.LENGTH_SHORT).show()
            }else{

                if(Network.hayRed(this)){
                    progressBar?.isVisible = true
                    btnLogin?.isEnabled = false
                    PASS = etPassword?.text.toString()
                    USER = etUser?.text.toString()
                    logIntoSiae(USER, PASS, etCaptcha?.text.toString())

                    if (cbClaves?.isChecked!!){
                        saveData()
                    }else{
                        cleanData()
                    }


                }else{
                    Toast.makeText(this, "Sin conexión a internet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadData()
        updateViews()
        loadPageAndExtractCaptchaUrl()

    }

    override fun onBackPressed() {
        /*finishAndRemoveTask();
        System.exit(0)*/

    }


    // Define la interfaz de servicio
    interface ApiService {
        @GET("lib/captcha.php")
        fun loadDgae(): Call<ResponseBody>

        @GET("/www_try.php")
        fun getScoreTable(): Call<ResponseBody>

        @GET("lib/captcha.php")
        fun fetchCaptcha(): Call<ResponseBody>

        @GET("www_gate.php")
        fun loadPageWithCaptcha(): Call<ResponseBody>

        @GET("mod_idn/www_idnt.php")
        fun getCurp(): Call<ResponseBody>

        @POST("www_gate.php")
        @FormUrlEncoded
        //@Headers("Content-Type: application/x-www-form-urlencoded")
        fun logIntoSiae(
            @Field("acc") acc: String,
            @Field("usr_logi") usrLogi: String,
            @Field("usr_pass") usrPass: String,
            @Field("captcha") captcha: String
        ): Call<Void>
    }

    // Cargar la página que contiene el captcha y extraer la URL de la imagen
    private fun loadPageAndExtractCaptchaUrl() {
        try {
            //limpiar cookies
            val myCookieJar = okHttpClient.cookieJar as MyCookieJar
            myCookieJar.clearCookies()

            val call = apiService.loadPageWithCaptcha().execute()

            if (call.isSuccessful) {
                // Imprime las cookies después de cargar la página
                val myCookieJar = okHttpClient.cookieJar as MyCookieJar
                val cookies = myCookieJar.loadForRequest(BASE_URL.toHttpUrlOrNull()!!)
                for (cookie in cookies) {
                    Log.d("cookie page load", "${cookie.name}: ${cookie.name}=${cookie.value}")
                }

                // Extraer la URL relativa de la imagen del captcha del HTML
                val htmlBody = call.body()?.string()
                val captchaRelativeUrl = extractCaptchaRelativeUrl(htmlBody)

                if (captchaRelativeUrl != null) {
                    // Convertir la URL relativa a una URL completa
                    val captchaFullUrl = BASE_URL.toHttpUrlOrNull()!!.newBuilder()
                        .addPathSegments(captchaRelativeUrl)
                        .build()

                    // Cargar la imagen del captcha
                    loadCaptchaImage(captchaFullUrl.toString())
                } else {
                    // Manejo de errores al extraer la URL relativa de la imagen del captcha
                    Log.e("Error", "No se pudo extraer la URL de la imagen del captcha")
                }
            } else {
                // Manejo de errores al cargar la página
                Log.e("Error", "No se pudo cargar la página")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // Extraer la URL relativa de la imagen del captcha del HTML (usando JSoup)
    private fun extractCaptchaRelativeUrl(html: String?): String? {
        return try {
            val doc = Jsoup.parse(html)
            val captchaImg = doc.select("img.captcha").first()
            captchaImg?.attr("src") // Devuelve la URL relativa de la imagen del captcha
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    // Cargar la imagen del captcha (usando Picasso)
    private fun loadCaptchaImage(imageUrl: String) {
        runOnUiThread {
            // Usa Picasso o la biblioteca de tu elección para cargar la imagen del captcha en tu ImageView
            picasso?.load(imageUrl)
                ?.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                ?.networkPolicy(NetworkPolicy.NO_CACHE)
                ?.into(ivCaptcha)
        }
    }

    private fun enableLoginButton() {
        progressBar?.isVisible = false
        btnLogin?.isEnabled = true
        btnLogin?.setTextColor(Color.WHITE)
        btnLogin?.setBackgroundResource(R.drawable.btn_bg)
    }

    private fun logIntoSiae(name: String, pass: String, captcha: String) {
        try {
           // val headers = headersOf("Content-Type", "application/x-www-form-urlencoded")

            // Crear una instancia de FormBody con los campos necesarios
            val formBody = FormBody.Builder()
                .add("acc", "aut")
                .add("usr_logi", name)
                .add("usr_pass", pass)
                .add("captcha", captcha)
                .build()

            val headers = Headers.Builder()
                .add("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
                .add("accept-language", "es-419,es;q=0.9")

            // Crear la solicitud con el método POST, encabezados y cuerpo
            val request = Request.Builder()
                .url("https://www.dgae-siae.unam.mx/www_gate.php")
                .post(formBody)
                .headers(headers.build())
                .build()

            // Imprime el cuerpo y los encabezados de la solicitud
            Log.d("POST Request", request.toString())
            Log.d("POST Headers", request.headers.toString())
            Log.d("POST Body", formBody.toString())

            val requestBody = StringBuilder()
            for (i in 0 until formBody.size) {
                requestBody.append("${formBody.name(i)}=${formBody.value(i)}")
                if (i < formBody.size - 1) {
                    requestBody.append("&")
                }
            }

            Log.d("POST Body", requestBody.toString())

            // Realizar la llamada síncrona
            val call = okHttpClient.newCall(request)
            val response = call.execute()


            if (response.isSuccessful) {

                val cookies = response.headers("Set-Cookie")

                if (response.body?.let { checkAccessSuccess(it.string()) } == true){
                    Log.d("ACCESO", "CORRECTO")


                    //loadPage("$BASE_URL/www_try.php")
                    getUserCurp()
                    getUserCareer()
                    //getUserGrades()

                    //val intent = Intent(this, MainActivity::class.java)
                    val intent = Intent(this, NavActivity::class.java)
                    startActivity(intent)

                }else{
                    Log.d("ACCESO", "FALLIDO")
                    Toast.makeText(this, "IMPOSIBLE ACCEDER", Toast.LENGTH_LONG).show()
                    enableLoginButton()
                    loadPageAndExtractCaptchaUrl()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkAccessSuccess(htmlResponse: String): Boolean {
        return try {
            val doc = Jsoup.parse(htmlResponse)
            val titleElement = doc.select("title").first()
            val title = titleElement?.text()

            // Verificar si el título coincide con el esperado
            title?.contains("SIAE [Panel de Control]", ignoreCase = true) == true
        } catch (e: Exception) {
            e.printStackTrace()
            false // Retorna falso si ocurre una excepción
        }
    }

    private fun filterCookies(){
        val myCookieJar = okHttpClient.cookieJar as MyCookieJar

        // Imprimir cookies antes de limpiarlas
        val cookiesBeforeClear = myCookieJar.loadForRequest(BASE_URL.toHttpUrlOrNull()!!)
        for (cookie in cookiesBeforeClear) {
            Log.d("Cookie PHP Before Clear", "${cookie.name}: ${cookie.value}")
        }

        // Eliminar solo la primera cookie si hay más de una
        val cookies = myCookieJar.loadForRequest(BASE_URL.toHttpUrlOrNull()!!)
        if (cookies.size > 2) {
            val cookiesToKeep = cookies.drop(1) // Mantener todas excepto la primera
            myCookieJar.clearCookies()
            myCookieJar.saveFromResponse(BASE_URL.toHttpUrlOrNull()!!, cookiesToKeep)
        } else {
            // No hacer nada si solo hay una cookie o ninguna
        }
    }

    private fun loadPage(url: String) {
        try {
            filterCookies()

            val request = Request.Builder()
                .url(url)
                .build()

            val call = okHttpClient.newCall(request).execute()

            if (call.isSuccessful) {
                // Imprimir cookies después de cargar la página
                val newCookies = myCookieJar.loadForRequest(BASE_URL.toHttpUrlOrNull()!!)
                for (cookie in newCookies) {
                    Log.d("Cookie PHP After Clear", "${cookie.name}: ${cookie.value}")
                }

                Log.d("BODY", call.body?.string())
            } else {
                // Manejo de errores al cargar la página
                Log.e("Error", "No se pudo cargar la página")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getUserGrades(llave: String){
        try {
            // Filtrar cookies antes de la solicitud
            filterCookies()

            val url = "https://www.dgae-siae.unam.mx/www_try.php?cta= $USER&llave=$llave&acc=hsa"
            Log.d("Grades url", url )
            val request = Request.Builder().url(url).build()

            val call = okHttpClient.newCall(request).execute()

            if (call.isSuccessful) {
                val htmlBody = call.body?.string()
                Log.d("Grades key", llave )
                Log.d("Grades", htmlBody )

                // Procesar HTML solo si hay un cuerpo presente
                htmlBody?.let {
                    GRADES_DOC = Jsoup.parse(it)

                }
            } else {
                // Manejar errores si la llamada no es exitosa
                Log.e("UserDataError", "Call was not successful: ${call.code}")
                Log.e("UserDataError", "Message: ${call.message}")
            }
        } catch (e: Exception) {
            // Manejar excepciones generales
            e.printStackTrace()
        }

    }

    private fun getUserCareer(){
        try {
            // Filtrar cookies antes de la solicitud
            filterCookies()

            val url = "https://www.dgae-siae.unam.mx/www_try.php"
            val request = Request.Builder().url(url).build()

            val call = okHttpClient.newCall(request).execute()

            if (call.isSuccessful) {
                val htmlBody = call.body?.string()

                // Procesar HTML solo si hay un cuerpo presente
                htmlBody?.let {
                    val document = Jsoup.parse(it)

                    var key = document.select("[name = llave]")
                    var numKeys = key.size

                    if (numKeys > 1){
                        CARRERA = document.select("td.CellDat[align = left]").last().text().toString()
                    }else{
                        CARRERA = document.select("td.CellDat[align = left]").text().toString()
                    }

                    getUserGrades(key.`val`().toString())

                    var imagen = document.getElementsByClass("foto_alumno")//todo: FOTO DEL ALUMNO
                    ///IMG_BYTE_ARRAY = Base64.getMimeDecoder().decode(imagen.attr("src").split(",")[1]) //todo: necesita api oreo
                    if (imagen.size == 0) {
                        val img = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASwAAAEsCAYAAAB5fY51AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJ"

                        IMG_BYTE_ARRAY = android.util.Base64.decode(
                            img.split(",")[1],
                            android.util.Base64.DEFAULT
                        )
                        runOnUiThread {
                            Toast.makeText(this, "Sin imagen disponible", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        IMG_BYTE_ARRAY = android.util.Base64.decode(
                            imagen.attr("src").split(",")[1], android.util.Base64.DEFAULT
                        )
                    }

                }
            } else {
                // Manejar errores si la llamada no es exitosa
                Log.e("UserDataError", "Call was not successful: ${call.code}")
                Log.e("UserDataError", "Message: ${call.message}")
            }
        } catch (e: Exception) {
            // Manejar excepciones generales
            e.printStackTrace()
        }

    }


    private fun getUserCurp() {
        try {
            // Filtrar cookies antes de la solicitud
            filterCookies()

            val url = "https://www.dgae-siae.unam.mx/mod_idn/www_idnt.php"
            val request = Request.Builder().url(url).build()

            val call = okHttpClient.newCall(request).execute()

            if (call.isSuccessful) {
                val htmlBody = call.body?.string()

                // Procesar HTML solo si hay un cuerpo presente
                htmlBody?.let {
                    val document = Jsoup.parse(it)

                    // Obtener información del usuario
                    CURP = document.getElementsByClass("CellDat")[2].text()
                    NAME = document.getElementsByClass("CellDat")[1].text()

                    val key = document.select("[name = llave]")
                    numKeys = key.size

                    /*CARRERA = if (numKeys > 1) {
                        document.select("td.CellDat[align = left]").last().text().toString()
                    } else {
                        document.select("td.CellDat[align = left]").text().toString()
                    }*/

                    // Log de la información del usuario
                    //Log.d("USERDATA", "Carrera: $CARRERA")
                    Log.d("USERDATA", "Nombre: $NAME")
                    Log.d("USERDATA", "CURP: $CURP")

                }
            } else {
                // Manejar errores si la llamada no es exitosa
                Log.e("UserDataError", "Call was not successful: ${call.code}")
                Log.e("UserDataError", "Message: ${call.message}")
            }
        } catch (e: Exception) {
            // Manejar excepciones generales
            e.printStackTrace()
        }
    }
    private fun obtainPhpSessionId(response: Response): String? {
        var newPhpSessionId: String? = null
        if (response.isSuccessful) {
            val cookies = response.headers("Set-Cookie")
            for (cookie in cookies) {
                if (cookie.startsWith("PHPSESSID=")) {
                    newPhpSessionId = cookie.substringAfter("PHPSESSID=").substringBefore(";")
                    Log.d("PHPSESSID new", newPhpSessionId)
                    break
                }
            }
        }
        return newPhpSessionId
    }



    fun saveData(){
        var sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.putString(TEXT_USER, USER)
        editor.putString(TEXT_PASS, PASS)
        editor.putBoolean(CHECKBOX1, cbClaves?.isChecked!!)
        //editor.putString("text_theme", "NOT DEFAULT")

        editor.apply()

        //Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    fun cleanData(){
        var sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        var editor = sharedPreferences.edit()

        editor.remove(TEXT_USER)
        editor.remove(TEXT_PASS)
        editor.remove(CHECKBOX1)
        editor.apply()
        editor.clear()
    }

    fun loadData(){
        var sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        text_user = sharedPreferences.getString(TEXT_USER, "").toString()
        text_pass = sharedPreferences.getString(TEXT_PASS, "").toString()
        cbStadeOnOff = sharedPreferences.getBoolean(CHECKBOX1, false)
    }

    fun updateViews(){
        etUser?.setText(text_user)
        etPassword?.setText(text_pass)
        cbClaves?.setChecked(cbStadeOnOff!!)
    }

}