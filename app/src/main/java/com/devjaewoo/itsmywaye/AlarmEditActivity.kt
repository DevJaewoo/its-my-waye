package com.devjaewoo.itsmywaye

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import com.devjaewoo.itsmywaye.dao.AlarmDAO
import com.devjaewoo.itsmywaye.dao.ItemDAO
import com.devjaewoo.itsmywaye.databinding.ActivityAlarmEditBinding
import com.devjaewoo.itsmywaye.model.Alarm

class AlarmEditActivity : AppCompatActivity() {

    private var _binding: ActivityAlarmEditBinding? = null
    private val binding: ActivityAlarmEditBinding get() = _binding!!

    private lateinit var currentAlarm: Alarm
    private var currentIndex: Int = 0

    private val activityResultCallback = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val name = result.data?.getStringExtra(EXTRA_RINGTONE_NAME)
        val uri = result.data?.getStringExtra(EXTRA_RINGTONE_URI)
        val volume = result.data?.getIntExtra(EXTRA_RINGTONE_VOLUME, currentAlarm.volume)
        
        if(uri != null) currentAlarm.filePath = "${currentAlarm.filePath.substring(0, 2)}$uri"
        if(name != null) binding.tvAlarmEditAlarmName.text = name
        if(volume != null) currentAlarm.volume = volume

        Log.d(TAG, "resultCallback: name: $name uri: $uri volume: $volume")
    }

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

        binding.toolbar.ibToolbar.setOnClickListener {
            finish()
            return@setOnClickListener
        }

        binding.switchAlarmEditOfftime.setOnCheckedChangeListener { _, status ->
            binding.layoutAlarmEditOfftime.visibility = if(status) View.VISIBLE else View.GONE
        }

        binding.layoutAlarmEditAlarmName.setOnClickListener {
            val intent = Intent(this, RingtoneSelectActivity::class.java).apply {
                putExtra(EXTRA_RINGTONE_NAME, binding.tvAlarmEditAlarmName.text)
                putExtra(EXTRA_RINGTONE_VOLUME, currentAlarm.volume)
            }

            activityResultCallback.launch(intent)
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
            if(currentAlarm.id == -1) { //ID가 없는 경우(새로 생성): Insert
                currentAlarm.id = AlarmDAO(this).insert(currentAlarm)
            }
            else {
                AlarmDAO(this).update(currentAlarm)
            }

            ApplicationManager.ItemList[currentIndex].alarm = currentAlarm
            ItemDAO(this).update(ApplicationManager.ItemList[currentIndex])

            finish()
        }

        setContentView(binding.root)
    }

    private fun initializeComponents() {

        binding.toolbar.ibToolbar.setImageResource(R.drawable.ic_baseline_chevron_left_24)
        binding.toolbar.ibToolbar.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.toolbar.tvToolbar.text = ApplicationManager.ItemList[currentIndex].name

        binding.npAlarmEditOfftimeStart.apply {
            minValue = 0
            maxValue = 23
        }

        binding.npAlarmEditOfftimeEnd.apply {
            minValue = 0
            maxValue = 23
        }

        if(currentAlarm.offTimeStart != -1 && currentAlarm.offTimeEnd != -1) { //이전에 알람 방해금지 시간을 켜놓은 상태
            binding.switchAlarmEditOfftime.isChecked = true
            binding.layoutAlarmEditOfftime.visibility = View.VISIBLE
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