package com.example.flex.ui.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.flex.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        val settingsButton = view.findViewById<Button>(R.id.settingButton)



        //projTitle.setText(Project.project.title)
        //projDesc.setText(Project.project.description)


        settingsButton.setOnClickListener {
            //Project.project.title = projTitle.text.toString()
            //Project.project.description = projDesc.text.toString()
             view.findNavController().navigate(R.id.settingsFragment)
        }

    }
}