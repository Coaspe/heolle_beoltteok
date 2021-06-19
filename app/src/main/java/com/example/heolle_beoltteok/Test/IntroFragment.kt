package com.example.heolle_beoltteok.Test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.heolle_beoltteok.R
import com.example.heolle_beoltteok.databinding.FragmentIntroBinding

import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class IntroFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    lateinit var rdb: DatabaseReference

    val scope = CoroutineScope(Dispatchers.IO)
    var binding : FragmentIntroBinding?=null




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentIntroBinding.inflate(layoutInflater, container, false)
        init()
        return binding!!.root
    }





    private fun init() {
        rdb = FirebaseDatabase.getInstance().getReference("datas/items")



        binding!!.button.setOnClickListener {
            val fragment = activity!!.supportFragmentManager.beginTransaction()
            //fragment.addToBackStack(null)
            fragment.replace(R.id.frameLayout, TestFragment())
            fragment.commit()
        }

            if (viewModel.getValue() == "Exam") {
                val url = "https://superkts.com/cal/su_day/2022"
                scope.launch {
                    try {
                        val doc = Jsoup.connect(url).get()
                        withContext(Dispatchers.Main) {
                            val d_day = doc.getElementsByAttributeValue("class", "result")
                            val testName = doc.getElementsByAttributeValue("class","intro")
                            binding!!.textView2.text = testName.text().substring(0,10)

                            binding!!.textView3.text = d_day.text().substring(0, 4)
                            Log.e("d_day", d_day.text())
                        }

                    } catch (e: Exception) {
                    }
                }


            }
            if (viewModel.getValue() == "TOEIC") {
                val url = "https://exam.toeic.co.kr/receipt/examSchList.php"
                scope.launch {
                    try {
                        val doc = Jsoup.connect(url).get()
                        withContext(Dispatchers.Main) {
                            val d_day = doc.getElementsByAttributeValue("class", "ing")

                            binding!!.textView2.text = d_day.text().substring(0,5)
                      binding!!.textView3.text = d_day.text().substring(6, 16)
                            Log.e("d_day", d_day.text())
                        }

                    } catch (e: Exception) {
                    }
                }
            }
        else {
                binding!!.textView2.text = viewModel.getValue()
                rdb.child(viewModel.getValue()).child("date")
                    .addListenerForSingleValueEvent(object  : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            binding!!.textView3.text = snapshot.value.toString()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.e("cancel", "실패")
                        }

            })
        }



    }
}
