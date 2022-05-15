package com.devjaewoo.itsmywaye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.devjaewoo.itsmywaye.databinding.ActivityAlarmEditBinding
import com.devjaewoo.itsmywaye.model.Alarm

class AlarmEditActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmEditBinding? = null
    private val binding: ActivityAlarmEditBinding get() = _binding!!

    private lateinit var currentAlarm: Alarm
    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAlarmEditBinding.inflate(layoutInflater)

        currentIndex = intent.getIntExtra(EXTRA_ITEM_INDEX, -1)
        if(currentIndex == -1) {
            Log.w(TAG, "onCreate: Item Index가 null입니다.")
            finish()
            return
        }

        binding.switchAlarmEditOfftime.setOnCheckedChangeListener { _, status ->
            binding.layoutAlarmEditOfftime.visibility = if(status) View.VISIBLE else View.GONE
        }

        binding.npAlarmEditOfftimeStart.apply {
            minValue = 0
            maxValue = 23
        }

        binding.npAlarmEditOfftimeEnd.apply {
            minValue = 0
            maxValue = 23
        }

        binding.btnAlarmEditConfirm.setOnClickListener {

            if(binding.switchAlarmEditOfftime.isChecked) {
                currentAlarm.offTimeStart = binding.npAlarmEditOfftimeStart.value
                currentAlarm.offTimeEnd = binding.npAlarmEditOfftimeEnd.value
            }
            else {
                currentAlarm.offTimeStart = -1
                currentAlarm.offTimeEnd = -1
            }

            //DAO에 넣어주기
            ApplicationManager.ItemList[currentIndex].alarm = currentAlarm
            finish()
        }

        currentAlarm = if(ApplicationManager.ItemList[currentIndex].alarm != null) {
            Alarm(ApplicationManager.ItemList[currentIndex].alarm!!)
        } else {
            Alarm()
        }

        if(currentAlarm.offTimeStart != -1) { //금지시간 활성화
            binding.switchAlarmEditOfftime.isChecked = true
            binding.npAlarmEditOfftimeStart.value = currentAlarm.offTimeStart
            binding.npAlarmEditOfftimeEnd.value = currentAlarm.offTimeEnd
        }

        setContentView(binding.root)
    }
}