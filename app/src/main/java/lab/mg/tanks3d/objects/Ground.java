package lab.mg.tanks3d.objects;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import lab.mg.tanks3d.containers.ObjectKeeper;
import lab.mg.tanks3d.utils.GLMatrixUtils;
import lab.mg.tanks3d.containers.Shader;
import lab.mg.tanks3d.containers.ShaderKeeper;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform3f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by kostya on 28.10.2016.
 */

public class Ground extends RenderableObject {
    private FloatBuffer vertexData;
    private FloatBuffer depthVertexData;
    private float size;

    private int texture;

    private float[] mModelMatrix = new float[16];

    private Shader shader;
    private Shader depthShader;


    public Ground(float x, float y, float z, String objName, float size, int texture) {
        super(x, y, z, objName, true);
        this.size = size;
        this.texture = texture;

        // find and assign shader, if not found throw exception
        if ((shader = ShaderKeeper.getInstance().getByName("rectangle_shader")) == null) {
            throw new RuntimeException("Can not find shader for Ground!");
        }

        // find and assign shader, if not found throw exception
        if ((depthShader = ShaderKeeper.getInstance().getByName("depth_map_shader")) == null) {
            throw new RuntimeException("Can not find shader for cube depth!");
        }

        prepareData();
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    private void prepareData() {
        float[] vertices = {
                getX(), getY(), getZ(), 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, // left bot
                getX(), getY() + size, getZ(), 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, // left top
                getX() + size, getY() + size, getZ(), 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, // right top
                getX() + size, getY(), getZ(), 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, // right bot
        };

        float[] verticesd = {
                getX(), getY(), getZ(), // left bot
                getX(), getY() + size, getZ(), // left top
                getX() + size, getY() + size, getZ(), // right top
                getX() + size, getY(), getZ(), // right bot
        };


        vertexData = ByteBuffer
                .allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.rewind();
        vertexData.put(vertices);

        depthVertexData = ByteBuffer
                .allocateDirect(verticesd.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        depthVertexData.rewind();
        depthVertexData.put(verticesd);
    }

    private void bindData() {
        float[] tempResultMatrix = new float[16];
        float[] mMVMatrix = new float[16];
        float[] mNormalMatrix = new float[16];
        float[] mMVPMatrix = new float[16];

        float[] mLightMVPMatrix = new float[16];
        float[] mLightPosInEyeSpace = new float[4];
        float[] mLightPosModel = new float[4];
        mLightPosModel[0] = GLMatrixUtils.getInstance().getLightX();
        mLightPosModel[1] = GLMatrixUtils.getInstance().getLightY();
        mLightPosModel[2] = GLMatrixUtils.getInstance().getLightZ();
        mLightPosModel[3] = 1.0f;

        float bias[] = new float [] {
                0.5f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.5f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f};

        float[] depthBiasMVP = new float[16];


        //calculate MV matrix
        Matrix.multiplyMM(tempResultMatrix, 0, GLMatrixUtils.getInstance().getViewMatrix(), 0, mModelMatrix, 0);
        System.arraycopy(tempResultMatrix, 0, mMVMatrix, 0, 16);
        //pass in MV Matrix as uniform
        int uMVMLocation = glGetUniformLocation(shader.getProgramId(), "uMVMatrix");
        glUniformMatrix4fv(uMVMLocation, 1, false, mMVMatrix, 0);

        //calculate Normal Matrix as uniform (invert transpose MV)
        Matrix.invertM(tempResultMatrix, 0, mMVMatrix, 0);
        Matrix.transposeM(mNormalMatrix, 0, tempResultMatrix, 0);
        //pass in Normal Matrix as uniform
        int uNormalMatrixLocation = glGetUniformLocation(shader.getProgramId(), "uNormalMatrix");
        glUniformMatrix4fv(uNormalMatrixLocation, 1, false, mNormalMatrix, 0);

        //calculate MVP matrix
        Matrix.multiplyMM(tempResultMatrix, 0, GLMatrixUtils.getInstance().getProjectionMatrix(), 0, mMVMatrix, 0);
        System.arraycopy(tempResultMatrix, 0, mMVPMatrix, 0, 16);

        //pass in MVP Matrix as uniform
        int uMVPMatrixLocation = glGetUniformLocation(shader.getProgramId(), "uMVPMatrix");
        glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mMVPMatrix, 0);

        // pass in light source position
        // calculate Light View Matrix
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, GLMatrixUtils.getInstance().getViewMatrix(), 0, mLightPosModel, 0);
        int uLightPosLocation = glGetUniformLocation(shader.getProgramId(), "uLightPos");
        glUniform3f(uLightPosLocation, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        //MVP matrix that was used during depth map render
        int uShadowMatrixLocation = glGetUniformLocation(shader.getProgramId(), "uShadowProjMatrix");
        Matrix.multiplyMM(mLightMVPMatrix, 0, GLMatrixUtils.getInstance().getLightViewMatrix(), 0, mModelMatrix, 0);
        Matrix.multiplyMM(mLightMVPMatrix, 0, GLMatrixUtils.getInstance().getLightProjectionMatrix(), 0, mLightMVPMatrix, 0);
        Matrix.multiplyMM(depthBiasMVP, 0, bias, 0, mLightMVPMatrix, 0);
        System.arraycopy(depthBiasMVP, 0, mLightMVPMatrix, 0, 16);
        glUniformMatrix4fv(uShadowMatrixLocation, 1, false, mLightMVPMatrix, 0);

        // pass shadow map texture
        int uShadowTextureLocation = glGetUniformLocation(shader.getProgramId(), "uShadowTexture");
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ObjectKeeper.getInstance().renderedDepthTexture[0]);
        glUniform1i(uShadowTextureLocation, 0);

        // pass object texture
        int uObjTextureLocation = glGetUniformLocation(shader.getProgramId(), "uObjectTexture");
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, texture);
        glUniform1i(uObjTextureLocation, 1);

        // примитивы
        int aPositionLocation = glGetAttribLocation(shader.getProgramId(), "aPosition");
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 8 * 4, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // координаты текстур
        int aTextureLocation = glGetAttribLocation(shader.getProgramId(), "aTexCoords");
        vertexData.position(3);
        glVertexAttribPointer(aTextureLocation, 2, GL_FLOAT, false, 8 * 4, vertexData);
        glEnableVertexAttribArray(aTextureLocation);

        // нормали
        int aNormalPosition = glGetAttribLocation(shader.getProgramId(), "aNormal");
        vertexData.position(5);
        glVertexAttribPointer(aNormalPosition, 3, GL_FLOAT, false, 8 * 4, vertexData);
        glEnableVertexAttribArray(aNormalPosition);
    }


    private void bindDataDepth() {
        // примитивы
        int aPositionLocation = glGetAttribLocation(depthShader.getProgramId(), "a_Position");
        depthVertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 3, GL_FLOAT, false, 0, depthVertexData);
        glEnableVertexAttribArray(aPositionLocation);
    }

    private void bindMatrixDepth() {
        // матрица
        int uMatrixLocation = glGetUniformLocation(depthShader.getProgramId(), "u_Matrix");
        float[] model = mModelMatrix.clone();
        //Matrix.setIdentityM(model, 0);
        float[] mMatrix = new float[16];
        Matrix.multiplyMM(mMatrix, 0, GLMatrixUtils.getInstance().getLightViewMatrix(), 0, model, 0);
        Matrix.multiplyMM(mMatrix, 0, GLMatrixUtils.getInstance().getLightProjectionMatrix(), 0, mMatrix, 0);
        glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0);
    }

    @Override
    public void render() {
        glUseProgram(shader.getProgramId());
        bindData();
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    @Override
    public void renderDepthOnly() {
        glUseProgram(depthShader.getProgramId());
        bindDataDepth();
        bindMatrixDepth();
        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    @Override
    public float getSizeX() {
        return 0;
    }

    @Override
    public float getSizeY() {
        return 0;
    }
}
