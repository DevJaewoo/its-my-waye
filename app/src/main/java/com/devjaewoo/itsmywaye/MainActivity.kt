package com.devjaewoo.itsmywaye

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.devjaewoo.itsmywaye.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var audioManager: AudioManager
        lateinit var notificationManager: NotificationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        requestWriteSettingsPermission()
        requestNotificationPermission()

        binding.contentMain.toolbar.ibToolbar.setOnClickListener {
            toggleDrawerLayout(binding.root)
        }

        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected: ${item.title}")
        return true
    }

    private fun toggleDrawerLayout(drawerLayout: DrawerLayout) {

        if(!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        else {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun requestWriteSettingsPermission() {
        if(!Settings.System.canWrite(this)) {
            Toast.makeText(this, "알림음을 켜기 위해 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS))
        }
    }

    private fun requestNotificationPermission() {
        if(!isNotificationPermissionGranted()) {
            Toast.makeText(this, "알림을 읽기 위해 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun isNotificationPermissionGranted(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            return notificationManager.isNotificationListenerAccessGranted(ComponentName(application, MessageListenerService::class.java))
        }
        else {
            return NotificationManagerCompat.getEnabledListenerPackages(applicationContext).contains(applicationContext.packageName)
        }
    }
}