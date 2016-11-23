package lab.mg.tanks3d;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import lab.mg.tanks3d.objects.CubeObject;
import lab.mg.tanks3d.objects.Tank;

/**
 * Created by kostya on 02.11.2016.
 */

public class Control implements View.OnTouchListener {
    private static Control currentInstance = new Control();
    private Tank tank;

    public static Control getInstance() {
        return currentInstance;
    }

    public void setControllableTank(Tank tank) {
        this.tank = tank;
    }

    public Tank getControllableTank() { return this.tank; };

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (tank == null) {
            return false;
        }

        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float screenWidth = MainActivity.screenWidth;
        float screenHeight = MainActivity.screenHeight;

        // down
        if (y >= screenHeight - (screenHeight / 4)) {
            tank.moveDown();
            Log.d("PlayerTank", "x = " + tank.getX() + " y = " + tank.getY());
            return false;
        }

        // up
        if (y <= screenHeight / 4) {
            tank.moveUp();
            Log.d("PlayerTank", "x = " + tank.getX() + " y = " + tank.getY());
            return false;
        }

        // right
        if (x >= screenWidth - (screenWidth / 5)) {
            tank.moveRight();
            Log.d("PlayerTank", "x = " + tank.getX() + " y = " + tank.getY());
            return false;
        }

        // left
        if (x <= screenWidth / 5) {
            tank.moveLeft();
            Log.d("PlayerTank", "x = " + tank.getX() + " y = " + tank.getY());
            return false;
        }

        // tap on center to fire
        if (x >= 760 && x <= 1160 && y >= 340 && y <= 740) {
            tank.fire();
            Log.d("PlayerTank", "fired! dir = " + tank.getCurrentDir());
            return false;
        }

        return false;
    }
}
