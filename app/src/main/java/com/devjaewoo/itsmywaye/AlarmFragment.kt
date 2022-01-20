package com.devjaewoo.itsmywaye

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

    var isAllEnabled: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context

        //context.getSharedPreferences("")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = binding.root
        alarmRecyclerView = binding.recyclerviewAlarm
        alarmRecyclerAdapter = AlarmRecyclerAdapter()
        return inflater.inflate(R.layout.fragment_alarm, container, false)
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