package com.example.heolle_beoltteok

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heolle_beoltteok.databinding.RowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MyAdapter (options: FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, MyAdapter.ViewHolder>(options) {


    interface OnItemClickListener {
        fun OnItemClick(holder: MyAdapter.ViewHolder, view: View)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: RowBinding) :
            RecyclerView.ViewHolder(binding.root) {



        init {

            binding.imageButton.setOnClickListener {
                itemClickListener!!.OnItemClick(this, it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: MyData) {


        Glide.with(holder.binding.root.context)
            .load("https://firebasestorage.googleapis.com/v0/b/myteamproject-84c5c.appspot.com/o/images%2Fegg.png?alt=media&token=904c490d-993d-40c3-8f0b-f9a253286cf2")
            .into(holder.binding.imageButton)

        holder.binding.apply {
            name.text = model.pName.toString()
            time.text = model.pTime.toString()
        }
    }
}



