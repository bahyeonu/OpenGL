package com.example.opengl

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


object GlUtil{
    val SIZEOF_FLOAT = 4

    fun createFloatBuffer(coords: FloatArray): FloatBuffer{
        val byteBuffer  = ByteBuffer.allocateDirect(coords.size * SIZEOF_FLOAT)
        byteBuffer.order(ByteOrder.nativeOrder())
        val floatBuffer = byteBuffer.asFloatBuffer()
        floatBuffer.put(coords)
        floatBuffer.position(0)
        return floatBuffer
    }
}