package com.example.heolle_beoltteok

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentUserSettingBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import java.util.HashMap


class User_SettingFragment : Fragment() {
    var binding: FragmentUserSettingBinding? = null
    val firestore = FirebaseFirestore.getInstance()
    lateinit var rdb: DatabaseReference
    lateinit var rdb2: DatabaseReference

    lateinit var itemTitle:String
    lateinit var newItem: HashMap<String, String>
    var newItemArray : ArrayList<UserItemInfo> = ArrayList()

    lateinit var dialogView:View
    lateinit var adapter: UserSettingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserSettingBinding.inflate(layoutInflater, container, false)
        dialogView = inflater.inflate(R.layout.add_dialog, container, false)

        init()

        return binding!!.root


    }



    private fun init() {
        rdb = FirebaseDatabase.getInstance().getReference("userFrag/items")
        rdb2 = FirebaseDatabase.getInstance().getReference("userFrag/itemname")


        binding!!.recyclerView.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)
        adapter = UserSettingAdapter(TestInfo_ArrayList)
        binding!!.recyclerView.adapter = adapter


        binding!!.addBtn2.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context)
                .setView(dialogView)
                .setCancelable(false)
                .setTitle("타이머 추가")
            val mAlertDialog = mBuilder.show()
            val okButton = dialogView.findViewById<Button>(R.id.addDialogAddButton)
            val noButton = dialogView.findViewById<Button>(R.id.addDialogCancleButton)
            okButton.setOnClickListener {
                val testName = dialogView.findViewById<EditText>(R.id.addDialogCookingName).text.toString()
                val testTime= dialogView.findViewById<EditText>(R.id.addDialogCookingTime).text.toString()

                val test = UserItemInfo(testName,testTime,String.format("%02d", testTime.toInt()/60),String.format("%02d", testTime.toInt()%60),"00")
                TestInfo_ArrayList.add(test)
                adapter.notifyDataSetChanged()

                val itemTitle = binding!!.addText.text.toString()
                rdb.child(itemTitle).child(testName).setValue(test)
                var tempitem = UserMenuTitle(itemTitle)
                rdb2.child(itemTitle).setValue(tempitem)

            }
            noButton.setOnClickListener {

                mAlertDialog.dismiss()
                //(dialogView.parent as ViewGroup).removeView(dialogView)
            }
        }

        binding!!.button3.setOnClickListener {
            val fragment = activity?.supportFragmentManager?.beginTransaction()
            //fragment.addToBackStack(null)
            fragment?.replace(R.id.frameLayout, UserMenuFragment())
            fragment?.commit()
        }


    }


}