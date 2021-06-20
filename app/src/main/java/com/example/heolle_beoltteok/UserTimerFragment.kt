package com.example.heolle_beoltteok

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.heolle_beoltteok.databinding.FragmentUserIntroBinding
import com.example.heolle_beoltteok.databinding.FragmentUserTimerBinding
import com.example.heolle_beoltteok.databinding.UsertimerrowBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.concurrent.thread

class UserTimerFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var rdb: DatabaseReference

    lateinit var layoutManager: LinearLayoutManager

    var binding: FragmentUserTimerBinding? = null
    lateinit var adapter: UserTimerAdapter

    var flag: Boolean = true
    var total = 0
    var started = true


    var totaltime:String = ""

    var binding2: FragmentUserIntroBinding?=null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentUserTimerBinding.inflate(layoutInflater, container, false)

        firebaseDatainit()

        return binding!!.root
    }


    private fun firebaseDatainit() {
        var path = "userFrag/items/"+viewModel.getValue()
        rdb = FirebaseDatabase.getInstance().getReference(path)

        val query = rdb.limitToLast(100000)
        val option = FirebaseRecyclerOptions.Builder<UserItemInfo>()
            .setQuery(query, UserItemInfo::class.java)
            .build()
        adapter = UserTimerAdapter(option)
        binding!!.viewpager.adapter = adapter

        rdb.child("hour").get().addOnSuccessListener{
            if(it.value != null){
                binding!!.hour.text = it.value
            }
        }


//        adapter.itemClickListener = object : UserTimerAdapter.OnItemClickListener{
//            override fun OnItemClick(holder: UserTimerAdapter.ViewHolder, view: View) {
//                var timeText = holder.binding.itemTime.text.toString()
//                Toast.makeText(context, timeText, Toast.LENGTH_SHORT).show()
//                binding!!.hour.text = String.format("%02d", timeText.toInt()/60)
//                binding!!.minute.text = String.format("%02d", timeText.toInt()%60)
//
//
//            }
//
//        }



//        rdb.child("hour").get()

//        binding!!.hour.text = rdb.get("hour")
//        binding!!.minute.text = rdb.child("minute").get().toString()
//        binding!!.sec.text = "00"




        adapter.startListening()


    }

//    fun start() {
//        flag = false
//        started = true
//
//
//
//        //sub thread
//
//        thread(start = true) {
//            var flag2 = true
//            while (true) {
//
//                Thread.sleep(1000)
//                if(flag2 == false)  break
//                if (!started) break
//                total = total - 1
//                activity!!.runOnUiThread {
//                    if (binding!!.hour.text == "00" && binding!!.minute.text == "00" && binding!!.sec.text == "00") {
//                        flag2 = false
//                        var current = binding!!.viewpager.currentItem
//                        binding!!.viewpager.setCurrentItem(current+1, false)
//                        Log.e("scroll", "scroll")
//                        Log.e("current",TestInfo_ArrayList.size.toString())
//                        started=false
//                        flag = true
//
//                    }
//                    val current = binding!!.viewpager.currentItem
//                    if(current == TestInfo_ArrayList.size-1 && binding!!.hour.text == "00" && binding!!.minute.text == "00" && binding!!.sec.text == "00")
//                    {
//                        Toast.makeText(context,"pz", Toast.LENGTH_SHORT).show()
//                    }
//
//
//
//                    binding!!.hour.text = String.format("%02d", (total / 3600) % 60)
//                    binding!!.minute.text = String.format("%02d", (total / 60) % 60)
//                    binding!!.sec.text = String.format("%02d", total % 60)
//                }
//
//            }
//        }
//
//    }
//
//
//    fun pause() {
//        started = false
//        flag = true
//    }
//
//    fun stop() {
//        started = false
//        total = 0
//        binding!!.hour.text = "00"
//        binding!!.minute.text = "00"
//        binding!!.sec.text = "00"
//        flag = true
//    }
//
//    fun init() {
//
//
//        total = binding!!.hour.text.toString().toInt() * 3600 + binding!!.minute.text.toString().toInt() * 60 + binding!!.sec.text.toString().toInt()
//        adapter.itemClickListener = object : TestRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(
//                holder: TestRecyclerViewAdapter.ViewHolder,
//                view: View,
//                position: Int,
//                hour: String,
//                minute: String,
//                sec: String
//            ) {
//                binding!!.hour.text = hour
//                binding!!.minute.text = minute
//                binding!!.sec.text = sec
//                total = hour.toInt() * 3600 + minute.toInt() * 60 + sec.toInt()
//                //holder.binding.nextButton.visibility = View.VISIBLE
//            }
//
//        }
//        adapter.itemClickListener2 = object :TestRecyclerViewAdapter.OnItemClickListener {
//            override fun OnItemClick(
//                    holder: TestRecyclerViewAdapter.ViewHolder,
//                    view: View,
//                    position: Int,
//                    hour: String,
//                    minute: String,
//                    sec: String
//            ) {
//                var current = binding!!.viewpager.currentItem
//                binding!!.viewpager.setCurrentItem(current+1, false)
//
//
//            }
//        }
//
//        binding!!.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//
//                var current = binding!!.viewpager.currentItem
//
//                binding!!.hour.text = adapter.items[current].hour
//                binding!!.minute.text = adapter.items[current].minute
//                binding!!.sec.text = adapter.items[current].sec
//
//                total = binding!!.hour.text.toString().toInt() * 3600 + binding!!.minute.text.toString().toInt() * 60 + binding!!.sec.text.toString().toInt()
//                if(position!=0) {
//                    binding!!.startBtn.performClick()
//
//                }
//
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//            }
//
//
//        }
//
//        binding!!.startBtn.setOnClickListener {
//            if (flag == true) {
//                start()
//            }
//        }
//
//        binding!!.pasueBtn.setOnClickListener {
//
//            pause()
//
//        }
//        binding!!.stopBtn.setOnClickListener {
//            stop()
//        }
//    }



}