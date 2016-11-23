package lab.mg.tanks3d.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kostya on 02.11.2016.
 */

public class TankTower extends RenderableObject {
    private List<CubeObject> cubes = new ArrayList<>();
    private int tankSizeInCubes;

    public TankTower(float x, float y, float z, String objName, int tankSizeInCubes, int texture) {
        super(x, y, z, objName, false);
        this.tankSizeInCubes = tankSizeInCubes;

        cubes.add(new CubeObject(
                getX(),
                getY() + CubeObject.DEFAULT_SIZE,
                getZ(),
                "TowerCube" + 0 + "_z0",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX(),
                getY() + CubeObject.DEFAULT_SIZE * 2,
                getZ(),
                "TowerCube" + 0 + "_z0",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX() + CubeObject.DEFAULT_SIZE,
                getY() + CubeObject.DEFAULT_SIZE,
                getZ(),
                "TowerCube" + 0 + "_z0",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX() + CubeObject.DEFAULT_SIZE,
                getY() + CubeObject.DEFAULT_SIZE * 2,
                getZ(),
                "TowerCube" + 0 + "_z0",
                false,
                CubeObject.DEFAULT_SIZE, texture));

        cubes.add(new CubeObject(
                getX(),
                getY() + CubeObject.DEFAULT_SIZE,
                getZ() + CubeObject.DEFAULT_SIZE,
                "TowerCube" + 0 + "_z1",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX(),
                getY() + CubeObject.DEFAULT_SIZE * 2,
                getZ() + CubeObject.DEFAULT_SIZE,
                "TowerCube" + 0 + "_z1",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX() + CubeObject.DEFAULT_SIZE,
                getY() + CubeObject.DEFAULT_SIZE,
                getZ() + CubeObject.DEFAULT_SIZE,
                "TowerCube" + 0 + "_z1",
                false,
                CubeObject.DEFAULT_SIZE, texture));
        cubes.add(new CubeObject(
                getX() + CubeObject.DEFAULT_SIZE,
                getY() + CubeObject.DEFAULT_SIZE * 2,
                getZ() + CubeObject.DEFAULT_SIZE,
                "TowerCube" + 0 + "_z1",
                false,
                CubeObject.DEFAULT_SIZE, texture));
    }

    @Override
    public void render() {
        for (CubeObject object : cubes) {
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
        for (CubeObject cubeObject : cubes) {
            cubeObject.rotateAroundPointByZAxis(getX(), getY(), angle);
        }
    }

    public void rotateAroundYourselfByXAxis(float angle) {
        for (CubeObject cubeObject : cubes) {
            cubeObject.rotateAroundPointByXAxis(
                    getX() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    getZ() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundYourselfByYAxis(float angle) {
        for (CubeObject cubeObject : cubes) {
            cubeObject.rotateAroundPointByYAxis(
                    getZ() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    getY() + tankSizeInCubes * CubeObject.DEFAULT_SIZE / 2,
                    angle);
        }
    }

    public void rotateAroundXYByZAxis(float x, float y, float angle) {
        for (CubeObject cubeObject : cubes) {
            cubeObject.rotateAroundPointByZAxis(x, y, angle);
        }
    }

    public void changeCoords(float xOffset, float yOffset) {
        setX(getX() + xOffset);
        setY(getY() + yOffset);
    }

    public void moveUp(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveUp(offset, x, y);
        }
        setY(getY() + offset);
    }

    public void moveDown(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveDown(offset, x, y);
        }
        setY(getY() - offset);
    }

    public void moveLeft(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveLeft(offset, x, y);
        }
        setX(getX() - offset);
    }

    public void moveRight(float offset, float x, float y) {
        for (CubeObject cubeObject: cubes) {
            cubeObject.moveRight(offset, x, y);
        }
        setX(getX() + offset);
    }

    @Override
    public float getSizeX() {
        return CubeObject.DEFAULT_SIZE * 2;
    }

    @Override
    public float getSizeY() {
        return CubeObject.DEFAULT_SIZE * 2;
    }
}
