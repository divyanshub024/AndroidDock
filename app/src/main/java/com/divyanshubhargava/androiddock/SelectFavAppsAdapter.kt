package com.divyanshubhargava.androiddock

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

/**
 * Created by divyanshu on 18/2/18.
 */
class SelectFavAppsAdapter(context : Context) : RecyclerView.Adapter<SelectFavAppsAdapter.ViewHolder>(){

    val mContext = context
    val dbHelper = DatabaseHelper(mContext)
    var appsList = dbHelper.getAllApps()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.app_list_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return appsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = mContext.packageManager.getApplicationIcon(appsList[position].name)
        holder.listAppName.text = appsList[position].label
        holder.listAppIcon.setImageDrawable(icon)
        holder.listAppCheckBox.setOnCheckedChangeListener(null)
        holder.listAppCheckBox.isChecked = appsList[position].checked == 1
        holder.listAppCheckBox.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                val db = dbHelper.writableDatabase

            // New value for one column
            var checkValue = 0
            if (isChecked){
                checkValue = 1
                dbHelper.addAppsToFav(AppDetails(appsList[position].name,appsList[position].label,0))
            }else{
                checkValue = 0
                val deleteSelection = "${DatabaseHelper.TableContents.FavApps.COLUMN_NAME} LIKE ?"
                val deleteSelectionArgs = arrayOf(appsList[position].name)
                db.delete(DatabaseHelper.TableContents.FavApps.TABLE_FavApps,deleteSelection,deleteSelectionArgs)

            }
            val values = ContentValues().apply {
                put(DatabaseHelper.TableContents.AllApps.COLUMN_CHECKED,checkValue)
            }

            val selection = "${DatabaseHelper.TableContents.AllApps.COLUMN_NAME} LIKE ?"
            val selectionArgs = arrayOf(appsList[position].name)
            val count = db.update(DatabaseHelper.TableContents.AllApps.TABLE_AllApps,
                    values,
                    selection,
                    selectionArgs)
            Log.e("count",count.toString())
                updateAppList()
            }

        })
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val listAppName : TextView = itemView.findViewById(R.id.listAppName)
        val listAppIcon : ImageView = itemView.findViewById(R.id.listAppIcon)
        val listAppCheckBox : CheckBox = itemView.findViewById(R.id.listAppCheckBox)
    }
    fun updateAppList(){
        appsList = dbHelper.getAllApps()
    }

}