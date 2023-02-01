package byog.Core;

import java.util.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
public class Room {
    private int x;
    private int y;
    private int width;
    private int height;

    public static Room[] generateRoom(int width, int height, int roomNum, int seed) {
        Random rand = new Random(seed);
        Room[] rooms = new Room[roomNum];
        int curTime = 0;
        int pos = 0;
        while (curTime < roomNum) {
            int rwidth = rand.nextInt(width / 10 + 2);
            int rheight = rand.nextInt(height / 5 + 2);
            int rx = rand.nextInt(width);
            int ry = rand.nextInt(height);
            Room r = new Room(rx, ry, rwidth, rheight);
            if (ry + height + 1 >= height || rx + width + 1 >= width) {
                continue;
            }
            if (!r.checkOverlap(rooms)) {
                continue;
            }
            rooms[pos++] = r;
            curTime++;
        }
        return rooms;
    }

    public static void draw(TETile[][] map, Room room) {
        for (int i = room.x; i < room.x + room.width; i++) {
            for (int j = room.y; j < room.y + room.height; j++) {
                map[i][j] = Tileset.GRASS;
            }
        }
    }

    public int[] data() {
        int[] res = {x, y, height, width};
        return res;
    }

   public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
   }

   public boolean isOverlap(Room other) {
        int diffx = other.x - this.x;
        int diffy = other.y - this.y;
        int thisxx = this.x + this.width;
        int thisyy = this.y + this.height;
        int otherxx = other.x + other.width;
        int otheryy = other.y + other.height;
        if (diffx > 0 && diffy > 0) {
            // other is on the right and above of this
            if (thisxx > otherxx && thisyy > otheryy) {
                return false;
                // is overlap
            }
        }
        if (diffx <= 0 && diffy <= 0) {
            // other is on the left below of this
            if (otherxx >= thisxx && otheryy >= thisyy) {
                return false;
            }
        }
        if (diffx <= 0 && diffy >= 0) {
            if (thisyy >= other.y && thisxx <= other.x) {
                return false;
            }
        }
        if (diffx >= 0 && diffy <= 0) {
            if (this.y <= otheryy && this.x >= otherxx) {
                return false;
            }
        }
        return true;
   }

   public boolean checkOverlap(Room[] rooms) {
        for (Room room : rooms) {
            if (room == this || room == null) {
                continue;
            }
            if (!isOverlap(room)) {
                return true;
            }
        }
        return false;
   }

}
