package com.divyanshubhargava.androiddock

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.RelativeLayout
import android.util.DisplayMetrics



/**
 * Created by divyanshu on 19/2/18.
 */
class DockOverLayService : Service(),View.OnTouchListener{


    private lateinit var mWindowManager: WindowManager
    private lateinit var view : View
    private lateinit var params : WindowManager.LayoutParams
    private lateinit var dockRecyclerView : RecyclerView
    private lateinit var gestureDetector : GestureDetector
    private lateinit var dockHead : RelativeLayout
    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()

        view = LayoutInflater.from(this).inflate(R.layout.layout_floating_view,null)

        params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT

        )

        gestureDetector = GestureDetector(this, SingleTapConfirm())
        //Specify the dock head position
        params.gravity = Gravity.TOP or Gravity.END
        params.x = 0
        params.y = 100

        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mWindowManager.addView(view,params)

        dockHead = view.findViewById(R.id.floatingHead)
        dockRecyclerView = view.findViewById(R.id.dockRecyclerView)
        dockRecyclerView.layoutManager = LinearLayoutManager(this)

//        dockHead.setOnTouchListener(this)

        dockHead

        dockRecyclerView.adapter = DockAppAdapter(this)

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {

        return when(v!!.id) {
            R.id.floatingHead -> {
                floatingHeadOnTouch(event)
                true
            }
            else -> false
        }
    }

    private var lastAction: Int = 0
    private var initialX: Int = 0
    private var initialY: Int = 0
    private var initialTouchX: Float = 0.toFloat()
    private var initialTouchY: Float = 0.toFloat()

    private fun floatingHeadOnTouch(event: MotionEvent?) {

        if (gestureDetector.onTouchEvent(event)){
            toggleDockVisibility()
        }else{
            when(event!!.action){
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y

                    //get the touch location
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    lastAction = event.action
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = 0
                    params.y = initialY + (event.rawY - initialTouchY).toInt()

                    //Update the layout with new X & Y coordinate
                    mWindowManager.updateViewLayout(view, params)
                    lastAction = event.action
                }
            }
        }
    }

    private fun toggleDockVisibility(){
        val start = 0f
        val end = dockRecyclerView.width.toFloat()

        if (dockRecyclerView.translationX == start){
            dockHead.animate().translationX(end)
            dockRecyclerView.animate().translationX(end)
        }else{
            dockHead.animate().translationX(start)
            dockRecyclerView.animate().translationX(start)
        }
    }


    private inner class SingleTapConfirm : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(event: MotionEvent): Boolean {
            return true
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mWindowManager.removeView(view)
    }
}