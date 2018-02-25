package com.divyanshubhargava.androiddock

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.view.menu.ActionMenuItemView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by divyanshu on 19/2/18.
 */
class DockAppAdapter(context: Context) : RecyclerView.Adapter<DockAppAdapter.ViewHolder>(){
    val mContext = context
    val dbHelper = DatabaseHelper(mContext)
    var appsList = dbHelper.getAllFavApps()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.appview_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return appsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val icon = mContext.packageManager.getApplicationIcon(appsList[position].name)

        holder.appName.text = appsList[position].label
        holder.appIcon.setImageDrawable(icon)
        holder.appItemView.setOnClickListener {
            val inputMethodManager : InputMethodManager = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            val intent : Intent = it.context.packageManager.getLaunchIntentForPackage(appsList[position].name)
            it.context.startActivity(intent)

        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val appName : TextView = itemView.findViewById(R.id.appName)
        val appIcon : ImageView = itemView.findViewById(R.id.appIcon)
        val appItemView : ConstraintLayout = itemView.findViewById(R.id.appItemView)
    }

}