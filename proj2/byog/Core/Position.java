package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 3738338544041213556L;
    public int x;
    public int y;
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public static Position[] findAdjacent(int x, int y) {
        int[][] Direction = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1 ,0}
        };
        Position pos = new Position(x, y);
        Position[] res = new Position[4];
        for (int i = 0; i < 4; i++) {
            res[i] = new Position(pos.x + Direction[i][0], pos.y + Direction[i][1]);
        }
        return res;
    }
    public static Position[] findAdjacent(Position pos) {
        int[][] Direction = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1 ,0}
        };
        Position[] res = new Position[4];
        for (int i = 0; i < 4; i++) {
            res[i] = new Position(pos.x + Direction[i][0], pos.y + Direction[i][1]);
        }
        return res;
    }

    public static boolean checkBarrier(TETile[][] map, Position pos) {
        if (map[pos.x][pos.x] == Tileset.WALL) return true;
        return false;
    }

    public TETile getTile(TETile[][] map) {
        return map[this.x][this.y];
    }

    public static TETile getTile(TETile[][] map, int x, int y) {
        return map[x][y];
    }

    public void setTile(TETile[][] map, TETile tile) {
        map[this.x][this.y] = tile;
    }
    private void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private Position[] findAdjacd() {
        int[][] Direction = {
                {0, 1},
                {1, 0},
                {0, -1},
                {-1 ,0}
        };
        Position[] res = new Position[4];
        for (int i = 0; i < 4; i++) {
            res[i] = new Position(this.x + Direction[i][0], this.y + Direction[i][1]);
        }
        return res;
    }

    public static boolean isEdge(TETile[][] map, int x, int y) {
        if (Position.getTile(map, x, y) == Tileset.WALL) {
            for (Position pos : Position.findAdjacent(x, y)) {
                if (pos.getTile(map) == Tileset.FLOOR) return true;
            }
        }
        return false;
    }
}
