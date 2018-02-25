package com.divyanshubhargava.androiddock

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

/**
 * Created by divyanshu on 18/2/18.
 */

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DB_NAME,null, DATABASE_VERSION){

    object TableContents{

        object AllApps : BaseColumns{
            const val TABLE_AllApps = "ALLAPPS"
            const val COLUMN_NAME = "NAME"
            const val COLUMN_LABEL = "LABEL"
            const val COLUMN_CHECKED = "CHECKED"
        }

        object FavApps : BaseColumns{
            const val TABLE_FavApps = "FAVAPPS"
            const val COLUMN_NAME = "NAME"
            const val COLUMN_LABEL = "LABEL"
            const val COLUMN_CHECKED = "CHECKED"
        }
    }

    //Create statements
    private val CREATE_TABLE_ALLAPPS = "Create table ${TableContents.AllApps.TABLE_AllApps} ("+
            "${BaseColumns._ID} Integer Primary Key,"+
            "${TableContents.AllApps.COLUMN_NAME} TEXT,"+
            "${TableContents.AllApps.COLUMN_LABEL} TEXT,"+
            "${TableContents.AllApps.COLUMN_CHECKED} Integer)"

    private val CREATE_TABLE_FAVAPPS = "Create table ${TableContents.FavApps.TABLE_FavApps} ("+
            "${BaseColumns._ID} Integer Primary Key,"+
            "${TableContents.FavApps.COLUMN_NAME} TEXT,"+
            "${TableContents.FavApps.COLUMN_LABEL} TEXT,"+
            "${TableContents.FavApps.COLUMN_CHECKED} Integer)"

    //Delete Statements
    private val DELETE_TABLE_ALLAPPS = "DROP TABLE IF EXISTS ${TableContents.AllApps.TABLE_AllApps}"
    private val DELETE_TABLE_FAVAPPS = "DROP TABLE IF EXISTS ${TableContents.FavApps.TABLE_FavApps}"





    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_ALLAPPS)
        db.execSQL(CREATE_TABLE_FAVAPPS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL(DELETE_TABLE_ALLAPPS)
        db.execSQL(DELETE_TABLE_FAVAPPS)

        onCreate(db)
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DB_NAME = "App.db"
    }

    fun addAppsToFav(appDetails: AppDetails){
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TableContents.FavApps.COLUMN_NAME,appDetails.name)
            put(TableContents.FavApps.COLUMN_LABEL,appDetails.label)
            put(TableContents.FavApps.COLUMN_CHECKED,appDetails.checked)
        }

        db.insert(TableContents.FavApps.TABLE_FavApps,null,values)
    }

    fun removeAppFromFav(appDetails: AppDetails){

    }

    fun getAllFavApps():ArrayList<AppDetails>{

        val list = ArrayList<AppDetails>()
        val selectQuery = "SELECT  * FROM " + TableContents.FavApps.TABLE_FavApps

        val db = this.writableDatabase
        val cursor : Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val appDetails = AppDetails(cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)))

                list.add(appDetails)
            }while (cursor.moveToNext())
        }

        return list
    }

    fun addApps(appDetails: AppDetails){

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(TableContents.AllApps.COLUMN_NAME,appDetails.name)
            put(TableContents.AllApps.COLUMN_LABEL,appDetails.label)
            put(TableContents.AllApps.COLUMN_CHECKED,appDetails.checked)
        }

        db.insert(TableContents.AllApps.TABLE_AllApps,null,values)

    }

    fun getAllApps():ArrayList<AppDetails>{

        val list = ArrayList<AppDetails>()
        val selectQuery = "SELECT  * FROM " + TableContents.AllApps.TABLE_AllApps

        val db = this.writableDatabase
        val cursor : Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()){
            do {
                val appDetails = AppDetails(cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)))

                list.add(appDetails)
            }while (cursor.moveToNext())
        }

        return list
    }

    fun isEmpty(): Boolean? {
        val db = this.writableDatabase
        val count = "SELECT count(*) FROM " + TableContents.AllApps.TABLE_AllApps
        val mcursor = db.rawQuery(count, null)
        mcursor.moveToFirst()
        val icount = mcursor.getInt(0)
        return icount <= 0

    }




}
