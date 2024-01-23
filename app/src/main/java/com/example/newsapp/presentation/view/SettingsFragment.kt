package com.example.newsapp.presentation.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import com.google.android.material.checkbox.MaterialCheckBox
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {
    private var politicsCheckBox: MaterialCheckBox? = null
    private var natureCheckBox: MaterialCheckBox? = null
    private var sportCheckBox: MaterialCheckBox? = null
    private var scienceCheckBox: MaterialCheckBox? = null
    private val sharedPreferences: SharedPreferences by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = resources.getString(R.string.settings_fragment_title)
        politicsCheckBox = view.findViewById(R.id.politicsCheckBox)
        natureCheckBox = view.findViewById(R.id.natureCheckBox)
        sportCheckBox = view.findViewById(R.id.sportCheckBox)
        scienceCheckBox = view.findViewById(R.id.scienceCheckBox)

        val politicsCheckedStatus = sharedPreferences.getBoolean(politicsCheckBox?.text.toString(), false)
        val natureCheckedStatus = sharedPreferences.getBoolean(natureCheckBox?.text.toString(), false)
        val sportCheckedStatus = sharedPreferences.getBoolean(sportCheckBox?.text.toString(), false)
        val scienceCheckedStatus = sharedPreferences.getBoolean(scienceCheckBox?.text.toString(), false)

        politicsCheckBox?.isChecked = politicsCheckedStatus
        natureCheckBox?.isChecked = natureCheckedStatus
        sportCheckBox?.isChecked = sportCheckedStatus
        scienceCheckBox?.isChecked = scienceCheckedStatus
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPreferences.edit()
        editor.putBoolean(politicsCheckBox?.text.toString(), politicsCheckBox?.isChecked ?: false)
        editor.putBoolean(natureCheckBox?.text.toString(), natureCheckBox?.isChecked ?: false)
        editor.putBoolean(sportCheckBox?.text.toString(), sportCheckBox?.isChecked ?: false)
        editor.putBoolean(scienceCheckBox?.text.toString(), scienceCheckBox?.isChecked ?: false)
        editor.apply()
    }
}