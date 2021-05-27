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
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class SettingFragment : Fragment() {

    private var filePath: Uri? = null
    private val PICK_IMAGE_REQUEST = 71
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val dialogView = inflater.inflate(R.layout.add_dialog, container, false)
        firebaseStore = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        addCooking(view, dialogView, context as Context)

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