package com.example.heolle_beoltteok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase


import kotlin.concurrent.thread


class CookFragment : Fragment() {
    lateinit var rdb: FirebaseDatabase
    lateinit var adapter:MyAdapter


    var binding:FragmentCookBinding?=null
    var total = 0
    var started = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentCookBinding.inflate(layoutInflater,container,false)
        init()
        return binding!!.root
    }
    fun start() {
        started = true
        //sub thread
        thread(start=true) {
            while(true) {
                Thread.sleep(1)
                if(!started)break
                total = total - 1
                activity!!.runOnUiThread {

                    binding!!.minute.text = String.format("%02d",(total/3600)%60)
                    binding!!.second.text = String.format("%02d",(total/60)%60)
                    binding!!.milli.text = String.format("%02d",total%60)
                }

            }

        }

    }



    fun pause() {
        started = false

    }
    fun stop() {
        started = false
        total = 0
        binding!!.minute.text = "00"
        binding!!.second.text = "00"
        binding!!.milli.text = "00"

    }
    fun init() {
        rdb = FirebaseDatabase.getInstance()
        val rdbref = rdb.getReference("MyDatas/items")



        val query = rdbref.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
            .setQuery(query, MyData::class.java)
            .build()

        adapter = MyAdapter(option)

        binding!!.recyclerView.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding!!.recyclerView.adapter = adapter
        adapter.startListening()

        total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()

        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {
            override fun OnItemClick(holder: MyAdapter.ViewHolder, view: View) {
                binding!!.minute.text = String.format("%02d",holder.binding.time.text.toString().toInt())
                total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()
            }
        }
        binding!!.startBtn.setOnClickListener {

            start()
        }
        binding!!.pasueBtn.setOnClickListener {
            pause()
        }
        binding!!.stopBtn.setOnClickListener {
            stop()
        }
    }
//    fun formatTime(time:Int) :String {
//        val milisecond = String.format("%02d",time%60)
//        val second = String.format("%02d",(time/60)%60)
//        val minute = String.format("%02d",(time/3600)%60)
//
//        return "$minute : $second : $milisecond"
//    }

}