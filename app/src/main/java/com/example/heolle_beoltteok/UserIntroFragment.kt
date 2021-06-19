package com.example.heolle_beoltteok

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heolle_beoltteok.databinding.FragmentUserIntroBinding
import com.example.heolle_beoltteok.databinding.FragmentUserMenuBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UserIntroFragment : Fragment() {
    val viewModel: MyViewModel by activityViewModels()
    var binding: FragmentUserIntroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserIntroBinding.inflate(layoutInflater,container, false)
        init()
        // Inflate the layout for this fragment
        return binding!!.root
    }


    private fun init() {
        binding!!.textView2.text = viewModel.getValue()
        binding!!.button.setOnClickListener {
            val fragment = activity?.supportFragmentManager?.beginTransaction()
            //fragment.addToBackStack(null)
            fragment?.replace(R.id.frameLayout, UserTimerFragment())
            fragment?.commit()
        }

    }

}
