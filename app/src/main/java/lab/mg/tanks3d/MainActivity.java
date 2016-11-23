package lab.mg.tanks3d;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends Activity {

    public GLSurfaceView glSurfaceView;
    public static float xPixelSize;
    public static float yPixelSize;
    public static float screenCoefficient;
    public static int screenWidth;
    public static int screenHeight;
    public static final int[][] GAME_MAP = {
            {1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1}
    };
    public static final int[][] DEBUG_MAP = {
            {1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!supportES2()) {
            Toast.makeText(this, "OpenGL ES 2.0 is not supported", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Окно на весь экран
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        xPixelSize = 2.0f / screenWidth;
        yPixelSize = 2.0f / screenHeight;
        screenCoefficient = (float) screenWidth / screenHeight;

        GLSurfaceView.EGLConfigChooser configChooser = new GLConfigChooser();

        glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setEGLConfigChooser(configChooser);
        glSurfaceView.setRenderer(new OpenGLRenderer(this));

        setContentView(glSurfaceView);
        glSurfaceView.setOnTouchListener(Control.getInstance());
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();

    }

    private boolean supportES2() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        return (configurationInfo.reqGlEsVersion >= 0x20000);
    }
}
