package com.example.flex.ui.screens.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.flex.R

class SettingsFragment : Fragment() {

    private lateinit var projTitle: EditText
    private lateinit var projDesc: EditText
    private lateinit var submit: Button
    private lateinit var cancel:Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)

        projTitle = view.findViewById(R.id.projTitleEdit)
        projDesc =  view.findViewById(R.id.projDescEdit)

        submit = view.findViewById<Button>(R.id.submit)
        cancel = view.findViewById<Button>(R.id.cancel)

        //projTitle.setText(Project.project.title)
        //projDesc.setText(Project.project.description)


        submit.setOnClickListener {
            //Project.project.title = projTitle.text.toString()
            //Project.project.description = projDesc.text.toString()
           // view.findNavController().
           // navigate(R.id.action_editFragment_pop)
        }
        cancel.setOnClickListener {
           // view.findNavController().
           // navigate(R.id.action_editFragment_pop)
        }
    }
}