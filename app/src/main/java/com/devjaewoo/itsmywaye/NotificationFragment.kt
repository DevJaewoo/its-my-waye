package com.devjaewoo.itsmywaye

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.devjaewoo.itsmywaye.databinding.FragmentAlarmBinding
import com.devjaewoo.itsmywaye.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)

        updateNotificationAllowed(ApplicationManager.isAllNotificationAllowed)
        updateDiscordNotificationAllowed(ApplicationManager.isDiscordNotificationAllowed)
        updateKakaotalkNotificationAllowed(ApplicationManager.isKakaotalkNotificationAllowed)


        binding.switchNotificationAll.setOnCheckedChangeListener { _, isChecked ->
            updateNotificationAllowed(isChecked)
        }

        binding.switchNotificationDiscord.setOnCheckedChangeListener { _, isChecked ->
            updateDiscordNotificationAllowed(isChecked)
        }

        binding.switchNotificationKakaotalk.setOnCheckedChangeListener { _, isChecked ->
            updateKakaotalkNotificationAllowed(isChecked)
        }

        return binding.root
    }

    private fun updateNotificationAllowed(enable: Boolean) {
        Log.d(TAG, "updateNotificationEnabled: $enable")

        binding.switchNotificationAll.isChecked = enable
        binding.notificationContent.visibility = if(!enable) View.VISIBLE else View.INVISIBLE

        ApplicationManager.isAllNotificationAllowed = enable
    }

    private fun updateDiscordNotificationAllowed(enable: Boolean) {
        Log.d(TAG, "updateDiscordNotificationEnabled: $enable")

        binding.switchNotificationDiscord.isChecked = enable
        ApplicationManager.isDiscordNotificationAllowed = enable
    }

    private fun updateKakaotalkNotificationAllowed(enable: Boolean) {
        Log.d(TAG, "updateKakaotalkNotificationEnabled: $enable")

        binding.switchNotificationKakaotalk.isChecked = enable
        ApplicationManager.isKakaotalkNotificationAllowed = enable
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            NotificationFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}