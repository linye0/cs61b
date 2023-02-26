package byog.Core;

import java.util.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
public class Room {
    public int x;
    public int y;
    public int WIDTH;
    public int HEIGHT;
    public Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    public void breakWalls(TETile[][] map, Random RANDOM) {
        int[][] Direction = {
                {2, 0},
                {0, -2},
                {-2, 0},
                {0, 2}
        };
        boolean done = false;
        boolean[] gone = new boolean[4];
        int totalWidth = map.length;
        int totalHeight = map[0].length;
        int depth = 0;
        while (!done) {
            int rand = RANDOM.nextInt(4);
            int[] direction = Direction[rand];

            if (rand == 0 && !gone[rand]) {// right
                gone[rand] = true;
                int x = this.x + this.WIDTH;
                int nextx = x + direction[0] + depth;
                if (nextx >= totalWidth) continue;
                for (int y = this.y + 1; y <= this.y + this.HEIGHT; y++) {
                    if (map[nextx][y] == Tileset.FLOOR) {
                        for (int digx = x; digx < nextx; digx++){
                            map[digx][y] = Tileset.GRASS;
                        }
                        done = true;
                        break;
                    }
                }
            }

            if (rand == 2 && !gone[rand]) { // left
                gone[rand] = true;
                int x = this.x + 1;
                int nextx = x + direction[0] - depth;
                if (nextx < 0) continue;
                for (int y = this.y + 1; y <= this.y + this.HEIGHT; y++) {
                    if (map[nextx][y] == Tileset.FLOOR) {
                        for (int digx = x; digx > nextx; digx--){
                            map[digx][y] = Tileset.GRASS;
                        }
                        done = true;
                        break;
                    }
                }
            }

            if (rand == 1 && !gone[rand]) { // down
                gone[rand] = true;
                int y = this.y + 1;
                int nexty = y + direction[1] - depth;
                if (nexty < 0) continue;
                for (int x = this.x + 1; x <= this.x + this.WIDTH; x++) {
                    if (map[x][nexty] == Tileset.FLOOR) {
                        for (int digy = y; digy > nexty; digy--){
                            map[x][digy] = Tileset.GRASS;
                        }
                        done = true;
                        break;
                    }
                }
            }

            if (rand == 3 && !gone[rand]) { // up
                gone[rand] = true;
                int y = this.y + this.HEIGHT + depth;
                int nexty = y + direction[1] + depth;
                if (nexty >= totalHeight) continue;
                for (int x = this.x + 1; x <= this.x + this.WIDTH; x++) {
                    if (map[x][nexty] == Tileset.FLOOR) {
                        for (int digy = y; digy < nexty; digy++){
                            map[x][digy] = Tileset.GRASS;
                        }
                        done = true;
                        break;
                    }
                }
            }

            if ((!gone[0] || !gone[1] || !gone[2] || !gone[3]) == false) {
                depth++;
                gone = new boolean[4];
            }
        }
    }
}
