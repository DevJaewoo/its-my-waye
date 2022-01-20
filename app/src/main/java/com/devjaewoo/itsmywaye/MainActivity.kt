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
import androidx.fragment.app.Fragment
import com.devjaewoo.itsmywaye.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val DRAWER_GRAVITY = GravityCompat.START

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
        setFragment(AlarmFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onNavigationItemSelected: ${item.title}")
        when(item.itemId) {
            R.id.menu_alarm -> {
                Log.d(TAG, "onNavigationItemSelected: Set fragment to Alarm")
                setFragment(AlarmFragment.newInstance())
            }

            R.id.menu_notification -> {
                Log.d(TAG, "onNavigationItemSelected: Set fragment to Notification")
                setFragment(NotificationFragment())
            }

            R.id.menu_settings -> {
                Log.d(TAG, "onNavigationItemSelected: Set fragment to Settings")
                setFragment(SettingsFragment())
            }

            else -> {
                Log.d(TAG, "onNavigationItemSelected: WTF")
            }
        }

        binding.root.closeDrawer(DRAWER_GRAVITY)
        return true
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view, fragment).commit()
    }

    private fun toggleDrawerLayout(drawerLayout: DrawerLayout) {

        if(!drawerLayout.isDrawerOpen(DRAWER_GRAVITY)) {
            drawerLayout.openDrawer(DRAWER_GRAVITY)
        }
        else {
            drawerLayout.closeDrawer(DRAWER_GRAVITY)
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