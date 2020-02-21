package com.example.opengl

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MyGLSurfaceView(context: Context): GLSurfaceView(context){
    private val gLRenderer: MyGLRenderer

    init{
        setEGLContextClientVersion(3) // OpenGL ES 3.0

        gLRenderer = MyGLRenderer()

        setRenderer(gLRenderer)
        //renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y

        when(e.action){
            MotionEvent.ACTION_MOVE -> {
                var dx: Float = x - previousX
                var dy: Float = y - previousY

                if(y > height / 2)  dx *= -1
                if(x < width / 2) dy += -1

                gLRenderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }

        previousX = x
        previousY = y

        return true
    }
}