package com.devjaewoo.itsmywaye

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.devjaewoo.itsmywaye.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var lastBackPressedTime: Long = 0

    companion object {
        private const val DRAWER_GRAVITY = GravityCompat.START
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissions()
        createNotificationChannel()

        binding.contentMain.toolbar.ibToolbar.setOnClickListener {
            toggleDrawerLayout(binding.root)
        }

        binding.navView.setNavigationItemSelectedListener(this)
        setFragment(AlarmFragment())
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if(currentTime - lastBackPressedTime < 1000) {
            setResult(RESULT_CLOSE)
            finish()
        }
        else {
            Toast.makeText(this, "'이전' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            lastBackPressedTime = currentTime
        }
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
                setFragment(NotificationFragment.newInstance())
            }

            R.id.menu_settings -> {
                Log.d(TAG, "onNavigationItemSelected: Set fragment to Settings")
                setFragment(SettingsFragment.newInstance())
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

    private fun requestPermissions() {
        requestNotificationWritePermission()
    }

    private fun requestNotificationWritePermission() {
        if(!isNotificationWritePermissionGranted()) {
            Toast.makeText(this, "알림을 띄우기 위해 권한 설정이 필요합니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
    }

    private fun isNotificationWritePermissionGranted(): Boolean = NotificationManagerCompat.from(this).areNotificationsEnabled()

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val notificationChannel = NotificationChannel(NOTIFICATION_DEFAULT_CHANNEL_ID, NOTIFICATION_DEFAULT_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(notificationChannel)
        }
    }
}