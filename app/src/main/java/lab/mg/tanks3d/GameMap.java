package lab.mg.tanks3d;

import java.util.ArrayList;
import java.util.List;

import lab.mg.tanks3d.objects.Box;
import lab.mg.tanks3d.objects.Ground;
import lab.mg.tanks3d.objects.IObject;

/**
 * Created by kostya on 24.10.2016.
 */

public class GameMap {
    private static GameMap currentInstance = new GameMap();
    private int[][] map;
    private int mapSizeInBoxes;
    private float realMapSize;
    private float cubeSize;
    private int boxSizeInCubes;
    private List<IObject> mapObjects = new ArrayList<>();

    public static GameMap getInstance() { return currentInstance; }

    public void create(float cubeSize, int boxSizeInCubes, int[][] map, int groundTexture, int boxTexture) {
        checkMapSize(map);

        this.map = map;
        this.cubeSize = cubeSize;
        this.mapSizeInBoxes = map.length;
        this.boxSizeInCubes = boxSizeInCubes;
        this.realMapSize = map.length * cubeSize * boxSizeInCubes;

        new Ground(0.0f, 0.0f, 0.99999f, "ground01", realMapSize, groundTexture);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 1) {
                    mapObjects.add(new Box(j * cubeSize * boxSizeInCubes, (mapSizeInBoxes - i - 1) * cubeSize * boxSizeInCubes,
                            1.0f, "mapCube" + i + "" + j, i + j, boxTexture));
                }
            }
        }
    }

    private void checkMapSize(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            if (map[i].length != map.length) {
                throw new IllegalArgumentException("Map width must be same as map length!");
            }
        }
    }

    private GameMap() {}
}
