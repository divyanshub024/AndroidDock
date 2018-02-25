package com.divyanshubhargava.androiddock

import android.content.Intent
import android.database.sqlite.SQLiteException
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_select_fav_apps.*

class SelectFavAppsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_fav_apps)

        rv_allAppList.layoutManager = LinearLayoutManager(this)


        val dbHelper = DatabaseHelper(this)

        try {
            if (dbHelper.isEmpty()!!){
                val i = Intent(Intent.ACTION_MAIN, null)
                i.addCategory(Intent.CATEGORY_LAUNCHER)
                val availableActivities = packageManager.queryIntentActivities(i, 0)
                for (ri in availableActivities) {


                    val app = AppDetails(ri.activityInfo.packageName,
                            ri.loadLabel(packageManager).toString(), 0)

                    dbHelper.addApps(app)
                }
            }
        }catch (e : SQLiteException){

        }

        val adapter = SelectFavAppsAdapter(this)
        rv_allAppList.adapter = adapter
    }
}
