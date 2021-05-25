package com.example.heolle_beoltteok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.heolle_beoltteok.databinding.RowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class MyAdapter (options: FirebaseRecyclerOptions<MyData>)
    : FirebaseRecyclerAdapter<MyData, MyAdapter.ViewHolder>(options) {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View)
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
        holder.binding.apply {
            imageButton.setImageResource(R.drawable.common_google_signin_btn_icon_dark)
            name.text = model.pName.toString()
            time.text = model.pTime.toString()
        }
    }
}



