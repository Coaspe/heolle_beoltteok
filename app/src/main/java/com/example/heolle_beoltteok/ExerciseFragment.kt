package com.example.heolle_beoltteok

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.example.heolle_beoltteok.databinding.FragmentExerciseBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.concurrent.thread


class ExerciseFragment : Fragment() {
    var binding: FragmentExerciseBinding?=null
    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ExerciseAdapter
    var findQuery = false

    var total = 0
    var started = false
    var flag = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        initFB()
        init()
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rdb = FirebaseDatabase.getInstance().getReference("MyData/items")
//        initData()

    }
    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.data))
        while (scan.hasNextLine()){
            val name = scan.nextLine()
            val time = scan.nextLine()
            val item = ExerciseData(name, time)
            rdb.child(item.toString()).setValue(item)

        }
        scan.close()

    }
    fun initFB(){
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val query = rdb.limitToLast(50)
        val option = FirebaseRecyclerOptions.Builder<ExerciseData>()
            .setQuery(query,ExerciseData::class.java)
            .build()
        adapter = ExerciseAdapter(option)
        binding?.recyclerView?.layoutManager = layoutManager
        binding?.recyclerView?.adapter = adapter
        adapter.itemClickListener = object : ExerciseAdapter.OnItemClickListener {
            override fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View) {
                Toast.makeText(context,"gg",Toast.LENGTH_SHORT).show()
            }

        }



        adapter.startListening()
    }

    fun init() {
        total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()
        adapter.itemClickListener = object : ExerciseAdapter.OnItemClickListener {



            override fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View) {
                stop()
                var exerciseTimeText = holder.binding.ExerciseTime.text
                val time = exerciseTimeText
                binding!!.minute.text = time
                total = binding!!.minute.text.toString()
                    .toInt() * 3600 + binding!!.second.text.toString()
                    .toInt() * 60 + binding!!.milli.text.toString().toInt()

            }
        }

        binding!!.startBtn.setOnClickListener {
            if (flag == true) {
                start()
            }
        }

        binding!!.pasueBtn.setOnClickListener {
            pause()
        }

        binding!!.stopBtn.setOnClickListener {
            stop()
        }



    }

    fun start() {
        started = true
        //sub thread
        thread(start=true) {
            while(true) {
                Thread.sleep(1000)
                if(!started)break
                total = total - 1
                activity!!.runOnUiThread {


                    binding!!.minute.text = String.format("%02d",(total/3600)%60)
                    binding!!.second.text = String.format("%02d",(total/60)%60)
                    binding!!.milli.text = String.format("%02d",total%60)
                }

            }

        }


        flag = false

    }
    fun pause() {
        started = false
        flag = true

    }
    fun stop() {
        started = false
        total = 0
        binding!!.minute.text = "00"
        binding!!.second.text = "00"
        binding!!.milli.text = "00"
        flag = true

    }


}