package com.devjaewoo.itsmywaye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.devjaewoo.itsmywaye.databinding.ActivityAlarmEditBinding

class AlarmEditActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmEditBinding? = null
    private val binding: ActivityAlarmEditBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlarmEditBinding.inflate(layoutInflater)

        intent

        setContentView(binding.root)
    }
}