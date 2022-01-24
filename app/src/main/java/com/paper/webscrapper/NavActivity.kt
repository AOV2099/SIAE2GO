package com.paper.webscrapper

//import com.paper.webscrapper.R.id.switchGrades
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.paper.webscrapper.R.id.btnInstituto
import com.paper.webscrapper.R.id.instit_item
import kotlinx.android.synthetic.main.activity_nav.*
import kotlinx.android.synthetic.main.nav_header.*


var titleEventUpdated : String? = null

//var switch : Switch? = null

class NavActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    var drawer: DrawerLayout? = null
    var optionEventUpdated : Boolean? = false

    var itemActual = 0
    var itemSelected = 0

    val SHARED_PREFS = "shared_prefs"
    val TEXT_THEME = "text_theme"
    var text_theme: String ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
        updateTheme()

        setContentView(R.layout.activity_nav)
        toolbar.setTitle("ALUMNO")
        reColor() //Actualiza dependiendo del theme

        titleEventUpdated = "Prepa/CCH"

        drawer = drawer_layout

        nav_viewer.setNavigationItemSelectedListener(this)

        var toggle : ActionBarDrawerToggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()


        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                HomeFragment()
            ).commit()//default fragment
            nav_viewer.setCheckedItem(R.id.nav_home)//default selected button
        }

        drawer?.openDrawer(GravityCompat.START)

    }

    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)){ // TODO FIX
            drawer?.closeDrawer(GravityCompat.START)
        } else{
            drawer?.openDrawer(GravityCompat.START)
           //super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        tvMainID.text = USER//set navbar's textview text
        tvMainNombre.text = NAME//set navbar's textview text

            /*menu?.findItem(btnInstituto)?.isVisible = optionEventUpdated != false
            menu?.findItem(instit_item)?.isVisible = optionEventUpdated != false*/

        if(numKeys > 1){//Valida si ha pertenecido a más de una escuela y oculta o muestra el boton de selección
            menu?.findItem(btnInstituto)?.isVisible = optionEventUpdated != false //TODO: Cambiar condicional por item actual
            menu?.findItem(instit_item)?.isVisible = optionEventUpdated != false
        }else{
            menu?.findItem(btnInstituto)?.setVisible(false)
            menu?.findItem(instit_item)?.setVisible(false)
        }

        menu?.findItem(instit_item)?.title = titleEventUpdated // cambia el titulo del item

       /* val textItem = menu?.findItem(instit_item)?.actionView as TextView //para cuando el item con texto tiene fondo

        //textItem.text =  "PREPA/CCH"

        if (titleEventUpdated == "Prepa/CCH"){
            textItem.text =  "PREPA/CCH"

        }else{
            textItem.text =  "FACULTAD"
        } */


        return super.onCreateOptionsMenu(menu)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //waits for a button to be selected and loads the fragment
        when(item.itemId){

            R.id.nav_home -> {
                itemSelected = 0
                if (itemActual != itemSelected) {
                    toolbar.setTitle("ALUMNO")
                    optionEventUpdated = false

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment()).commit()
                    //.replace(R.id.fragment_container, HomeFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 0
                }

            }


            R.id.nav_grades -> {
                itemSelected = 1
                if (itemActual != itemSelected) {
                    toolbar.setTitle("CALIF")
                    optionEventUpdated = true
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GradeFragment()).commit()
                    itemActual = 1
                }
            }

            R.id.nav_assignments -> {
                itemSelected = 2
                if (itemActual != itemSelected) {
                    toolbar.setTitle("TAREAS")
                    optionEventUpdated = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AssignmentsFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 2
                }
            }

            R.id.nav_calendario -> {
                itemSelected = 3
                if (itemActual != itemSelected) {
                    toolbar.setTitle("Calendario UNAM")
                    optionEventUpdated = false

                    var fragment: Fragment =
                        PdfViewerFragment().newInstance("http://escolar1.unam.mx/pdfs/calendario_anual2021.pdf")

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 3
                }
            }

            R.id.nav_agenda -> {
                itemSelected = 4
                if (itemActual != itemSelected) {
                    toolbar.setTitle("Agenda")
                    optionEventUpdated = false

                    var fragment: Fragment =
                        PdfViewerFragment().newInstance("https://www.gaceta.unam.mx/agenda/htdocs/agenda.pdf")

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment).commit()
                    //.replace(R.id.fragment_container, HomeFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 4
                }


            }

            R.id.nav_classroom -> {
                itemSelected = 5
                if (itemActual != itemSelected) {
                    //toolbar.setTitle("Classroom")
                    optionEventUpdated = false
                    try {
                        var intent =
                            packageManager.getLaunchIntentForPackage("com.google.android.apps.classroom")
                        startActivity(intent)
                    } catch (e: RuntimeException) {
                        Toast.makeText(this, "Classroom no instalado", Toast.LENGTH_SHORT).show()
                    }

                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = -1
                }
            }

            R.id.nav_horarios -> {
                itemSelected = 8
                if (itemActual != itemSelected) {
                    toolbar.setTitle("HORARIO")
                    optionEventUpdated = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HorarioFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 8
                }
            }

            //-------------------------------------------------------------------------------------

            R.id.nav_theme -> {
                itemSelected = 6
                if (itemActual != itemSelected) {
                    toolbar.setTitle("Tema")
                    optionEventUpdated = false
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ThemeFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu
                    itemActual = 6
                }
            }

            R.id.nav_contact -> {
                Toast.makeText(this, "Elige una app de correo", Toast.LENGTH_LONG).show()
                itemSelected = 7
                if (itemActual != itemSelected) {
                    optionEventUpdated = false

                    var recipient = "aov2099@gmail.com"
                    var subject = "Comentarios APP"
                    var message = "Mensaje para desarrolladores:"
                    var intent = Intent(Intent.ACTION_SEND)
                    intent.setData(Uri.parse("mailto:"))
                    intent.setType("text/plain")

                    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                    intent.putExtra(Intent.EXTRA_TEXT, message)

                    intent.setType("message/rfc822")
                    startActivity(Intent.createChooser(intent, "Elige una app de correo"))
                    /*supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ThemeFragment()).commit()
                    invalidateOptionsMenu() // calls onCreateOptionsMenu*/
                    itemActual = -1
                }
            }

            R.id.nav_share -> {
                Toast.makeText(this, "Próximamente", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_exit -> {
                val intent = Intent(this, LoginActivity::class.java)
                //FragmentTransaction
                startActivity(intent)
            }

        }

        drawer?.closeDrawer(GravityCompat.START)

        return true
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){

            (R.id.btnInstituto) -> {

                if (titleEventUpdated == "Prepa/CCH") { //CAMBIA LOS ESTADOS DEL titleEventUpdated
                    titleEventUpdated = "Facultad"

                } else {
                    titleEventUpdated = "Prepa/CCH"
                }

                toolbar.setTitle("CALIF")
                optionEventUpdated = true
                invalidateOptionsMenu() // calls onCreateOptionsMenu
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    GradeFragment()
                ).commit()
                //Toast.makeText(this, titleEventUpdated, Toast.LENGTH_SHORT).show()
                return true
            }

            (R.id.instit_item) -> {

                if (titleEventUpdated == "Prepa/CCH") { //CAMBIA LOS ESTADOS DEL titleEventUpdated
                    titleEventUpdated = "Facultad"

                } else {
                    titleEventUpdated = "Prepa/CCH"
                }

                toolbar.setTitle("CALIF")
                optionEventUpdated = true
                invalidateOptionsMenu() // calls onCreateOptionsMenu
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    GradeFragment()
                ).commit()
                //Toast.makeText(this, titleEventUpdated, Toast.LENGTH_SHORT).show()
                return true
            }


            else -> (return super.onOptionsItemSelected(item))
        }

    }

    fun reColor(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccent));
            getWindow().setNavigationBarColor(ThemeColors.getPrimary(this))
        }

        setSupportActionBar(toolbar)
        toolbar.setBackgroundColor(ThemeColors.getPrimary(this))
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

