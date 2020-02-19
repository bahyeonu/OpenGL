package com.example.opengl

import android.app.Activity
import android.opengl.GLSurfaceView
import android.os.Bundle

class OpenGLES30Activity : Activity(){
    private  lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gLView = MyGLSurfaceView(this)
        setContentView(gLView)
    }
}