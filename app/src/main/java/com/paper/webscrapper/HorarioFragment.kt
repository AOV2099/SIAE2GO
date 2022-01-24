package com.paper.webscrapper

import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_horario.*

class HorarioFragment : Fragment() {

    var vista : View? = null
    var tabsHorario : TabLayout? = null
    var viewPager : ViewPager? = null
    //var model: FragmentCommunicator? = null
    var fab: FloatingActionButton? = null
    var allowRefresh = true


    override fun onCreate(savedInstanceState: Bundle?) {
        if (allowRefresh == true)
            allowRefresh = false
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onResume() {
        super.onResume()
        if (allowRefresh) {
            allowRefresh = false;
            getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()
        }
        else
            allowRefresh = true
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vista = inflater.inflate(R.layout.fragment_horario, container, false)
        // Inflate the layout for this fragment
        tabsHorario = vista?.findViewById(R.id.tabsHorario)
        viewPager = vista?.findViewById(R.id.viewPagerHorario)
        //var model: FragmentCommunicator? = null
        fab =  vista?.findViewById(R.id.fabHorario)

        setupViewPageAdapter()

        fab?.setOnClickListener {
            startActivity(Intent(context, NewSubject::class.java))
        }

        return vista
    }

    override fun onStop() {
        super.onStop()
        viewPager?.setCurrentItem(0)
    }


    fun setupViewPageAdapter(){

        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("L"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("M"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("M"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("J"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("V"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("S"))
        tabsHorario?.addTab(tabsHorario?.newTab()!!.setText("D"))


        val adapter = ViewPagerAdapter(this.requireContext(), childFragmentManager, tabsHorario?.tabCount!!) //importante que sea el childFrarmentManager
        viewPager?.adapter = adapter

        viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabsHorario))
        //viewPager?.set
        //model = ViewModelProviders.of( requireActivity() ).get(FragmentCommunicator::class.java)

        tabsHorario?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager?.currentItem = tab.position

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

}