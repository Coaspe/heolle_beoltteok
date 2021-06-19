package com.example.heolle_beoltteok



//class ExerciseAdapter(options: FirebaseRecyclerOptions<ExerciseData>)
//    : FirebaseRecyclerAdapter<ExerciseData, ExerciseAdapter.ViewHolder>(options) {
//
//
//
//    interface OnItemClickListener{
//        fun OnItemClick(holder: ExerciseAdapter.ViewHolder, view: View)
//    }
//    var itemClickListener:OnItemClickListener?=null
//
//
//
//    inner class ViewHolder(val binding: ExerciseRowBinding): RecyclerView.ViewHolder(binding.root){
//        init {
//            binding.root.setOnClickListener {
//                itemClickListener!!.OnItemClick(this, it)
//
//            }
//
//
//
//
//        }
//    }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = ExerciseRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: ExerciseData) {
//        holder.binding.apply {
//
//            ExerciseName.text = model.ename.toString()
//            ExerciseTime.text = (model.emin + " 분 "+model.esec+" 초").toString()
//            RestTime.text = (model.rmin + " 분 "+model.rsec+" 초").toString()
//
//
//
//        }
//    }
//}

