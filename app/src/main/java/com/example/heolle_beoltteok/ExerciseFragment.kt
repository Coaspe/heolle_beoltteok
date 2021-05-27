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


class ExerciseFragment : Fragment() {
    var binding: FragmentExerciseBinding?=null
    lateinit var rdb: DatabaseReference
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: ExerciseAdapter
    var findQuery = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
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
    fun init(){
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


}