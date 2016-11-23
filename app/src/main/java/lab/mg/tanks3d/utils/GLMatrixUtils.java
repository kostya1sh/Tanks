package lab.mg.tanks3d.utils;

import android.opengl.Matrix;

/**
 * Created by kostya on 24.10.2016.
 */
public class GLMatrixUtils {
    private float[] mProjectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mLightProjectionMatrix = new float[16];
    private float[] mLightViewMatrix = new float[16];

    // точка положения камеры
    private float eyeX = 0.0f;
    private float eyeY = 0.0f;
    private float eyeZ = 0.0f;

    // точка направления камеры
    private float centerX = 0.0f;
    private float centerY = 0.0f;
    private float centerZ = 0.0f;

    // up-вектор
    // показывает вдоль какой оси расположена камера
    private float upX = 0.0f;
    private float upY = 0.0f;
    private float upZ = 0.0f;

    private float lightX = 0.0f;
    private float lightY = 0.0f;
    private float lightZ = 50.0f;


    private static GLMatrixUtils currentInstance = new GLMatrixUtils();

    public static GLMatrixUtils getInstance() {
        return currentInstance;
    }

    private GLMatrixUtils() {
    }

    private void createProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 1;
        float far = 100;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mProjectionMatrix, 0, 1.1f * left, 1.1f * right, 1.1f * bottom, 1.1f * top, near, far);
    }

    private void createLightProjectionMatrix(int width, int height) {
        float ratio = 1;
        float left = -1;
        float right = 1;
        float bottom = -1;
        float top = 1;
        float near = 1;
        float far = 100;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

        Matrix.frustumM(mLightProjectionMatrix, 0, 1.1f * left, 1.1f * right, 1.1f * bottom, 1.1f * top, near, far);
    }

    private void setLightViewMatrix() {
        float[] mLightPosModel = new float[4];
        mLightPosModel[0] = GLMatrixUtils.getInstance().getLightX();
        mLightPosModel[1] = GLMatrixUtils.getInstance().getLightY();
        mLightPosModel[2] = GLMatrixUtils.getInstance().getLightZ();
        mLightPosModel[3] = 0.0f;

        // calculate Light View Matrix
        //Set view matrix from light source position
        //Set view matrix from light source position
        Matrix.setLookAtM(mLightViewMatrix, 0,
                //lightX, lightY, lightZ,
                mLightPosModel[0], mLightPosModel[1], mLightPosModel[2],
                //lookX, lookY, lookZ,
                //look in direction -y
                12.0f, 12.0f, 0.0f,
                //upX, upY, upZ
                //up vector in the direction of axisY
                //-mLightPosModel[0], 1.0f, -mLightPosModel[2]
                0.0f, 1.0f, 0.0f);
    }

    private void setViewMatrix(float xOffset, float yOffset, float zOffset) {
        // точка положения камеры
        eyeX = xOffset;
        eyeY = yOffset;
        eyeZ = zOffset;

        // точка направления камеры
        centerX = 0.0f;
        centerY = 0.0f;
        centerZ = 0.0f;

        // up-вектор
        // показывает вдоль какой оси расположена камера
        upX = 0.0f;
        upY = 1.0f;
        upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    public void setViewPoint(float x, float y, float z) {
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, x, y, z, upX, upY, upZ);
        setLightViewMatrix();
    }

    public void setLightPoint(float x, float y, float z) {
        this.lightX = x;
        this.lightY = y;
        this.lightZ = z;
        setLightViewMatrix();
    }

    public void init(int width, int height,
                     float xCameraOffset, float yCameraOffset, float zCameraOffset) {
        createProjectionMatrix(width, height);
        createLightProjectionMatrix(width, height);
        setViewMatrix(xCameraOffset, yCameraOffset, zCameraOffset);
        setLightViewMatrix();
    }

    public float getEyeX() {
        return eyeX;
    }

    public float getEyeY() {
        return eyeY;
    }

    public float getEyeZ() {
        return eyeZ;
    }

    public float[] getProjectionMatrix() {
        return mProjectionMatrix.clone();
    }

    public float[] getViewMatrix() {
        return mViewMatrix.clone();
    }

    public float getLightX() {
        return lightX;
    }

    public float getLightY() {
        return lightY;
    }

    public float getLightZ() {
        return lightZ;
    }

    public float[] getLightViewMatrix() {
        return mLightViewMatrix.clone();
    }

    public float[] getLightProjectionMatrix() {
        return mLightProjectionMatrix.clone();
    }
}
