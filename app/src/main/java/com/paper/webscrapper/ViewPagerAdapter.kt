package com.paper.webscrapper

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


@Suppress("DEPRECATION")
class ViewPagerAdapter( var context: Context, fm: FragmentManager, var totalTabs: Int ) :
    FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {


        return when (position) {
            0 -> {
                DaySelectedMonday()
            }

            1 -> {
                DaySelectedTuesday()
            }

            2 -> {
                DaySelectedWednesday()
            }

            3 -> {
                DaySelectedThursday()
            }

            4 -> {
                DaySelectedFriday()
            }

            5 -> {
                DaySelectedSaturday()
            }

            6 -> {
                DaySelectedSunday()
            }

            else -> {
                DaySelectedMonday()
            }
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}