package com.example.heolle_beoltteok

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heolle_beoltteok.databinding.CookingrowBinding

class CookRecyclerViewAdapter(val items:ArrayList<CookInfo>) : RecyclerView.Adapter<CookRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CookingrowBinding, val view: View) : RecyclerView.ViewHolder(binding.root){
        // 아이템 하나를 클릭했을 때 그 아이템에 해당하는 조리 시간이 타이머로 넘어가게 하는 함수
        fun set_time_click(holder: ViewHolder){
            val one_row = holder.binding.cookingRowLayout
            one_row.setOnClickListener {
                // RecyclerView의 Item을 클릭했을 때 그 holder의 cookingTime이 타이머로 가야함
                // 타이머의 객체 = holder.binding.CookingTime 이런 식으로.
            }
        }

        fun data_bind(holder:ViewHolder, position: Int) {
            val CookName = holder.binding.CookingName
            val CookTime = holder.binding.CookingTime
            val CookImage = holder.binding.CookingImage

            CookName.text = items[position].cookingName
            CookTime.text = items[position].cookingTime
            Glide.with(itemView).load(items[position].cookingImg).into(CookImage)
        }
        fun bind(holder:ViewHolder, position:Int) {
            data_bind(holder,position)
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CookingrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(holder, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}