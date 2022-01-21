package com.devjaewoo.itsmywaye

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.devjaewoo.itsmywaye.database.DBHelper
import com.devjaewoo.itsmywaye.databinding.FragmentAlarmBinding
import com.devjaewoo.itsmywaye.model.Item

class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    lateinit var mContext: Context

    lateinit var alarmRecyclerView: RecyclerView
    lateinit var alarmRecyclerAdapter: AlarmRecyclerAdapter
    lateinit var alarmList: List<Item>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG, "onCreateView: ")
        
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        alarmRecyclerView = binding.recyclerviewAlarm
        alarmRecyclerAdapter = AlarmRecyclerAdapter()

        alarmRecyclerView.adapter = alarmRecyclerAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        alarmRecyclerAdapter.dataSet = SettingsManager.ItemList

        updateAlarmEnabled(SettingsManager.isAlarmEnabled)
        binding.switchAlarmAll.setOnCheckedChangeListener { _, isChecked ->
            updateAlarmEnabled(isChecked)
        }
        
        return binding.root
    }

    private fun updateAlarmEnabled(enable: Boolean) {
        Log.d(TAG, "updateAlarmEnabled: $enable")

        binding.switchAlarmAll.isChecked = enable
        if(enable) alarmRecyclerView.visibility = View.VISIBLE
        else alarmRecyclerView.visibility = View.INVISIBLE

        SettingsManager.isAlarmEnabled = enable
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AlarmFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}