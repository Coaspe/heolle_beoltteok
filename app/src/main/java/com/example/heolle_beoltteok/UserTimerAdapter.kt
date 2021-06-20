package com.example.heolle_beoltteok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heolle_beoltteok.databinding.UsertimerrowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class UserTimerAdapter(options: FirebaseRecyclerOptions<UserItemInfo>)
    : FirebaseRecyclerAdapter<UserItemInfo, UserTimerAdapter.ViewHolder>(options) {



    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, view: View)
    }
    var itemClickListener:OnItemClickListener?=null

//    var itemClickListener2:OnItemClickListener2?=null


    inner class ViewHolder(val binding: UsertimerrowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(this, it)

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = UsertimerrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: UserItemInfo) {
        holder.binding.apply {
            itemName.text = model.itemName.toString()
            itemTime.text = model.itemTime.toString()

        }
    }

}