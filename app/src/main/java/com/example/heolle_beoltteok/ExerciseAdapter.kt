package com.example.heolle_beoltteok



import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.heolle_beoltteok.databinding.ExerciseRowBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions


class ExerciseAdapter(options: FirebaseRecyclerOptions<ExerciseData>)
    : FirebaseRecyclerAdapter<ExerciseData, ExerciseAdapter.ViewHolder>(options) {



    interface OnItemClickListener{
        fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View)
    }
    var itemClickListener:OnItemClickListener?=null

    inner class ViewHolder(val binding: ExerciseRowBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {
                itemClickListener!!.OnItemClick(this, it)

            }




        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExerciseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ExerciseData) {
        holder.binding.apply {

            ExerciseName.text = model.name
            ExerciseTime.text = model.time



        }
    }
}

