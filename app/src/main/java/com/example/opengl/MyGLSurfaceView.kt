package com.example.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(context: Context): GLSurfaceView(context){
    private val gLRenderer: MyGLRenderer

    init{
        setEGLContextClientVersion(3) // OpenGL ES 3.0

        gLRenderer = MyGLRenderer()

        setRenderer(gLRenderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}