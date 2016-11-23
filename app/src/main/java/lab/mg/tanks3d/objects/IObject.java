package lab.mg.tanks3d.objects;

/**
 * Created by kostya on 24.10.2016.
 */

public interface IObject {
    static final short DIR_UP = 0;
    static final short DIR_DOWN = 1;
    static final short DIR_LEFT = 2;
    static final short DIR_RIGHT = 3;

    void render();
    void renderDepthOnly();
    String getName();

    float getX();
    void setX(float x);
    float getY();
    void setY(float y);
    float getZ();
    void setZ(float z);
    float getSizeX();
    float getSizeY();
}
