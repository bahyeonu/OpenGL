package com.example.opengl

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

const val COORDS_PER_VERTEX = 3 // 하나의 정점이 갖는 좌표의 수 ex) 3 = (X, Y, Z)
var triangleCoords = floatArrayOf( // 반시계 방향
    0.0f, 0.622008459f, 0.0f, // 상단
    -0.5f, -0.311004243f, 0.0f, // 왼쪽 아래
    0.5f, -0.311004243f, 0.0f // 오른쪽 아래
)

var squareCoords = floatArrayOf(
    -0.5f, 0.5f, 0.0f,
    -0.5f, -0.5f, 0.0f,
    0.5f, -0.5f, 0.0f,
    0.5f, 0.5f, 0.0f
)

class Triangle{
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f) // 색상설정 (R, G, B, Alpha)

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(triangleCoords)
                position(0)
            }
        }


    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private var vPMatrixHandle: Int = 0

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int

    init {
        val myGLRenderer = MyGLRenderer()
        // vertex shader 타입 생성
        val vertexShader: Int = myGLRenderer.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        // fragment shader 타입 생성
        val fragmentShader: Int = myGLRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // 빈 OpenGL ES Program 생성
        mProgram = GLES30.glCreateProgram().also {
            // program에 vertex shader 추가
            GLES30.glAttachShader(it, vertexShader)
            // program에 fragment shader 추가
            GLES30.glAttachShader(it, fragmentShader)
            // 실행 가능한 OpenGL ES program 생성
            GLES30.glLinkProgram(it)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 정점당 4 바이트 할당

    fun draw(mvpMatrix: FloatArray){
        GLES30.glUseProgram(mProgram) // OpenGL ES environment에 program 추가
        // vertex shader의 vPosition member 핸들 획득
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {
            GLES30.glEnableVertexAttribArray(it) // 삼각형 정점 핸들 사용
            GLES30.glVertexAttribPointer(it, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer) // 도형 좌표 데이터 준비
            // fragment shader의 vPosition member 핸들 획득
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also {colorHandle ->
                GLES30.glUniform4fv(colorHandle, 1, color,0) // 도형 색상 설정
            }

            vPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix") // 회전시 uMVPMatrix member 획득
            GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0) // mvpMatrix 좌표 데이터 준비

            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount) // 도형 그리기
            GLES30.glDisableVertexAttribArray(positionHandle) // vertex array 비활성화

        }
    }
}

class Square{
    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f) // 색상설정 (R, G, B, Alpha)

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(squareCoords)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run{
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "}"

    private var vPMatrixHandle: Int = 0

    private val fragmentShaderCode =
        "precision mediump float;" +
                "uniform vec4 vColor;" +
                "void main() {" +
                "  gl_FragColor = vColor;" +
                "}"

    private var mProgram: Int

    init {
        val myGLRenderer = MyGLRenderer()
        // vertex shader 타입 생성
        val vertexShader: Int = myGLRenderer.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        // fragment shader 타입 생성
        val fragmentShader: Int = myGLRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // 빈 OpenGL ES Program 생성
        mProgram = GLES30.glCreateProgram().also {
            // program에 vertex shader 추가
            GLES30.glAttachShader(it, vertexShader)
            // program에 fragment shader 추가
            GLES30.glAttachShader(it, fragmentShader)
            // 실행 가능한 OpenGL ES program 생성
            GLES30.glLinkProgram(it)
        }
    }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = squareCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 정점당 4 바이트 할당

    fun draw(mvpMatrix: FloatArray){
        GLES30.glUseProgram(mProgram) // OpenGL ES environment에 program 추가
        // vertex shader의 vPosition member 핸들 획득
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {
            GLES30.glEnableVertexAttribArray(it) // 삼각형 정점 핸들 사용
            GLES30.glVertexAttribPointer(it, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer) // 도형 좌표 데이터 준비
            // fragment shader의 vPosition member 핸들 획득
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also {colorHandle ->
                GLES30.glUniform4fv(colorHandle, 1, color,0) // 도형 색상 설정
            }

            vPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix") // 회전시 uMVPMatrix member 획득
            GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0) // mvpMatrix 좌표 데이터 준비

            //GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount) // 도형 그리기
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.size, GLES30.GL_UNSIGNED_BYTE, drawListBuffer)
            GLES30.glDisableVertexAttribArray(positionHandle) // vertex array 비활성화

        }
    }
}
