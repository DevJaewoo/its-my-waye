package com.devjaewoo.itsmywaye

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.devjaewoo.itsmywaye.databinding.ActivityFullscreenAlarmBinding
import java.util.*

class FullscreenAlarmActivity : AppCompatActivity() {

    private var _binding: ActivityFullscreenAlarmBinding? = null
    private val binding: ActivityFullscreenAlarmBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFullscreenAlarmBinding.inflate(layoutInflater)

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        else {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        }

        binding.ivLogo.clipToOutline = true

        binding.tvItemName.text = intent.getStringExtra(EXTRA_ALARM_CONTENT) ?: "알람"
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+09:00"), Locale.KOREA)
        val hour = calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val minute = calendar.get(Calendar.MINUTE).toString().padStart(2, '0')
        val text = "010 $hour$minute ${hour}55"

        binding.tvItemNumber.text = text

        binding.ibCallOk.setOnClickListener {
            finish()
        }

        binding.ibCallCancel.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()

        val intent = Intent(ApplicationManager.applicationContext, AlarmService::class.java).apply {
            action = ACTION_ALARM_OFF
            putExtra(EXTRA_NOTIFICATION_ID, NOTIFICATION_DEFAULT_ID)
        }

        startService(intent)
    }
}