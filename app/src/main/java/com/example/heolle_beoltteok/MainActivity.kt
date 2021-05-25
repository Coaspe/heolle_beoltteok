package com.example.heolle_beoltteok

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
<<<<<<< HEAD
=======
import com.google.firebase.database.FirebaseDatabase

>>>>>>> origin/Yoon
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var rdb : FirebaseDatabase

    private var time = 0
    private var timerTask: Timer? = null      // null을 허용
    private var isRunning = false
    private var lap = 1


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        init()
    }

private fun init() {
        val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        supportFragmentManager.addOnBackStackChangedListener {
            var fg = supportFragmentManager.findFragmentById(R.id.frameLayout)
            var fg_str = fg.toString().split("{")[0]
            if (fg_str == "ExerciseFragment"){
                bottomNavBar.menu.getItem(1).isChecked = true
            } else if (fg_str == "CookFragment"){
                bottomNavBar.menu.getItem(2).isChecked = true
            } else if (fg_str == "TestFragment"){
                bottomNavBar.menu.getItem(3).isChecked = true
            }

        }

        replaceFragment(HomeFragment())

        bottomNavBar.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_page -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.exercise_page -> {
                    replaceFragment(ExerciseFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.cooking_page -> {
                    replaceFragment(CookFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.test_page -> {
                    replaceFragment(TestFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.setting_page -> {
                    replaceFragment(SettingFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()

        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    }
}
