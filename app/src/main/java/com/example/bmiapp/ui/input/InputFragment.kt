package com.example.bmiapp.ui.input

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.bmiapp.R

class InputFragment : Fragment() {

    private lateinit var inputViewModel: InputViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        inputViewModel = ViewModelProviders.of(this).get(InputViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_input, container, false)
        inputViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }
}
