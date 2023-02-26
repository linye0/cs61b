package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.util.Random;
public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    Font bigFont = new Font("Monaco", Font.PLAIN, 60);
    Font middleFont = new Font("Monaco", Font.PLAIN, 45);
    Font smallFont = new Font("Monaco", Font.PLAIN, 30);
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawWelcomeUI();
        char firstChar = getFirstChar();
        if (firstChar == 'n') {
            newGame();
        } else if (firstChar == 'l') {
            loadGame();
        } else if (firstChar == 'q') {
            System.exit(0);
        }

    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        input = input.toLowerCase();
        TETile[][] finalWorldFrame = null;
        char firstChar = input.charAt(0);
        if (firstChar == 'n') {
            finalWorldFrame = newGame(input);
        } else if (firstChar == 'l') {
            // finalWorldFrame = loadGame(input);
        } else if (firstChar == 'q') {
            System.exit(0);
        } else {
            throw new RuntimeException("Wrong String");
        }
        return finalWorldFrame;
    }

    private char getFirstChar() {
        char c;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) continue;
            c = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (c == 'n' || c == 'l' || c == 'q') break;
        }
        return c;
    }
    private void drawWelcomeUI() {
        initializeCanvas();

        StdDraw.setFont(bigFont);
        StdDraw.text(WIDTH / 2, 3 * HEIGHT / 4, "Welcome to cs61b's MAZE");

        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 4 + 4, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4 + 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 4  , "Quit (Q)");

        StdDraw.show();
    }

    private void initializeCanvas() {
        StdDraw.setCanvasSize(WIDTH * 16, (HEIGHT + 1) * 16);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT + 1);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);
    }

    private void newGame() {
        long seed = getSeed();
        World world = new World(WIDTH, HEIGHT, seed);
        ter.initialize(WIDTH + 1, HEIGHT + 1);
        ter.renderFrame(world.map());
        play(world.map);
    }

    private long getSeed() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Type in the seed to begin (end with 'S').");
        StdDraw.show();
        String seedString = "";
        while (true) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setFont(smallFont);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "Type in the seed to begin (end with 'S').");

            char digit;
            if (!StdDraw.hasNextKeyTyped()) continue;
            digit = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (digit != 's') {
                if (!Character.isDigit(digit)) {
                    break;
                }
                seedString += digit;
                StdDraw.setFont(smallFont);
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, seedString);
                StdDraw.show();
            } else {
                break;
            }
        }
        long seed = Long.parseLong(seedString);
        return seed;
    }

    private TETile[][] newGame(String input) {
        int indexS = input.indexOf('s');
        long seed = Long.parseLong(input.substring(1, indexS));
        World world = new World(WIDTH, HEIGHT, seed);
        return world.map();
    }

    private void play(TETile[][] map) {
        while(true) {
            if (!StdDraw.hasNextKeyTyped()) continue;
            char c = Character.toLowerCase(StdDraw.nextKeyTyped());
            switch(c) {
                case 'w':
                    Player.moveUp(map);
                    ter.renderFrame(map);
                    if (Player.isWin(map)) showWin();
                    break;
                case 'a':
                    Player.moveLeft(map);
                    ter.renderFrame(map);
                    if (Player.isWin(map)) showWin();
                    break;
                case 's':
                    Player.moveDown(map);
                    ter.renderFrame(map);
                    if (Player.isWin(map)) showWin();
                    break;
                case 'd':
                    Player.moveRight(map);
                    ter.renderFrame(map);
                    if (Player.isWin(map)) showWin();
                    break;
                case ':':
                    while (true) {
                        if (!StdDraw.hasNextKeyTyped()) continue;
                        if (Character.toLowerCase(StdDraw.nextKeyTyped()) == 'q') {
                            saveGame(map);
                            System.exit(0);
                        } else {
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }

    private void saveGame(TETile[][] map) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("savefile.txt"));
            out.writeObject(map);
            out.writeObject(Player.getPos());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showWin() {
        while (true) {
            StdDraw.clear(Color.WHITE);
            StdDraw.setFont(smallFont);
            StdDraw.text(WIDTH / 2, HEIGHT / 2, "Win!");
            StdDraw.show();
        }
    }
    private void loadGame() {
        ter.initialize(WIDTH + 1, HEIGHT + 1);
        TETile[][] map = getSavedGame();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if(map[i][j] == Tileset.PLAYER) {
                    Position playerPos = new Position(i, j);
                    Player.setPos(playerPos);
                    break;
                }
            }
        }
        System.out.println(Player.getPos().x);
        System.out.println(Player.getPos().y);
        ter.renderFrame(map);
        play(map);
    }
    private TETile[][] getSavedGame() {
        TETile[][] finalWorldFrame = null;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("savefile.txt"));
            finalWorldFrame = (TETile[][]) in.readObject();
            Player.setPos((Position) in.readObject());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return finalWorldFrame;
    }

    private TETile loadGame(String input) {
        return null;
    }
}