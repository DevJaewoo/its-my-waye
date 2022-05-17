package com.devjaewoo.itsmywaye

import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
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

        currentAlarm = if(ApplicationManager.ItemList[currentIndex].alarm != null) {
            Alarm(ApplicationManager.ItemList[currentIndex].alarm!!)
        } else {
            Alarm()
        }

        initializeComponents()

        binding.switchAlarmEditOfftime.setOnCheckedChangeListener { _, status ->
            binding.layoutAlarmEditOfftime.visibility = if(status) View.VISIBLE else View.GONE
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

            currentAlarm.filePath = "${if(binding.switchAlarmEditAlarmName.isChecked) 1 else 0}|${currentAlarm.filePath.substring(2)}"
            currentAlarm.vibrate = binding.switchAlarmEditVibrate.isChecked
            currentAlarm.fullscreen = binding.switchAlarmEditAlarmFullscreen.isChecked

            //DAO에 넣어주기
            ApplicationManager.ItemList[currentIndex].alarm = currentAlarm
            finish()
        }

        setContentView(binding.root)
    }

    private fun initializeComponents() {

        binding.npAlarmEditOfftimeStart.apply {
            minValue = 0
            maxValue = 23
        }

        binding.npAlarmEditOfftimeEnd.apply {
            minValue = 0
            maxValue = 23
        }

        if(currentAlarm.offTimeStart != -1 && currentAlarm.offTimeEnd != -1) { //이전에 알람 방해금지 시간을 켜놓은 상태
            binding.switchAlarmEditOfftime.visibility = View.VISIBLE
            binding.npAlarmEditOfftimeStart.value = currentAlarm.offTimeStart
            binding.npAlarmEditOfftimeEnd.value = currentAlarm.offTimeEnd
        }

        if(currentAlarm.filePath.isNotEmpty()) {
            binding.switchAlarmEditAlarmName.isChecked = currentAlarm.filePath.startsWith('1')
        }
        else { //Default Ringtone
            currentAlarm.filePath = "0|${RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE)}"
        }
        Log.d(TAG, "initializeComponents: FilePath: ${currentAlarm.filePath}")
        binding.tvAlarmEditAlarmName.text = RingtoneManager(this).run {
            val position = getRingtonePosition(Uri.parse(currentAlarm.filePath.substring(2)))
            getRingtone(position).getTitle(this@AlarmEditActivity)
        }

        binding.switchAlarmEditVibrate.isChecked = currentAlarm.vibrate
        binding.switchAlarmEditAlarmFullscreen.isChecked = currentAlarm.fullscreen
    }
}