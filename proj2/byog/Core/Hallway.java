package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.LinkedList;
import java.util.Random;

public class Hallway {
    public static void initializeHallWay(TETile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != Tileset.FLOWER && map[i][j] != Tileset.GRASS) map[i][j] = Tileset.WALL;
            }
        }
        for (int i = 1; i < map.length - 1; i += 2) {
            for (int j = 1; j < map[0].length - 1; j += 2) {
                if (map[i][j] != Tileset.FLOWER && map[i][j] != Tileset.GRASS) map[i][j] = Tileset.NOTHING;
            }
        }
    }

    public static void generateHallway(TETile[][] map, Position start, long seed) {
        LinkedList<Position> positionList = new LinkedList<>();
        positionList.add(start);
        Random random = new Random(seed);
        while(!positionList.isEmpty()) {
            int index = random.nextInt(positionList.size());
            Position curPos = positionList.get(index);
            map[curPos.x][curPos.y] = Tileset.FLOOR;
            getAvaliableAndConnect(map, positionList, curPos, seed);
            positionList.remove(index);
        }
    }

    private static void getAvaliableAndConnect(TETile[][] map, LinkedList<Position> positionList, Position curPos, long seed) {
        int[][] Direction = {
                {2, 0},
                {0, -2},
                {-2, 0},
                {0, 2}
        };
        Random random = new Random(seed);
        boolean flagConnect = false;
        boolean[] went = new boolean[4];
        while((went[0] && went[1] && went[2] && went[3]) == false) {
            int rand = random.nextInt(4);
            if (went[rand]) continue;
            went[rand] = true;
            int[] goDirection = Direction[rand];
            int newx = curPos.x + goDirection[0];
            int newy = curPos.y + goDirection[1];
            if (newx < 0 || newx > map.length - 1 || newy < 0 || newy > map[0].length - 1) {
                continue;
            }
            if (map[newx][newy] == Tileset.NOTHING) {
                map[newx][newy] = Tileset.FLOWER;
                 positionList.add(new Position(newx, newy));
            }
            if (map[newx][newy] == Tileset.FLOOR && !flagConnect) {
                int tmpx = (curPos.x + newx) / 2;
                int tmpy = (curPos.y + newy) / 2;
                map[tmpx][tmpy] = Tileset.FLOOR;
                flagConnect = true;
            }
        }
    }
}
