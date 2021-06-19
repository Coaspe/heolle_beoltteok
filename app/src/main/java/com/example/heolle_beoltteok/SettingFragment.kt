package com.example.heolle_beoltteok

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SettingFragment : Fragment() {


    lateinit var rdb: DatabaseReference
    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val dialogView = inflater.inflate(R.layout.add_dialog, container, false)
        val exerciseView = inflater.inflate(R.layout.add_exercise_list, container, false)
        val deleteExerciseView = inflater.inflate(R.layout.delete_exercise_list, container, false)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        addCooking(view, dialogView, context as Context)
        addExercise(view, exerciseView, context as Context)
        deleteExercise(view, deleteExerciseView, context as Context)

        return view
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
    //======================================================================//
    fun addExercise(view: View, exerciseView: View, context: Context) {
        val addExerciseBtn = view.findViewById<Button>(R.id.addExerciseTimer)
        rdb = FirebaseDatabase.getInstance().getReference("MyData/items")
        addExerciseBtn.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context)
                .setView(exerciseView)
                .setCancelable(false)
                .setTitle("운동 추가")

            val mAlertDialog = mBuilder.show()

            val okButton = exerciseView.findViewById<Button>(R.id.addExAddButton)
            val noButton = exerciseView.findViewById<Button>(R.id.addExCancelButton)
            val ExName = exerciseView.findViewById<EditText>(R.id.addExerciseName)
            val ExTime = exerciseView.findViewById<EditText>(R.id.addExerciseTime)
            val RelaxTime = exerciseView.findViewById<EditText>(R.id.addRelaxingTime)

            okButton.setOnClickListener {
                if (ExName.text.toString().length == 0 ||
                    ExTime.text.toString().length == 0 ||
                    RelaxTime.text.toString().length == 0) {
                }
                else {
                    if (chkNum(ExTime.text.toString()) == false && chkNum(ExTime.text.toString()) == false) {
                        Toast.makeText(context, "잘못된 입력값 입니다 다시 입력하세요", Toast.LENGTH_SHORT).show()

                        ExName.text.clear()
                        ExTime.text.clear()
                        RelaxTime.text.clear()
                    }
                    else {
                        rdb.child(ExName.text.toString()).setValue(
                            ExerciseData(
                                ExName.text.toString(),
                                (ExTime.text.toString().toInt() / 60).toString(),
                                (ExTime.text.toString().toInt() % 60).toString(),
                                (RelaxTime.text.toString().toInt() / 60).toString(),
                                (RelaxTime.text.toString().toInt() % 60).toString()
                            )
                        )

                        Toast.makeText(
                            context,
                            "${ExName.text.toString()}항목 추가",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        ExName.text.clear()
                        ExTime.text.clear()
                        RelaxTime.text.clear()
                        mAlertDialog.dismiss()
                        (exerciseView.parent as ViewGroup).removeView(exerciseView)
                    }
                }
            }
            noButton.setOnClickListener {
                ExName.text.clear()
                ExTime.text.clear()
                RelaxTime.text.clear()
                mAlertDialog.dismiss()
                (exerciseView.parent as ViewGroup).removeView(exerciseView)
            }

        }
    }

    fun deleteExercise(view: View, deleteExerciseView: View, context: Context){
        rdb = FirebaseDatabase.getInstance().getReference("MyData/items")
        val deleteExTimer = view.findViewById<Button>(R.id.deleteExerciseTimer)

        deleteExTimer.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context)
                .setView(deleteExerciseView)
                .setCancelable(false)
                .setTitle("운동 삭제")

            val mAlertDialog = mBuilder.show()
            val deleteButton = deleteExerciseView.findViewById<Button>(R.id.deleteExDeleteButton)
            val noButton = deleteExerciseView.findViewById<Button>(R.id.deleteExCancelButton)
            val ExName = deleteExerciseView.findViewById<EditText>(R.id.deleteExerciseName)

            deleteButton.setOnClickListener {
                if (ExName.text.toString().length == 0) {

                }
                else {
                    rdb.child(ExName.text.toString()).get().addOnSuccessListener {
                        if (it.value != null) {
                            rdb.child(it.key.toString()).removeValue()
                            Toast.makeText(context, "${it.key} 항목 delete", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "검색결과가 존재하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                    ExName.text.clear()
                    mAlertDialog.dismiss()
                    (deleteExerciseView.parent as ViewGroup).removeView(deleteExerciseView)
                }
            }
            noButton.setOnClickListener {
                ExName.text.clear()
                mAlertDialog.dismiss()
                (deleteExerciseView.parent as ViewGroup).removeView(deleteExerciseView)
            }
        }
    }
    fun chkNum(str: String) : Boolean {
        var temp: Char
        var result = true
        for (i in 0 until str.length) {
            temp = str.elementAt(i)
            if (temp.toInt() < 48 || temp.toInt() > 57)
                result = false
        }
        return result
    }

//==========================================================================//
    fun addCooking(view: View, dialogView: View, context: Context) {
        val addTimerBtn = view.findViewById<Button>(R.id.addTimer)

        addTimerBtn.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setCancelable(false)
                    .setTitle("단어 추가")
            val mAlertDialog = mBuilder.show()
            val okButton = dialogView.findViewById<Button>(R.id.addDialogAddButton)
            val noButton = dialogView.findViewById<Button>(R.id.addDialogCancleButton)
            val addImageLayout = dialogView.findViewById<LinearLayout>(R.id.addImageLayout)
            addImageLayout.setOnClickListener {
                launchGallery()
            }

            okButton.setOnClickListener {
                editTextClear(dialogView)
                GlobalScope.launch {
                    uploadImage(dialogView)
                }
                mAlertDialog.dismiss()
                (dialogView.parent as ViewGroup).removeView(dialogView)
            }
            noButton.setOnClickListener {
                editTextClear(dialogView)
                mAlertDialog.dismiss()
                (dialogView.parent as ViewGroup).removeView(dialogView)
            }
        }

    }
    fun editTextClear(dialogView: View) {
        dialogView.findViewById<EditText>(R.id.addDialogCookingName).text.clear()
        dialogView.findViewById<EditText>(R.id.addDialogCookingTime).text.clear()
    }
    // 갤러리 오픈
    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }
    // 받아온 갤러리 사진 fireStorage에 올리고 access token 받아와서 firestore에 올림
    private fun uploadImage(dialogView : View) {
        val db = FirebaseFirestore.getInstance()
        var CookingName: String = ""
        var CookingImage: String = ""
        var CookingTime: String = ""

        if (filePath != null) {
            val na = UUID.randomUUID().toString()
            val ref = storageReference?.child("uploads/" + na)
            runBlocking {
                val uploadTask = ref?.putFile(filePath!!)?.addOnSuccessListener {
                    val url = storageReference?.child("uploads/" + na)?.downloadUrl?.addOnSuccessListener {
                        CookingName = dialogView.findViewById<EditText>(R.id.addDialogCookingName).text.toString()
                        CookingTime = dialogView.findViewById<EditText>(R.id.addDialogCookingTime).text.toString()
                        CookingImage = it.toString()

                        var newCook = hashMapOf(
                                "cookingImg" to CookingImage,
                                "cookingName" to CookingName,
                                "cookingTime" to CookingTime
                        )
                        db.collection("Cooking_Info")
                                .document(CookingName)
                                .set(newCook)
                    }
                }
            }
        }
    }
}