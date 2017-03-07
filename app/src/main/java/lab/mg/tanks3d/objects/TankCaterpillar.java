package lab.mg.tanks3d.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 02.11.2016.
 */

public class TankCaterpillar extends RenderableObject {
    private List<CubeObject> cubes = new ArrayList<>();
    private int tankSizeInCubes;

    public TankCaterpillar(float x, float y, float z, String objName, int tankSizeInCubes, int texture) {
        super(x, y, z, objName, false);
        this.tankSizeInCubes = tankSizeInCubes;
        for (int i = 0; i < tankSizeInCubes; i++) {
            cubes.add(new CubeObject(
                    getX(),
                    getY() + i * CubeObject.DEFAULT_SIZE,
                    getZ(),
                    "CaterpillarCube" + i + "_z0",
                    false,
                    CubeObject.DEFAULT_SIZE, texture));
        }
    }

    @Override
    public void render() {
        for (CubeObject object: cubes) {
            object.render();
        }
    }

    @Override
    public void renderDepthOnly() {
        for (CubeObject object: cubes) {
            object.renderDepthOnly();
        }
    }

    public void rotateAroundXY(float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByZAxis(getX(), getY(), angle);
        }
    }

    public void rotateAroundYourselfByXAxis(float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByXAxis(
                    getX() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    getZ() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundYourselfByYAxis(float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByYAxis(
                    getZ() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    getY() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundXYByZAxis(float x, float y, float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByZAxis(x, y, angle);
        }
    }

    public synchronized void changeCoords(float xOffset, float yOffset) {
        setX(getX() + xOffset);
        setY(getY() + yOffset);
    }

    public synchronized void moveUp(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveUp(offset, x, y);
        }
        setY(getY() + offset);
    }

    public synchronized void moveDown(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveDown(offset, x, y);
        }
        setY(getY() - offset);
    }

    public synchronized void moveLeft(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveLeft(offset, x, y);
        }
        setX(getX() - offset);
    }

    public synchronized void moveRight(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveRight(offset, x, y);
        }
        setX(getX() + offset);
    }

    @Override
    public float getSizeX() {
        return CubeObject.DEFAULT_SIZE;
    }

    @Override
    public float getSizeY() {
        return CubeObject.DEFAULT_SIZE * tankSizeInCubes;
    }
}
