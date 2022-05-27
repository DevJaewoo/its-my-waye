package com.devjaewoo.itsmywaye

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devjaewoo.itsmywaye.databinding.FragmentSettingsBinding
import kotlin.coroutines.coroutineContext

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.btnAlarmTest.setOnClickListener {
            Thread {
                Thread.sleep(3000)
                AlarmManager.startAlarm("웨이")
            }.run()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}