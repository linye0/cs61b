package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import com.sun.xml.internal.ws.util.UtilException;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class World implements Serializable {
    public static TETile[][] map;
    private static ArrayList<Room> Rooms;
    private int WIDTH;
    private int HEIGHT;
    private long SEED;
    private static final int TIME = 100;
    private Random RANDOM;

    public World(int width, int height, long seed) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.SEED = seed;
        this.RANDOM = new Random(seed);
        Rooms = new ArrayList<Room>();
        map = new TETile[width + 1][height + 1];
        initialize();
    }
    private void initialize() {
        fillWithNothing();
        fillWithRooms();
        fillWithMaze();
        breakAllWalls();
        repairFloorAndWall();
        removeDeedends();
        removeConcreteWalls();
        setPlayerAndDoor();
    }
    private void fillWithMaze() {
        Hallway.initializeHallWay(map);
        Position startPos;
        while (true) {
            startPos = new Position(RANDOM.nextInt((map.length - 3) / 2) * 2 + 1, RANDOM.nextInt((map[0].length - 3) / 2) * 2 + 1);
            if (map[startPos.x][startPos.y] == Tileset.FLOWER || map[startPos.x][startPos.y] == Tileset.GRASS) continue;
            break;
        }
        Hallway.generateHallway(map, startPos, SEED);
    }
    private void fillWithNothing() {
       for (int i = 0; i <= WIDTH; i++) {
           for (int j = 0; j <= HEIGHT; j++) {
               map[i][j] = Tileset.NOTHING;
           }
       }
    }

    private void fillWithRooms() {
        for (int i = 0; i < TIME; i++) {
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            int width = RANDOM.nextInt(WIDTH / 10) + 2;
            int height = RANDOM.nextInt(HEIGHT / 5) + 2;
            if (y + height + 1 > HEIGHT || x + width + 1 > WIDTH || isOverlap(x, y, width, height)) {
                continue;
            }
            buildRoom(x, y, width, height);
            Rooms.add(new Room(x, y, width, height));
        }
    }

    private void buildRoom(int x, int y, int width, int height) {
        // buildWall
        for (int i = x; i <= x + width + 1; i++) {
            map[i][y] = Tileset.FLOWER;
            map[i][y + height + 1] = Tileset.FLOWER;
        }
        for (int j = y + 1; j <= y + height; j++) {
            map[x][j] = Tileset.FLOWER;
            map[x + width + 1][j] = Tileset.FLOWER;
        }
        for (int i = x + 1; i <= x + width; i++) {
            for (int j = y + 1; j <= y + height; j++) {
                map[i][j] = Tileset.GRASS;
            }
        }
    }

    private void setPlayerAndDoor() {
        int step = 0;
        while(true) {
            int x = RANDOM.nextInt(WIDTH + 1);
            int y = RANDOM.nextInt(HEIGHT + 1);
            if (step == 0 && Position.isEdge(map, x, y)) {
                map[x][y] = Tileset.LOCKED_DOOR;
                step++;
            }
            if (step == 1 && map[x][y] == Tileset.FLOOR) {
                Position startPlayerPos = new Position(x, y);
                Player.setPos(startPlayerPos);
                map[x][y] = Tileset.PLAYER;
                break;
            }
        }
    }

    private boolean isOverlap (int x, int y, int width, int height) {
        for (int i = x; i <= x + width + 1; i++) {
            for (int j = y; j <= y + height + 1; j++) {
                if (map[i][j] == Tileset.GRASS || map[i][j] == Tileset.FLOWER) {
                    return true;
                }
            }
        }
        return false;
    }

    private void breakAllWalls() {
        for (Room room : Rooms) {
            room.breakWalls(map, RANDOM);
        }
    }

    private void repairFloorAndWall() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == Tileset.GRASS) map[i][j] = Tileset.FLOOR;
                if (map[i][j] == Tileset.FLOWER) map[i][j] = Tileset.WALL;
            }
        }
    }

    private void removeDeedends() {
        // Use DFS to search for deadends, boolmap is used to mark a position has been detected or not.
        boolean[][] boolMap = new boolean[WIDTH + 1][HEIGHT + 1];
        Position startPos = null;
        boolean flagFindStart = false;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (!flagFindStart && map[i][j] == Tileset.FLOOR) {
                    int wallCnt = 0;
                    boolean noWall = true;
                    Position curPos = new Position(i, j);
                    for (Position pos : Position.findAdjacent(curPos)) {
                        if (map[pos.x][pos.y] == Tileset.WALL) {
                            noWall = false;
                            break;
                        }
                    }
                    if (!noWall) continue;
                    flagFindStart = true;
                    startPos = new Position(i, j);
                }
                if (map[i][j] == Tileset.WALL) {
                    boolMap[i][j] = true;
                }
            }
        }
        startPos = new Position(Rooms.get(0).x + 1, Rooms.get(0).y + 1);
        removeDeedendsDFS(boolMap, startPos);
    }

    private boolean removeDeedendsDFS(boolean[][] boolMap, Position startPos) {
        // search forward
        int wallCnt = 0;
        boolMap[startPos.x][startPos.y] = true;
        for (Position pos : Position.findAdjacent(startPos)) {
            if (pos.x > WIDTH || pos.x < 0 || pos.y > HEIGHT || pos.y < 0 || map[pos.x][pos.y] == Tileset.WALL) {
                wallCnt++;
            }
        }
        if (wallCnt >= 3) map[startPos.x][startPos.y] = Tileset.WALL;
        // check backward
        for (Position pos : Position.findAdjacent(startPos)) {
            if (pos.x < 0 || pos.x >= WIDTH || pos.y >= HEIGHT || pos.y < 0) {
                continue;
            }
            if (!boolMap[pos.x][pos.y]) {
                if (removeDeedendsDFS(boolMap, pos)) wallCnt += 1;
                boolMap[pos.x][pos.y] = true;
                if (wallCnt >= 3) map[startPos.x][startPos.y] = Tileset.WALL;
            }
        }
        return wallCnt >= 3;
    }

    private boolean checkDeadends(Position pos) {
        int[][] Direction = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1 ,0}
        };
        int[] flagDeadends = new int[4];
        for (int i = 0; i < 4; i++) {
            int newx = pos.x + Direction[i][0];
            int newy = pos.y + Direction[i][1];
            if (newx >= WIDTH || newx < 0 || newy >= HEIGHT || newy < 0) {
                continue;
            }
            if (map[newx][newy] == Tileset.WALL) {
                flagDeadends[i] = 1;
            }
        }
        int sum = IntStream.of(flagDeadends).sum();
        if (sum >= 3) return true;
        else return false;
    }

    private void removeConcreteWalls() {
        boolean[][] boolConcreteWall = new boolean[WIDTH + 1][HEIGHT + 1];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Position curPos = new Position(i, j);
                if (map[i][j] == Tileset.WALL && checkConcreteWall(curPos)) {
                    boolConcreteWall[curPos.x][curPos.y] = true;
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (boolConcreteWall[i][j]) {
                    map[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    private boolean checkConcreteWall(Position curPos) {
        int[][] Direction = {
                {1, 1},
                {1, -1},
                {-1, -1},
                {-1, 1}
        };
        boolean flagFloor = true;
        for (int i = 0; i < 4; i++) {
            int newx = curPos.x + Direction[i][0];
            int newy = curPos.y + Direction[i][1];
            if (newx > WIDTH || newx < 0 || newy > HEIGHT || newy < 0) {
                continue;
            }
            if (map[newx][newy] == Tileset.FLOOR) flagFloor = false;
        }
        return flagFloor;
    }

    public TETile[][] map() {
        return map;
    }
}