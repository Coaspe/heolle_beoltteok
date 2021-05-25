package com.example.heolle_beoltteok

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
<<<<<<< HEAD
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

=======
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.net.URL
>>>>>>> origin/Yoon

import kotlin.concurrent.thread


class CookFragment : Fragment() {
<<<<<<< HEAD
    lateinit var rdb: FirebaseDatabase
    lateinit var adapter:MyAdapter

=======
    val storage = Firebase.storage
    lateinit var rdb:FirebaseDatabase
    lateinit var adapter:MyAdapter
    var flag:Boolean = true
>>>>>>> origin/Yoon

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
                Thread.sleep(10)
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
    fun init() {
<<<<<<< HEAD
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
=======





        rdb = FirebaseDatabase.getInstance()
        val rdbref = rdb.getReference("MyDatas/items")

        val query = rdbref.orderByKey()
        val option = FirebaseRecyclerOptions.Builder<MyData>()
                .setQuery(query, MyData::class.java)
                .build()

        adapter = MyAdapter(option)

        binding!!.recyclerView.layoutManager = GridLayoutManager(context,3)
        binding!!.recyclerView.adapter = adapter
        adapter.startListening()

        val storage = Firebase.storage
        var  storageRef = storage.reference
        var imageRef: StorageReference? = storageRef.child("images")

        var image = storageRef.child("images/egg.png")

        image.downloadUrl.addOnSuccessListener(OnSuccessListener {
            Log.e("success",it.toString())
            rdb.getReference("MyDatas/items/1").child("image").setValue(it.toString())
        })

//        rdb.getReference("MyDatas/items/1").child("image").list
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        val value = snapshot?.value
//
//                        Glide.with(context)
//                                .load("https://firebasestorage.googleapis.com/v0/b/myt...")
//                                .into(binding!!.imageView2)
//
//                    }
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.e("data", "data")
//                    }
//                })


        total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()
>>>>>>> origin/Yoon

        adapter.itemClickListener = object :MyAdapter.OnItemClickListener {

            override fun OnItemClick(holder: MyAdapter.ViewHolder, view: View) {
                binding!!.minute.text = String.format("%02d",holder.binding.time.text.toString().toInt())
                total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()
            }


        }

            binding!!.startBtn.setOnClickListener {
                if(flag==true) {
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
//    fun formatTime(time:Int) :String {
//        val milisecond = String.format("%02d",time%60)
//        val second = String.format("%02d",(time/60)%60)
//        val minute = String.format("%02d",(time/3600)%60)
//
//        return "$minute : $second : $milisecond"
//    }

}