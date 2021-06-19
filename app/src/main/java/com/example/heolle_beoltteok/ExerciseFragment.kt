package com.example.heolle_beoltteok

//import com.firebase.ui.database.FirebaseRecyclerOptions
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentExerciseBinding
import java.util.*
import kotlin.concurrent.thread


class ExerciseFragment : Fragment() {
    var binding: FragmentExerciseBinding?=null
//    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: LinearLayoutManager
//    lateinit var adapter: ExerciseAdapter
    var findQuery = false
    var stat = 0

    var total = 0
    var total2 = 0
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
//        rdb = FirebaseDatabase.getInstance().getReference("MyData/items")
//        initData()

    }



    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.data))
        while (scan.hasNextLine()){
            val e_name = scan.nextLine()
            val e_min = scan.nextLine()
            val e_sec = scan.nextLine()
            val r_min = scan.nextLine()
            val r_sec = scan.nextLine()


            val item = ExerciseData(e_name, e_min, e_sec, r_min, r_sec)
//            rdb.child(e_name).setValue(item)

        }
        scan.close()

    }
    fun initFB(){
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        val query = rdb.limitToLast(50)
//        val option = FirebaseRecyclerOptions.Builder<ExerciseData>()
//            .setQuery(query,ExerciseData::class.java)
//            .build()
//        adapter = ExerciseAdapter(option)
//        binding?.recyclerView?.layoutManager = layoutManager
//        binding?.recyclerView?.adapter = adapter
//        adapter.itemClickListener = object : ExerciseAdapter.OnItemClickListener {
//            override fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View) {
//                Toast.makeText(context,"gg",Toast.LENGTH_SHORT).show()
//            }
//
//        }



//        adapter.startListening()
    }

    fun init() {
        total = binding!!.minute.text.toString().toInt() *60 + binding!!.second.text.toString().toInt()
//        adapter.itemClickListener = object : ExerciseAdapter.OnItemClickListener {
//            override fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View) {
//                stat = 0
//                stop()
//                stop2()
//                var exerciseTimeText = holder.binding.ExerciseTime.text.toString()
//                val spltext = exerciseTimeText.split(" ")
//
//                binding!!.minute.text = spltext[0].toString()
//                binding!!.second.text = spltext[2].toString()
//
//                total = binding!!.minute.text.toString()
//                    .toInt() * 60 + binding!!.second.text.toString()
//                    .toInt()
//
//                binding!!.progressBar1.setProgress(100)
//                binding!!.progressBar2.setProgress(100)
//
//                var restTimeText = holder.binding.RestTime.text.toString()
//                val spltext2 = restTimeText.split(" ")
//                binding!!.rminute.text = spltext2[0].toString()
//                binding!!.rsecond.text = spltext2[2].toString()
//
//                total2 = binding!!.rminute.text.toString()
//                    .toInt() * 60 + binding!!.rsecond.text.toString()
//                    .toInt()
//
//            }
//        }

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
        var totaltotal = total.toFloat()
        //sub thread
        thread(start=true) {
            while(true) {
                Thread.sleep(1000)
                if(!started)break
                total = total - 1
                if(stat==0){
                    var percent = (total.toFloat()/totaltotal)*100
                    binding!!.progressBar1.setProgress(percent.toInt())
                    if (percent.toInt()==0){
                        stat = 1
                        started=false
                        start2()
                    }
                }

                activity!!.runOnUiThread {

                    if (stat==0){
                    binding!!.minute.text = String.format("%02d",(total/60)%60)
                    binding!!.second.text = String.format("%02d",(total)%60)
                    }
                    else{
                        binding!!.minute.text = "00"
                        binding!!.second.text = "00"
                    }
                }


            }

        }
        flag = false

    }

    fun start2() {
        started = true
        var totaltotal = total2.toFloat()
        //sub thread
        thread(start = true) {
            while (true) {
                Thread.sleep(1000)
                if (!started) break
                total2 = total2 - 1
                if (stat == 1) {
                    var percent = (total2.toFloat() / totaltotal) * 100
                    binding!!.progressBar2.setProgress(percent.toInt())
                    if (percent.toInt() == 0) {
                        started = false
                    }
                }

                activity!!.runOnUiThread {


                    binding!!.rminute.text = String.format("%02d", (total2 / 60) % 60)
                    binding!!.rsecond.text = String.format("%02d", (total2) % 60)
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
        flag = true

    }
    fun stop2() {
        started = false
        total2 = 0
        binding!!.rminute.text = "00"
        binding!!.rsecond.text = "00"
        flag = true

    }


}