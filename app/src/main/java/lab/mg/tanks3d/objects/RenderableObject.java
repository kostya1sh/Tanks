package lab.mg.tanks3d.objects;

import lab.mg.tanks3d.containers.ObjectKeeper;

/**
 * Created by kostya on 24.10.2016.
 */

public abstract class RenderableObject implements IObject {
    private float x;
    private float y;
    private float z;
    private float rotateAngle;
    private String name;

    public RenderableObject(float x, float y, float z, String objName, boolean isRenderItself) {
        if (isRenderItself) {
            // add object to container
            ObjectKeeper.getInstance().addObject(this);
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.rotateAngle = 0.0f;
        this.name = objName;
    }

    @Override
    public abstract void render();

    @Override
    public abstract void renderDepthOnly();

    @Override
    public synchronized float getX() {
        return x;
    }

    @Override
    public synchronized void  setX(float x) {
        this.x = x;
    }

    @Override
    public synchronized float getY() {
        return y;
    }

    @Override
    public synchronized void setY(float y) {
        this.y = y;
    }

    @Override
    public synchronized float getZ() {
        return z;
    }

    @Override
    public synchronized void setZ(float z) {
        this.z = z;
    }

    @Override
    public synchronized String getName() {
        return name;
    }
}
