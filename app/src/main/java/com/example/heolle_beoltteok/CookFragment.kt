package com.example.heolle_beoltteok

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text
import kotlin.concurrent.thread


class CookFragment : Fragment() {
    var binding:FragmentCookBinding?=null
    var total = 0
    var started = false
    var CookInfo_ArrayList : ArrayList<CookInfo> = ArrayList()
    var flag = true

    lateinit var adapter: CookRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCookBinding.inflate(layoutInflater,container,false)
        //val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        firebaseDatainit()
        initRecyclerView(binding!!.recyclerView)
        init()
        return binding!!.root
    }
    fun init() {
        adapter.itemClickListener = object : CookRecyclerViewAdapter.OnItemClickListener {



            override fun OnItemClick(holder: CookRecyclerViewAdapter.ViewHolder, view: View) {

                var cookTimeText = holder.binding.CookingTime.text.toString()
                val time = cookTimeText.substring(0, cookTimeText.length - 1)
                binding!!.minute.text = String.format("%02d", time)
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


    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = GridLayoutManager(context,2, GridLayoutManager.HORIZONTAL, false)
        adapter = CookRecyclerViewAdapter(CookInfo_ArrayList)
        recyclerView.adapter = adapter
    }

    // firestore에서 데이터를 읽어오는 함수
     fun firebaseDatainit() {
        val firestore = FirebaseFirestore.getInstance()
        try {
            // collection(Cooking_Info) > documentation(요리 이름) > field(CookingImage,CookingName,CookingTime)
            firestore.collection("Cooking_Info")
                // Cooking_Info에 해당하는 collection의 모든 documentation을 불러온 것임 그게 result로 들어감
                .get()
                // result로 불러온 값을 CookInfo object로 변형 그렇게하면 정의해둔 data class CookInfo와 같은 형태로 사용 가능
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        CookInfo_ArrayList.add(doc.toObject(CookInfo::class.java))
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("RMx" , CookInfo_ArrayList[0].cookingName)
                }.addOnFailureListener {
                    Log.d("fail", it.message.toString())
                }
        } catch (e: Exception){
            Log.d("Exception", e.message.toString())
        }
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
}


