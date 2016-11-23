package lab.mg.tanks3d.objects;

import java.util.ArrayList;
import java.util.List;

import lab.mg.tanks3d.containers.ObjectKeeper;

import static android.opengl.GLES20.GL_TEXTURE1;

/**
 * Created by kostya on 26.10.2016.
 */

public class Box extends RenderableObject {
    private List<CubeObject> cubes = new ArrayList<>();
    private int mapPos;

    public Box(float x, float y, float z, String objName, int mapPos, int texture) {
        super(x, y, z, objName, true);
        this.mapPos = mapPos;

        /*for (int i = 0; i < sizeInCubes; i++) {
            for (int j = 0; j < sizeInCubes; j++) {
                cubes.add(new CubeObject(
                        getX() + i * CubeObject.DEFAULT_SIZE,
                        getY() + j * CubeObject.DEFAULT_SIZE,
                        getZ(),
                        "Cube" + i + j + "_z0",
                        false,
                        CubeObject.DEFAULT_SIZE, texture));
                cubes.add(new CubeObject(
                        getX() + i * CubeObject.DEFAULT_SIZE,
                        getY() + j * CubeObject.DEFAULT_SIZE,
                        getZ() + CubeObject.DEFAULT_SIZE,
                        "Cube" + i + j + "_z1",
                        false,
                        CubeObject.DEFAULT_SIZE, texture));
                cubes.add(new CubeObject(
                        getX() + i * CubeObject.DEFAULT_SIZE,
                        getY() + j * CubeObject.DEFAULT_SIZE,
                        getZ() + CubeObject.DEFAULT_SIZE,
                        "Cube" + i + j + "_z2",
                        false,
                        CubeObject.DEFAULT_SIZE, texture));
            }
        }*/

        cubes.add(new CubeObject(
                getX(),
                getY(),
                getZ(),
                "Cube_box",
                false,
                CubeObject.DEFAULT_SIZE * 4, texture));

    }

    @Override
    public void render() {
        for (CubeObject cubeObject: cubes) {
            cubeObject.render();
        }
    }

    @Override
    public void renderDepthOnly() {
        for (CubeObject cubeObject: cubes) {
            cubeObject.renderDepthOnly();
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
                    getX() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    getZ() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundYourselfByYAxis(float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByYAxis(
                    getZ() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    getY() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundYourselfByZAxis(float angle) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.rotateAroundPointByZAxis(
                    getX() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    getY() + 4 * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    @Override
    public float getSizeX() {
        return CubeObject.DEFAULT_SIZE * 4;
    }

    @Override
    public float getSizeY() {
        return CubeObject.DEFAULT_SIZE * 4;
    }
}
