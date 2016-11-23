package lab.mg.tanks3d.containers;

import android.opengl.GLES20;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lab.mg.tanks3d.AI;
import lab.mg.tanks3d.MainActivity;
import lab.mg.tanks3d.objects.IObject;
import lab.mg.tanks3d.objects.Tank;

/**
 * Created by kostya on 24.10.2016.
 */

public class ObjectKeeper {
    private static final int SHADOW_MAP_WIDTH = 512;
    private static final int SHADOW_MAP_HEIGHT = 512;
    private static ObjectKeeper currentInstance = new ObjectKeeper();
    private CopyOnWriteArrayList<IObject> objects = new CopyOnWriteArrayList<>();
    private boolean isRenderStopped = false;

    private int[] FBOid;
    private int[] depthTextureRBid;

    // generated depth texture
    public int[] renderedDepthTexture;


    /**
     * Class that control render and behavior of all RenderableObject's
     *
     * @return instance of this class
     */
    public static synchronized ObjectKeeper getInstance() {
        return currentInstance;
    }

    private ObjectKeeper() {
        /*empty*/
    }


    public synchronized void addObject(IObject object) {
        if (!objects.contains(object)) {
            objects.add(object);
        }
    }

    @Nullable
    public synchronized IObject getObjectByCoordinates(float x, float y, float z) {
        for (IObject object : objects) {
            if (object.getX() == x && object.getY() == y && object.getZ() == z) {
                return object;
            }
        }

        return null;
    }

    @Nullable
    public synchronized IObject getObjectByName(String name) {
        for (IObject object : objects) {
            if (object.getName().equals(name)) {
                return object;
            }
        }

        return null;
    }

    /**
     * method render all objects that have
     */
    public synchronized void renderAll() {
        if (!isRenderStopped) {

            // Cull front faces for shadow generation to avoid self shadowing
            renderDepthTexture();



            // bind default framebuffer
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            GLES20.glViewport(0, 0, MainActivity.screenWidth, MainActivity.screenHeight);


            // ai main function
            AI.getInstance().performAction();

            // render all objects
            for (IObject object : objects) {
                object.render();

                if (object.getName().contains("projectile")) {
                    checkProjectileCollision(object);
                }
            }

            // Print openGL errors to console
            int debugInfo = GLES20.glGetError();

            if (debugInfo != GLES20.GL_NO_ERROR) {
                String msg = "OpenGL error: " + debugInfo;
                Log.w("ObjectKeeper", msg);
            }
        }
    }

    /**
     * initialize Frame Buffer for depth map
     */
    public void initFBO() {
        FBOid = new int[1];
        depthTextureRBid = new int[1];
        renderedDepthTexture = new int[1];

        // create a framebuffer object
        GLES20.glGenFramebuffers(1, FBOid, 0);

        // create render buffer and bind 16-bit depth buffer
        GLES20.glGenRenderbuffers(1, depthTextureRBid, 0);
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, depthTextureRBid[0]);
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_COMPONENT16, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT);

        // Try to use a texture depth component
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glGenTextures(1, renderedDepthTexture, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, renderedDepthTexture[0]);

        // texture filters
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        // Remove artifact on the edges of the shadowmap
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE );

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, FBOid[0]);

        // Use a depth texture
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_DEPTH_COMPONENT, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, 0, GLES20.GL_DEPTH_COMPONENT, GLES20.GL_UNSIGNED_SHORT, null);

        // Attach the depth texture to FBO depth attachment point
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_TEXTURE_2D, renderedDepthTexture[0], 0);

        // check FBO status
        int FBOstatus = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if(FBOstatus != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            Log.e("ObjectKeeper", "GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
            throw new RuntimeException("GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO");
        }
    }

    /**
     * render depth map
     */
    private void renderDepthTexture() {
        // bind the generated framebuffer
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, FBOid[0]);
        GLES20.glViewport(0, 0, SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT);

        // Clear color and buffers
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glCullFace(GLES20.GL_FRONT);

        for (IObject object: objects) {
            //if (!object.getName().contains("tank")) {
                object.renderDepthOnly();
            //}
        }

        GLES20.glCullFace(GLES20.GL_BACK);
    }

    private void checkProjectileCollision(IObject projectile) {
        for (IObject checkObj : objects) {
            if (checkObj.equals(projectile)) {
                continue;
            }

            // Collision x-axis?
            boolean collisionX = projectile.getX() + projectile.getSizeX() >= checkObj.getX() &&
                    checkObj.getX() + checkObj.getSizeX() >= projectile.getX();

            // Collision y-axis?
            boolean collisionY = projectile.getY() + projectile.getSizeY() >= checkObj.getY() &&
                    checkObj.getY() + checkObj.getSizeY() >= projectile.getY();

            // Collision only if on both axes
            if (collisionX && collisionY) {
                // remove destroyed object and projectile
                objects.remove(projectile);
                objects.remove(checkObj);
                if (checkObj.getName().contains("ai")) {
                    AI.getInstance().removeTank((Tank) checkObj);
                }
                return;
            }
        }

        // if map end remove projectile
        if (projectile.getX() > 6 * 4.0f || projectile.getX() < 0.0f
                || projectile.getY() > 6 * 4.0f || projectile.getY() < 0.0f) {
            objects.remove(projectile);
        }
    }

    /**
     * stop rendering objects
     */
    public synchronized void stopRender() {
        isRenderStopped = true;
    }

    /**
     * start rendering objects
     */
    public synchronized void startRender() {
        isRenderStopped = false;
    }

    public List<IObject> getAllObjects() {
        return objects;
    }

}
