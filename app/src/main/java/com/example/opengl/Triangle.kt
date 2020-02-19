package com.example.opengl

import android.opengl.GLES30
import java.nio.Buffer

class Triangle{
        private val triangleCoords = 3 //삼각형 정점 갯수
        private val COORDS_PER_VERTEX = 2 //

        private val vertexShaderCode =
            "attribute vec4 vPosition;" +
            "void main() {" +
            "   gl_Position = vPosition;" +
            "}"

        private val fragmentShaderCode =
            "precision mediump float" +
             "uniform vec4 vColor;" +
             "void main(){" +
             "  gl_FragColor = vColor;" +
             "}"

        val triangleBuffer: Buffer = GlUtil.createFloatBuffer(arrayOf( // 삼각형 꼭지점 좌표
            0.0f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
        ).toFloatArray())

        val color: FloatArray = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f) // 삼각형 색 지정

        private var mProgram: Int

        init {
            val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
            val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

            mProgram = GLES30.glCreateProgram().also {
                GLES30.glAttachShader(it, vertexShader)
                GLES30.glAttachShader(it, fragmentShader)
                GLES30.glLinkProgram(it)
            }
        }

        private var positionHandle: Int = 0
        private var mColorHandle: Int = 0

        private val vertexCount: Int = triangleCoords
        private val vertexStride: Int = COORDS_PER_VERTEX * 4

        fun draw(){
            GLES30.glUseProgram(mProgram)
            positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {
                GLES30.glEnableVertexAttribArray(it)
                GLES30.glVertexAttribPointer(it, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, triangleBuffer)
                mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also {colorHandle ->
                    GLES30.glUniform4fv(colorHandle, 1, color,0)
                }
                GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)
                GLES30.glDisableVertexAttribArray(it)
            }
        }

    }


fun loadShader(type: Int, shaderCode: String): Int{
    return GLES30.glCreateShader(type).also { shader ->
        GLES30.glShaderSource(shader, shaderCode)
        GLES30.glCompileShader(shader)
    }
}