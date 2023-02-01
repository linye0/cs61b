package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.*;
public class World {
    public TETile[][] map;
    private Room[] rooms;
    private int width;
    private int height;
    private int seed;
    private int generateTime;

    public World(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        this.seed = seed;
        map = new TETile[width][height];
        Random rand = new Random(seed);
        this.generateTime = 100;
        rooms = Room.generateRoom(width, height, generateTime, seed);
    }

    public void show() {
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        mapInitialize();
        drawRoom();
        drawHallway();
        ter.renderFrame(map);
    }
    public TETile[][] map() {
        return map;
    }

    private void mapInitialize() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }
    }
    private void drawRoom() {
        for (Room room : rooms) {
            Room.draw(map, room);
        }
    }

    private void drawHallway() {
        Hallway.draw(map, rooms);
    }
}
