package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.devjaewoo.itsmywaye.databinding.ActivityPermissionBinding

class PermissionActivity : AppCompatActivity() {

    private var _binding: ActivityPermissionBinding? = null
    private val binding: ActivityPermissionBinding get() = _binding!!

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_CLOSE) {
            finish()
        }
        else {
            refreshPermissionStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPermissionBinding.inflate(layoutInflater)

        refreshPermissionStatus()

        setContentView(binding.root)
    }

    private fun refreshPermissionStatus() {
        if(!isNotificationReadPermissionGranted()) {
            //텍스트 변경
            binding.tvPermissionDescription.text = getString(R.string.tv_permission_description_notification)

            //gif 변경
            Glide.with(this).load(R.raw.example_grant_notification_read).into(binding.ivGrantNotificationExample)

            //버튼 동작 변경
            binding.btnGrantPermission.setOnClickListener {
                requestNotificationReadPermission()
            }
        }
        else if(!isWriteSettingsPermissionGranted()) {
            //텍스트 변경
            binding.tvPermissionDescription.text = getString(R.string.tv_permission_description_write_settings)

            //gif 변경
            Glide.with(this).load(R.raw.example_grant_settings_write).into(binding.ivGrantNotificationExample)

            //버튼 동작 변경
            binding.btnGrantPermission.setOnClickListener {
                requestWriteSettingsPermission()
            }
        }
        else {
            val intent = Intent(ApplicationManager.applicationContext, MainActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    private fun requestNotificationReadPermission() {
        if(!isNotificationReadPermissionGranted()) {
            resultLauncher.launch(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun isNotificationReadPermissionGranted(): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            notificationManager.isNotificationListenerAccessGranted(ComponentName(application, MessageListenerService::class.java))
        } else {
            NotificationManagerCompat.getEnabledListenerPackages(applicationContext).contains(applicationContext.packageName)
        }
    }


    private fun requestWriteSettingsPermission() {
        if(!isWriteSettingsPermissionGranted()) {
            resultLauncher.launch(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
        }
    }

    private fun isWriteSettingsPermissionGranted(): Boolean = Settings.System.canWrite(this)
}