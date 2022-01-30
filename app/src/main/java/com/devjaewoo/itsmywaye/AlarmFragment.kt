package com.devjaewoo.itsmywaye

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjaewoo.itsmywaye.databinding.FragmentAlarmBinding
import java.lang.NumberFormatException


class AlarmFragment : Fragment() {

    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    lateinit var mContext: Context

    lateinit var alarmRecyclerView: RecyclerView
    lateinit var alarmRecyclerAdapter: AlarmRecyclerAdapter

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

        alarmRecyclerAdapter.dataSet = ApplicationManager.ItemList

        updateAlarmEnabled(ApplicationManager.isAlarmEnabled)
        updateAlarmOfftimeStart(ApplicationManager.alarmOffTimeStart)
        updateAlarmOfftimeEnd(ApplicationManager.alarmOffTimeEnd)

        binding.switchAlarmAll.setOnCheckedChangeListener { _, isChecked ->
            updateAlarmEnabled(isChecked)
        }


        binding.editAlarmOfftimeStart.addTextChangedListener {
            try {
                binding.editAlarmOfftimeStart.setSelection(it?.length ?: 0)
                updateAlarmOfftimeStart(it.toString().toInt())
            }
            catch (e: NumberFormatException) {}
        }

        binding.editAlarmOfftimeStart.setOnFocusChangeListener { _, isFocused ->
            if(!isFocused) {
                try {
                    binding.editAlarmOfftimeStart.text.toString().toInt()
                }
                catch (e: NumberFormatException) {
                    updateAlarmOfftimeStart(0)
                }
            }
        }


        binding.editAlarmOfftimeEnd.addTextChangedListener {
            try {
                binding.editAlarmOfftimeEnd.setSelection(it?.length ?: 0)
                updateAlarmOfftimeEnd(it.toString().toInt())
            }
            catch (e: NumberFormatException) {}
        }

        binding.editAlarmOfftimeEnd.setOnFocusChangeListener { _, isFocused ->
            if(!isFocused) {
                try {
                    binding.editAlarmOfftimeEnd.text.toString().toInt()
                }
                catch (e: NumberFormatException) {
                    updateAlarmOfftimeEnd(0)
                }
            }
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

    private fun updateAlarmOfftimeStart(start: Int) {
        val formattedStart = when {
            start < 0 -> 0
            start > 23 -> 23
            else -> start
        }

        if(binding.editAlarmOfftimeStart.text.toString() != formattedStart.toString()) {
            binding.editAlarmOfftimeStart.setText(formattedStart.toString()) //toString 안하면 가끔 에러남
        }

        Log.d(TAG, "updateAlarmOfftimeStart: $formattedStart")
        ApplicationManager.alarmOffTimeStart = formattedStart
    }

    private fun updateAlarmOfftimeEnd(end: Int) {
        val formattedEnd = when {
            end < 0 -> 0
            end > 23 -> 23
            else -> end
        }

        if(binding.editAlarmOfftimeEnd.text.toString() != formattedEnd.toString()) {
            binding.editAlarmOfftimeEnd.setText(formattedEnd.toString()) //toString 안하면 가끔 에러남
        }

        Log.d(TAG, "updateAlarmOfftimeEnd: $formattedEnd")
        ApplicationManager.alarmOffTimeEnd = formattedEnd
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