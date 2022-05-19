package com.devjaewoo.itsmywaye

import android.content.Intent
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devjaewoo.itsmywaye.adapter.RingtoneRecyclerAdapter
import com.devjaewoo.itsmywaye.databinding.ActivityRingtoneSelectBinding
import com.devjaewoo.itsmywaye.dto.RingtoneDataDTO

class RingtoneSelectActivity : AppCompatActivity() {

    private var _binding: ActivityRingtoneSelectBinding? = null
    private val binding: ActivityRingtoneSelectBinding get() = _binding!!

    private lateinit var ringtoneRecyclerAdapter: RingtoneRecyclerAdapter
    private lateinit var ringtoneRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRingtoneSelectBinding.inflate(layoutInflater)


        binding.toolbar.tvToolbar.text = "알람음 선택"
        binding.toolbar.ibToolbar.setImageResource(R.drawable.ic_baseline_chevron_left_24)
        binding.toolbar.ibToolbar.scaleType = ImageView.ScaleType.CENTER_CROP
        binding.toolbar.ibToolbar.setOnClickListener {
            finish()
            return@setOnClickListener
        }

        val ringtoneList: MutableList<RingtoneDataDTO> = ArrayList()
        RingtoneManager(this).cursor.run {
            Log.d(TAG, "Ringtones: ")
            while(moveToNext()) {
                val ringtoneData = RingtoneDataDTO().apply {
                    name = getString(RingtoneManager.TITLE_COLUMN_INDEX)
                    uri = "${getString(RingtoneManager.URI_COLUMN_INDEX)}/${getString(RingtoneManager.ID_COLUMN_INDEX)}"
                    selected = false
                }

                ringtoneList.add(ringtoneData)
            }
        }

        val index = ringtoneList.indexOfFirst { it.name == intent.getStringExtra(EXTRA_RINGTONE_NAME) }
        ringtoneRecyclerAdapter = RingtoneRecyclerAdapter(ringtoneList, index)
        ringtoneRecyclerView = binding.recyclerRingtoneList
        ringtoneRecyclerView.adapter = ringtoneRecyclerAdapter
        ringtoneRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        binding.seekBar.progress = intent.getIntExtra(EXTRA_RINGTONE_VOLUME, 50)

        binding.btnRingtoneSelectConfirm.setOnClickListener {
            val result = Intent().apply {
                ringtoneRecyclerAdapter.getSelectedRingtone().apply {
                    putExtra(EXTRA_RINGTONE_NAME, name)
                    putExtra(EXTRA_RINGTONE_URI, uri)
                }

                putExtra(EXTRA_RINGTONE_VOLUME, binding.seekBar.progress)
            }

            setResult(RESULT_OK, result)
            finish()
        }

        setContentView(binding.root)
    }
}