package com.example.heolle_beoltteok

import android.os.Bundle
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
=======
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.heolle_beoltteok.databinding.ActivityMainBinding

import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding


    private var time = 0
    private var timerTask: Timer? = null      // null을 허용
    private var isRunning = false
    private var lap = 1
>>>>>>> db8c2d1839abd5e3e2f5d8a69cff2b01e009b903

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        setContentView(R.layout.activity_main)
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
=======
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.playFab.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) { start() } else { pause() }
        }

        binding.labButton.setOnClickListener {
            recordLapTime()
        }

        binding.resetFab.setOnClickListener {
            reset()
        }
    }

    private fun start() {
       // binding.playFab.setImageResource(R.drawable.ic_pause_black_24dp)   // 일시정지 이미지

        timerTask = timer(period=10) {       // 타이머 인터벌 10 ms
            time++

            val sec = time / 100
            val milli = time % 100

            runOnUiThread {                   // UI 조작이 가능한 블럭
                binding.secTextView.text = "$sec"
                binding.milliTextView.text = "$milli"
>>>>>>> db8c2d1839abd5e3e2f5d8a69cff2b01e009b903
            }
        }
    }

<<<<<<< HEAD
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    }
<<<<<<< HEAD
    fun init() {}
=======
>>>>>>> b260e700de8fdc4125995d1d525102c6b8d0c941
=======
    private fun pause() {
       // playFab.setImageResource(R.drawable.ic_play_arrow_black_24dp)   // 시작 이미지

        timerTask?.cancel()                  // 실행중인 타이머 취소
    }

    private fun recordLapTime() {
        if (!isRunning) return         // 타이머가 실행 중이 아니라면 리턴
        val lapTime = this.time
        val textView = TextView(this)      // 동적으로 TextView 생성
        textView.text = "$lap LAB : ${lapTime / 100}. ${lapTime % 100}"

        binding.lapLayout.addView(textView, 0)     // 0 : 맨 위쪽에 추가
        lap++
    }

    private fun reset() {
        timerTask?.cancel()       // 실행중인 타이머 취소

        // 모든 변수 초기화
        time = 0
        isRunning = false
        //playFab.setImageResource(R.drawable.ic_play_arrow_black_24dp)
        binding.secTextView.text = "0"
        binding.milliTextView.text = "0"

        // 모든 랩타임 기록 삭제
        binding.lapLayout.removeAllViews()
        lap = 1
    }

>>>>>>> db8c2d1839abd5e3e2f5d8a69cff2b01e009b903
}