package com.example.opengl

import android.content.Context
import android.opengl.GLSurfaceView

class MyGLSurfaceView(context: Context): GLSurfaceView(context){
    private val renderer: MyGLRenderer

    init{
        setEGLContextClientVersion(3) // OpenGL ES 3.0

        renderer = MyGLRenderer()

        setRenderer(renderer)

    }
}