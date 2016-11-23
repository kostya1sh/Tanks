package lab.mg.tanks3d;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lab.mg.tanks3d.containers.ObjectKeeper;
import lab.mg.tanks3d.containers.ShaderKeeper;
import lab.mg.tanks3d.objects.Box;
import lab.mg.tanks3d.objects.CubeObject;
import lab.mg.tanks3d.objects.Tank;
import lab.mg.tanks3d.utils.GLMatrixUtils;
import lab.mg.tanks3d.utils.TextureUtils;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE1;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;

/**
 * Created by kostya on 24.10.2016.
 */

public class OpenGLRenderer implements GLSurfaceView.Renderer {
    private Context context;
    private int groundTexture;
    private int boxTexture;
    private int blueCamoTexture;
    private int greenCamoTexture;
    private int redCamoTexture;
    private int grayCamoTexture;
    private int projectileTexture;
    private boolean mHasDepthTextureExtension;
    private static final int DEFAULT_OBJECT_SIZE = 4;


    public OpenGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        long startTime = System.currentTimeMillis();

        //render
        ObjectKeeper.getInstance().renderAll();

        long endTime = System.currentTimeMillis();
        Log.w("Renderer", "render time = " + (float) (endTime - startTime) / 1000.0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);

        //TODO: Why not working?
        //glEnable(GLES20.GL_CULL_FACE);

        // init shaders there
        // shader for cubes
        if (!ShaderKeeper.getInstance().createShader(context, "cube_shader",
                R.raw.depth_tex_v_with_shadow, R.raw.depth_tex_f_with_simple_shadow)) {
            throw new RuntimeException("Shader was not created!");
        }

        // shader for 2d rectangle
        if (!ShaderKeeper.getInstance().createShader(context, "rectangle_shader",
                R.raw.depth_tex_v_with_shadow, R.raw.depth_tex_f_with_simple_shadow)) {
            throw new RuntimeException("Shader was not created!");
        }

        // shader for depth map
        if (!ShaderKeeper.getInstance().createShader(context, "depth_map_shader",
                R.raw.depth_map_vertex_shader, R.raw.depth_map_fragment_shader)) {
            throw new RuntimeException("Shader was not created!");
        }

        // create projection and view matrices
        GLMatrixUtils.getInstance().init(MainActivity.screenWidth, MainActivity.screenHeight, 12.0f, 12.0f, 15.0f);

        // load textures
        // all textures in first unit; third parameter not needed!
        groundTexture = TextureUtils.loadTexture(context, R.drawable.desert_texture, GL_TEXTURE0);
        boxTexture = TextureUtils.loadTexture(context, R.drawable.box_texture, GL_TEXTURE1);
        grayCamoTexture = TextureUtils.loadTexture(context, R.drawable.gray_camo_texture, GL_TEXTURE1);
        greenCamoTexture = TextureUtils.loadTexture(context, R.drawable.green_camo_texture, GL_TEXTURE1);
        redCamoTexture = TextureUtils.loadTexture(context, R.drawable.red_camo_texture, GL_TEXTURE1);
        blueCamoTexture = TextureUtils.loadTexture(context, R.drawable.blue_camo_texture, GL_TEXTURE1);
        projectileTexture = TextureUtils.loadTexture(context, R.drawable.projectile_texture, GL_TEXTURE1);

        // Create game map from int matrix
        GameMap.getInstance().create(CubeObject.DEFAULT_SIZE, DEFAULT_OBJECT_SIZE, MainActivity.DEBUG_MAP, groundTexture, boxTexture);

        if (Control.getInstance().getControllableTank() == null) {
            // Add controllable tank for player
            Control.getInstance().setControllableTank(new Tank(4.0f, 4.0f, 1.0f, "player_tank", DEFAULT_OBJECT_SIZE, greenCamoTexture, projectileTexture));
        }

        // A point in which camera is directed
        GLMatrixUtils.getInstance().setViewPoint(12.0f, 12.0f, -10.0f);

        // Light point
        GLMatrixUtils.getInstance().setLightPoint(12.0f, 12.0f, 13.0f);

        if (AI.getInstance().getTanks().size() < 1) {
            // Add controllable tank for AI
            AI.getInstance().addTank(new Tank(6.0f, 16.0f, 1.0f, "ai_tank", DEFAULT_OBJECT_SIZE, blueCamoTexture, projectileTexture));
        }

        new Box(10.0f, 10.0f, 3.0f, "AirBox", -1, boxTexture);

        // Test OES_depth_texture extension
        String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
        if (extensions.contains("OES_depth_texture")) {
            mHasDepthTextureExtension = true;
        } else {
            throw new RuntimeException("NO OES EXTENSION! CAN NOT RENDER SHADOWS!");
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        ObjectKeeper.getInstance().initFBO();
    }

}
