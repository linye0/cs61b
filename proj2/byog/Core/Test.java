package byog.Core;

import byog.TileEngine.TETile;
public class Test {
    public static void main(String[] args) {
        World world = new World(140, 80, 234);
        TETile[][] finalWorldFrame = world.map();
        System.out.println(TETile.toString(finalWorldFrame));
    }
}
