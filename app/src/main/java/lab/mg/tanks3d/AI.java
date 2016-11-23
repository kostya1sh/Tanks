package lab.mg.tanks3d;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import lab.mg.tanks3d.containers.ObjectKeeper;
import lab.mg.tanks3d.objects.IObject;
import lab.mg.tanks3d.objects.Tank;

/**
 * Created by kostya on 03.11.2016.
 */

public class AI {
    private static AI currentInstance = new AI();
    private CopyOnWriteArrayList<Tank> tanks = new CopyOnWriteArrayList<>();
    private long lastActionTime = 0;
    private static float ATTACK_RANGE = 8.0f;

    private AI(){ /*empty*/ };

    public static synchronized AI getInstance() {
        return currentInstance;
    }

    public void addTank(Tank tank) {
        this.tanks.add(tank);
    }

    public void removeTank(Tank tank) {
        this.tanks.remove(tank);
    }

    public void performAction() {
        Tank playerTank = (Tank) ObjectKeeper.getInstance().getObjectByName("player_tank");
        if (playerTank != null) {
            if (System.currentTimeMillis() >= lastActionTime + 250) {
                lastActionTime = System.currentTimeMillis();
                Random random = new Random();
                for (Tank tank : tanks) {
                    int randDir = 0;
                    if (playerTank.getX() < tank.getX() && playerTank.getY() < tank.getY()) {
                        randDir = random.nextInt(2);
                        switch (randDir) {
                            case 0:
                                tank.moveDown();
                                break;
                            case 1:
                                tank.moveLeft();
                                break;
                        }
                    }
                    if (playerTank.getX() > tank.getX() && playerTank.getY() < tank.getY()) {
                        randDir = random.nextInt(2);
                        switch (randDir) {
                            case 0:
                                tank.moveDown();
                                break;
                            case 1:
                                tank.moveRight();
                                break;
                        }
                    }
                    if (playerTank.getX() < tank.getX() && playerTank.getY() > tank.getY()) {
                        randDir = random.nextInt(2);
                        switch (randDir) {
                            case 0:
                                tank.moveUp();
                                break;
                            case 1:
                                tank.moveLeft();
                                break;
                        }
                    }
                    if (playerTank.getX() > tank.getX() && playerTank.getY() > tank.getY()) {
                        randDir = random.nextInt(2);
                        switch (randDir) {
                            case 0:
                                tank.moveUp();
                                break;
                            case 1:
                                tank.moveRight();
                                break;
                        }
                    }
                    if (playerTank.getX() == tank.getX() && playerTank.getY() > tank.getY()) {
                        randDir = random.nextInt(1);
                        switch (randDir) {
                            case 0:
                                tank.moveUp();
                                break;
                        }
                    }
                    if (playerTank.getX() == tank.getX() && playerTank.getY() < tank.getY()) {
                        randDir = random.nextInt(1);
                        switch (randDir) {
                            case 0:
                                tank.moveDown();
                                break;
                        }
                    }
                    if (playerTank.getX() > tank.getX() && playerTank.getY() == tank.getY()) {
                        randDir = random.nextInt(1);
                        switch (randDir) {
                            case 0:
                                tank.moveRight();
                                break;
                        }
                    }
                    if (playerTank.getX() < tank.getX() && playerTank.getY() == tank.getY()) {
                        randDir = random.nextInt(1);
                        switch (randDir) {
                            case 0:
                                tank.moveLeft();
                                break;
                        }
                    }

                    randDir = random.nextInt(20);
                    if (randDir > 17) {
                        tank.fire();
                    }
                }
            }
        }
    }

    public CopyOnWriteArrayList<Tank> getTanks() {
        return this.tanks;
    }

    public void setTanks(CopyOnWriteArrayList<Tank> tanks) {
        this.tanks = tanks;
    }
}
