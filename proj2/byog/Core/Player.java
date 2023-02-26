package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class Player implements Serializable {
    private static Position pos;
    public static void setPos(Position Pos) {
        Player.pos = Pos;
    }

    public static Position getPos() {
        return pos;
    }
    private static int x() {
        return pos.x;
    }

    private static int y() {
        return pos.y;
    }

    public static void moveUp(TETile[][] map) {
        Position newPos = new Position(x(), y() + 1);
        if (move(map, newPos)) pos = newPos;
    }

    public static void moveDown(TETile[][] map) {
        Position newPos = new Position(x(), y() - 1);
        if (move(map, newPos)) pos = newPos;
    }

    public static void moveLeft(TETile[][] map) {
        Position newPos = new Position(x() - 1, y());
        if (move(map, newPos)) pos = newPos;
    }

    public static void moveRight(TETile[][] map) {
        Position newPos = new Position(x() + 1, y());
        if (move(map, newPos)) pos = newPos;
    }

    public static boolean isWin(TETile[][] map) {
        return pos.getTile(map) == Tileset.LOCKED_DOOR;
    }

    private static boolean move(TETile[][] map, Position newPos) {
        if (newPos.getTile(map) == Tileset.FLOOR) {
            newPos.setTile(map, Tileset.PLAYER);
            pos.setTile(map, Tileset.FLOOR);
            return true;
        }
        if (newPos.getTile(map) == Tileset.LOCKED_DOOR) {
            pos = newPos;
            return true;
        }
        return false;
    }

}
