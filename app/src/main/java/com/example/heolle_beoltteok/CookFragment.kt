package com.example.heolle_beoltteok

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heolle_beoltteok.databinding.AddDialogBinding
import com.example.heolle_beoltteok.databinding.FragmentCookBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread


class CookFragment : Fragment() {
    var binding:FragmentCookBinding?=null
    var total = 0
    var started = false
    var CookInfo_ArrayList : ArrayList<CookInfo> = ArrayList()
    var flag = true
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    lateinit var adapter: CookRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCookBinding.inflate(layoutInflater,container,false)
        val dialogBinding = AddDialogBinding.inflate(layoutInflater, container,false)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        firebaseDatainit(binding!!.root)
        initRecyclerView(binding!!.recyclerView)
        init()
        addCooking(binding!!.root,context as Context, adapter, dialogBinding, binding!!)
        return binding!!.root
    }
    fun init() {
        total = binding!!.minute.text.toString().toInt() *3600 + binding!!.second.text.toString().toInt()*60 + binding!!.milli.text.toString().toInt()
        adapter.itemClickListener = object : CookRecyclerViewAdapter.OnItemClickListener {



            override fun OnItemClick(holder: CookRecyclerViewAdapter.ViewHolder, view: View) {

                var cookTimeText = holder.binding.CookingTime.text.toString()
                val time = cookTimeText.substring(0, cookTimeText.length - 1)
                binding!!.minute.text = String.format("%02d", time.toInt())
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
     fun firebaseDatainit(view: View) {
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
                    val btn = view.findViewById<Button>(R.id.addTimer2)
                    btn.isEnabled = true
                    //Log.d("RMx" , CookInfo_ArrayList[0].cookingName)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }
            filePath = data.data
        }
    }

    fun addCooking(view: View,context: Context, adapter: CookRecyclerViewAdapter, dialogBinding: AddDialogBinding, cookBinding: FragmentCookBinding) {
        val addTimerBtn = view.findViewById<Button>(R.id.addTimer2)

        addTimerBtn.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context)
                    .setView(dialogBinding.root)
                    .setCancelable(false)
                    .setTitle("단어 추가")
            val mAlertDialog = mBuilder.show()
            val okButton = dialogBinding.addDialogAddButton
            val noButton = dialogBinding.addDialogCancleButton
            val addImageLayout = dialogBinding.addImageLayout
            addImageLayout.setOnClickListener {
                launchGallery()
            }

            okButton.setOnClickListener {
                GlobalScope.launch {
                    uploadImage(dialogBinding, adapter, cookBinding)
                }
                mAlertDialog.dismiss()
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
                (dialogBinding.root.parent as ViewGroup).removeView(dialogBinding.root)
            }
        }

    }
    // 갤러리 오픈
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    // 받아온 갤러리 사진 fireStorage에 올리고 access token 받아와서 firestore에 올림
    private fun uploadImage(dialogBinding: AddDialogBinding, adapter: CookRecyclerViewAdapter,cookBinding: FragmentCookBinding) {
        val db = FirebaseFirestore.getInstance()
        var CookingName: String = ""
        var CookingImage: String = ""
        var CookingTime: String = ""

        if (filePath != null) {
            val na = UUID.randomUUID().toString()
            val ref = storageReference?.child("uploads/" + na)
                val uploadTask = ref?.putFile(filePath!!)?.addOnSuccessListener {

                    storageReference?.child("uploads/" + na)?.downloadUrl?.addOnSuccessListener {
                        Log.d("cookingName", dialogBinding.addDialogCookingName.text.toString())
                        Log.d("cookingTime", dialogBinding.addDialogCookingTime.text.toString())
                        Log.d("cookingImage", it.toString())
                        CookingName = dialogBinding.addDialogCookingName.text.toString()
                        CookingTime = dialogBinding.addDialogCookingTime.text.toString()
                        CookingImage = it.toString()

                        var newCook = hashMapOf(
                                "cookingImg" to CookingImage,
                                "cookingName" to CookingName,
                                "cookingTime" to CookingTime
                        )
                        db.collection("Cooking_Info")
                                .document(CookingName)
                                .set(newCook)
                                .addOnSuccessListener {
                                    CookInfo_ArrayList.add(CookInfo(CookingName,CookingTime,CookingImage))
                                    adapter.notifyDataSetChanged()
                                }
                    }?.addOnFailureListener {
                        Log.d("acees token", "acees token get fail")
                }
            }
        }
    }
}


