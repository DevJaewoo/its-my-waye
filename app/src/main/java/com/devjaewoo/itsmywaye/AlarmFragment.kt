package com.devjaewoo.itsmywaye

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.databinding.FragmentAlarmBinding
import com.devjaewoo.itsmywaye.model.Alarm
import java.lang.NumberFormatException


class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    lateinit var mContext: Context

    lateinit var alarmRecyclerView: RecyclerView
    lateinit var alarmRecyclerAdapter: AlarmRecyclerAdapter

    private val alarmListener = object : AlarmListener {

        override fun onAlarmChanged(id: Int, enabled: Boolean) {
            if(ApplicationManager.ItemList[id - 1].enabled != enabled) {
                ApplicationManager.ItemList[id - 1].enabled = enabled
                ItemDAO(mContext).update(ApplicationManager.ItemList[id - 1])
            }
        }

        override fun onAlarmEdit(id: Int) {
            val intent = Intent(mContext, AlarmEditActivity::class.java).apply {
                val index = ApplicationManager.ItemList.indexOfFirst { it.id == id }
                putExtra(EXTRA_ITEM_INDEX, index)
            }

            startActivity(intent)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d(TAG, "onCreateView: ")
        
        _binding = FragmentAlarmBinding.inflate(inflater, container, false)

        alarmRecyclerView = binding.recyclerviewAlarm
        alarmRecyclerAdapter = AlarmRecyclerAdapter(alarmListener)

        alarmRecyclerView.adapter = alarmRecyclerAdapter
        alarmRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        updateAlarmEnabled(ApplicationManager.isAlarmEnabled)

        binding.switchAlarmAll.setOnCheckedChangeListener { _, isChecked ->
            updateAlarmEnabled(isChecked)
        }
        
        return binding.root
    }

    private fun updateAlarmEnabled(enable: Boolean) {
        Log.d(TAG, "updateAlarmEnabled: $enable")

        binding.switchAlarmAll.isChecked = enable
        if (enable) binding.alarmContent.visibility = View.VISIBLE
        else binding.alarmContent.visibility = View.INVISIBLE

        ApplicationManager.isAlarmEnabled = enable
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