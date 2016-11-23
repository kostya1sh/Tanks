package lab.mg.tanks3d.objects;

import lab.mg.tanks3d.containers.ObjectKeeper;

/**
 * Created by kostya on 28.10.2016.
 */

public class Tank extends RenderableObject {
    private int size;
    private float speed = 0.5f;
    private TankCaterpillar leftCaterpillar;
    private TankCaterpillar rightCaterpillar;
    private TankBarrel barrel;
    private TankTower tower;
    private int projectileTexture;
    private int currentDir = IObject.DIR_UP;

    public Tank(float x, float y, float z, String objName, int sizeInCubes, int texture, int projectileTexture) {
        super(x, y, z, objName, true);
        this.size = sizeInCubes;
        this.projectileTexture = projectileTexture;
        tower = new TankTower(x + CubeObject.DEFAULT_SIZE, y, z, objName + "_tower", size, texture);
        barrel = new TankBarrel(x + CubeObject.DEFAULT_SIZE + CubeObject.DEFAULT_SIZE / 2 + CubeObject.DEFAULT_SIZE / 4, y, z, objName + "_barrel", size, texture);
        leftCaterpillar = new TankCaterpillar(x, y, z, objName + "_tank_lcr", size, texture);
        rightCaterpillar = new TankCaterpillar(x + (3 * CubeObject.DEFAULT_SIZE), y, z, objName + "_tank_rcr", size, texture);
    }

    @Override
    public void render() {
        tower.render();
        barrel.render();
        leftCaterpillar.render();
        rightCaterpillar.render();
    }

    @Override
    public void renderDepthOnly() {
        tower.renderDepthOnly();
        barrel.renderDepthOnly();
        leftCaterpillar.renderDepthOnly();
        rightCaterpillar.renderDepthOnly();
    }

    public void changeCoords(float xOffset, float yOffset) {
        setX(getX() + xOffset);
        setY(getY() + yOffset);
        tower.changeCoords(xOffset, yOffset);
        barrel.changeCoords(xOffset, yOffset);
        leftCaterpillar.changeCoords(xOffset, yOffset);
        rightCaterpillar.changeCoords(xOffset, yOffset);
    }

    private boolean checkTankCollision(IObject tank) {

        for (IObject checkObj : ObjectKeeper.getInstance().getAllObjects()) {
            if (checkObj.equals(tank)) {
                continue;
            }

            // Collision x-axis?
            boolean collisionXRight = tank.getX() + tank.getSizeX() > checkObj.getX();
            boolean collisionXLeft = checkObj.getX() + checkObj.getSizeX() > tank.getX();

            // Collision y-axis?
            boolean collisionYTop = tank.getY() + tank.getSizeY() > checkObj.getY();
            boolean collisionYBottom = checkObj.getY() + checkObj.getSizeY() > tank.getY();

            // Collision only if on both axes
            if (collisionXRight && collisionXLeft && collisionYTop && collisionYBottom) {
                return true;
            }
        }

        if (tank.getX() > 6 * 4.0f - 4.0f || tank.getX() < 0.0f
                || tank.getY() > 6 * 4.0f - 4.0f || tank.getY() < 0.0f) {
            return true;
        }

        return false;
    }

    public void moveUp() {
        setY(getY() + speed);
        if (checkTankCollision(this)) {
            setY(getY() - speed);
            return;
        }
        tower.moveUp(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        barrel.moveUp(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        leftCaterpillar.moveUp(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        rightCaterpillar.moveUp(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        currentDir = IObject.DIR_UP;
    }

    public void moveDown() {
        setY(getY() - speed);
        if (checkTankCollision(this)) {
            setY(getY() + speed);
            return;
        }
        tower.moveDown(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        barrel.moveDown(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        leftCaterpillar.moveDown(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        rightCaterpillar.moveDown(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        currentDir = IObject.DIR_DOWN;
    }

    public void moveLeft() {
        setX(getX() - speed);
        if (checkTankCollision(this)) {
            setX(getX() + speed);
            return;
        }
        tower.moveLeft(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        barrel.moveLeft(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        leftCaterpillar.moveLeft(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        rightCaterpillar.moveLeft(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        currentDir = IObject.DIR_LEFT;
    }

    public void moveRight() {
        setX(getX() + speed);
        if (checkTankCollision(this)) {
            setX(getX() - speed);
            return;
        }
        tower.moveRight(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        barrel.moveRight(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        leftCaterpillar.moveRight(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        rightCaterpillar.moveRight(speed, getX() + CubeObject.DEFAULT_SIZE * (size / 2), getY() + CubeObject.DEFAULT_SIZE * (size / 2));
        currentDir = IObject.DIR_RIGHT;
    }

    public void fire() {
        switch (currentDir) {
            case DIR_UP:
                new Projectile(
                        getX() + CubeObject.DEFAULT_SIZE * size / 2 - 0.15f,
                        getY() + CubeObject.DEFAULT_SIZE * size + 0.15f,
                        2.0f,
                        "projectile_" + getName(), true, IObject.DIR_UP, projectileTexture);
                break;
            case DIR_DOWN:
                new Projectile(
                        getX() + CubeObject.DEFAULT_SIZE * size / 2 - 0.15f,
                        getY() - 0.15f,
                        2.0f,
                        "projectile_" + getName(), true, IObject.DIR_DOWN, projectileTexture);
                break;

            case DIR_LEFT:
                new Projectile(
                        getX(),
                        getY() + CubeObject.DEFAULT_SIZE * size / 2 + 0.15f,
                        2.0f,
                        "projectile_" + getName(), true, IObject.DIR_LEFT, projectileTexture);
                break;
            case DIR_RIGHT:
                new Projectile(
                        getX() + CubeObject.DEFAULT_SIZE * size,
                        getY() + CubeObject.DEFAULT_SIZE * size / 2,
                        2.0f,
                        "projectile_" + getName(), true, IObject.DIR_RIGHT, projectileTexture);
                break;
            default: break;
        }

    }

    @Override
    public float getSizeX() {
        return CubeObject.DEFAULT_SIZE * size;
    }

    @Override
    public float getSizeY() {
        return CubeObject.DEFAULT_SIZE * size;
    }

    public int getCurrentDir() {
        return currentDir;
    }

    public void setCurrentDir(int currentDir) {
        this.currentDir = currentDir;
    }
}
