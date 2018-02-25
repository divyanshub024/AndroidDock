package com.divyanshubhargava.androiddock

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.elevation = 0F

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.canDrawOverlays(this)){
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + packageName))
                startActivityForResult(intent, 100)
            }
        }else{
        }

        sw_dock.isChecked = isMyServiceRunning(DockOverLayService::class.java)

        val dockService = Intent(this,DockOverLayService::class.java)
        sw_dock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Log.e("overlayService","start")
                this.startService(dockService)
            } else {
                Log.e("overlayService","stop")
                this.stopService(dockService)
//                stopService(overlayService)
            }
        }

        tv_add_apps.setOnClickListener {
            val intent = Intent(this,SelectFavAppsActivity::class.java)
            startActivity(intent)
        }


    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return manager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
    }
}
