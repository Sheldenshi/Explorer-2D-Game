package Core;

import TileEngine.TERenderer;
import TileEngine.TETile;
import TileEngine.Tileset;

import edu.princeton.cs.algs4.BlackFilter;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;


public class Engine {


    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 32;
    private WorldObjects worldObjects;
    private TERenderer ter = new TERenderer();
    private boolean limit = true;

    public TETile[][] worldInitialize () {
        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        Font title = new Font("Monaco", Font.BOLD, 50);
        Font subtitle = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(title);
        StdDraw.text(40, 18, "Explorer");
        StdDraw.setFont(subtitle);
        StdDraw.text(40, 16, "New Game (N)");
        StdDraw.text(40, 15, "Load Game (L)");
        StdDraw.text(40, 14, "Quit (:Q)");
        StdDraw.show();
        run("");
        System.exit(0);
    }
    private void run(String input) {
        boolean withKeyboard = input.equals("");
        boolean gameOn = false;
        int index = 0;
        char c;
        while (true) {
            if (withKeyboard) {
                c = getNextKey();
            } else if (index < input.length()){
                c = Character.toUpperCase(input.charAt(index));
                index++;
            } else {
                break;
            }

            if (c == 'N') {
                if (withKeyboard) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.text(40, 15,
                            "Please input a seed, then press 's' to start the game.");
                    StdDraw.show();
                    String seed = getSeed("");
                    if (seed == "Wrong") {
                        wrongSeedMsg();
                    } else {
                        index += seed.length();
                        startGame(Long.parseLong(seed), true);
                        gameOn = true;
                    }
                } else {
                    String seed = getSeed(input.substring(index));
                    if (seed == "Wrong") {
                        System.out.println("Input is bad.");
                    } else {
                        startGame(Long.parseLong(seed), false);
                        gameOn = true;
                    }
                }
            } else if (c == 'L') {
                load(withKeyboard);
                gameOn = true;
                if (withKeyboard) {
                    ter.renderFrame(worldObjects, limit);
                }

            } else if (c == ':') {
                if ((withKeyboard && getNextKey() == 'Q')) {
                    save();
                    break;
                } else if (index < input.length()
                        && Character.toUpperCase(input.charAt(index)) == 'Q') {
                    save();
                    break;
                } else {
                    if (withKeyboard) {
                        StdDraw.clear(StdDraw.BLACK);
                        StdDraw.setPenColor(StdDraw.RED);
                        StdDraw.text(40, 15,
                                "Type :Q to save and quit.");
                        StdDraw.show();
                    }
                }
            } else if (gameOn) {
                if (c == 'W' || c == 'A' || c == 'S' || c == 'D') {
                    move(c);
                    if (withKeyboard) {
                        ter.renderFrame(worldObjects, limit);
                    }

                    if (worldObjects.getDoors().size() == 0) {
                        wonGameMsg();
                        gameOn = false;
                    }
                }
            }
        }
    }
    private void wrongSeedMsg() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.text(40, 13,
                "Please type in 0-9 digits for seed.");
        StdDraw.setPenColor(StdDraw.WHITE);
        Font title = new Font("Monaco", Font.BOLD, 50);
        Font subtitle = new Font("Monaco", Font.PLAIN, 15);
        StdDraw.setFont(title);
        StdDraw.text(40, 18, "Explorer");
        StdDraw.setFont(subtitle);
        StdDraw.text(40, 16, "New Game (N)");
        StdDraw.text(40, 15, "Load Game (L)");
        StdDraw.text(40, 14, "Quit (Q)");
        StdDraw.show();
    }
    private void wonGameMsg() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(40, 12, "New Game (N)");
        StdDraw.text(40, 13, "Load Game (L)");
        StdDraw.text(40, 14, "Quit (Q)");
        StdDraw.show();
        StdDraw.text(40, 15,
                "You have won the game");
        StdDraw.show();
    }
    private void move(char c) {
        Point avatarCurr = worldObjects.getAvatar();
        Point next;
        if (c == 'W') {
            if (avatarCurr.getY() + 1 > 0) {
                next = new Point(avatarCurr.getX(), avatarCurr.getY() + 1);
                if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("floor") ||
                        worldObjects.getWorld()[next.getX()][next.getY()].equals("unlocked door")) {
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.AVATAR;
                    worldObjects.getWorld()[avatarCurr.getX()][avatarCurr.getY()] = Tileset.FLOOR;
                    worldObjects.setAvatar(next, 'W');
                } else if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("locked door")) {
                    worldObjects.getDoors().remove(0);
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.FLOOR;
                }
            }
        } else if (c == 'A') {
            if (avatarCurr.getX() - 1 > 0) {
                next = new Point(avatarCurr.getX() - 1, avatarCurr.getY());
                if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("floor") ||
                        worldObjects.getWorld()[next.getX()][next.getY()].equals("unlocked door")) {
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.AVATAR;
                    worldObjects.getWorld()[avatarCurr.getX()][avatarCurr.getY()] = Tileset.FLOOR;
                    worldObjects.setAvatar(next, 'A');
                } else if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("locked door")) {
                    worldObjects.getDoors().remove(0);
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.FLOOR;
                }
            }
        } else if (c == 'S') {
            if (avatarCurr.getY() - 1 < HEIGHT) {
                next = new Point(avatarCurr.getX(), avatarCurr.getY() - 1);
                if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("floor") ||
                        worldObjects.getWorld()[next.getX()][next.getY()].equals("unlocked door")) {
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.AVATAR;
                    worldObjects.getWorld()[avatarCurr.getX()][avatarCurr.getY()] = Tileset.FLOOR;
                    worldObjects.setAvatar(next, 'S');
                } else if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("locked door")) {
                    worldObjects.getDoors().remove(0);
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.FLOOR;
                }
            }
        } else {
            if (avatarCurr.getX() + 1 < WIDTH) {
                next = new Point(avatarCurr.getX() + 1, avatarCurr.getY());
                if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("floor") ||
                        worldObjects.getWorld()[next.getX()][next.getY()].description().equals("unlocked door")) {
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.AVATAR;
                    worldObjects.getWorld()[avatarCurr.getX()][avatarCurr.getY()] = Tileset.FLOOR;
                    worldObjects.setAvatar(next, 'D');
                } else if (worldObjects.getWorld()[next.getX()][next.getY()].description().equals("locked door")) {
                    worldObjects.getDoors().remove(0);
                    worldObjects.getWorld()[next.getX()][next.getY()] = Tileset.FLOOR;
                }
            }
        }
    }
    private void load(boolean withKeyboard) {
        try {
            File saveFile = new File("save.txt");
            if (!saveFile.exists()) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.text(0.5, 0.3,
                        "No world to load.");
                StdDraw.show();
            }
            FileInputStream fileIn = new FileInputStream(saveFile.getAbsolutePath());
            ObjectInputStream in = new ObjectInputStream(fileIn);
            worldObjects = (WorldObjects)in.readObject();
            in.close();
            fileIn.close();
            if (withKeyboard) {
                ter.initialize(WIDTH, HEIGHT);
            }

        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("WorldObjects class not found");
            c.printStackTrace();
            return;
        }
    }
    private void save() {
        try {
            File saveFile = new File( "save.txt");
            if (! saveFile.exists()) {
                saveFile.createNewFile();
            }
            FileOutputStream fileOut =
                    new FileOutputStream(saveFile.getAbsolutePath());
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(worldObjects);
            out.close();
            fileOut.close();
            System.out.printf("World saved to /savedWorld/save.txt");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
    private char getNextKey() {

        while (true) {

            if (worldObjects!= null && StdDraw.isMousePressed()) {
                HeadsUpDisplay();
            }

            if (StdDraw.hasNextKeyTyped()) {

                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (true) {
                    System.out.print(c);
                }
                return c;
            }
        }
    }
    private String getSeed(String input) {
        boolean withKeyBoard = input == "";
        String seed = "";
        int index = 0;
        char c;
        while (true) {
            if (withKeyBoard) {
                c = getNextKey();
            } else if (index < input.length()){
                c = Character.toUpperCase(input.charAt(index));
                index++;
            } else {
                return "Wrong";
            }
            if (Character.isDigit(c)) {
                seed += c;
                if (withKeyBoard) {
                    StdDraw.clear(Color.BLACK);
                    StdDraw.text(40, 15, "Seed:" + seed);
                    StdDraw.text(40, 16, "Press S to Start the game.");
                    StdDraw.show();
                }

            } else {
                if (c == 'S') {
                    return seed;
                }
                return "Wrong";
            }
        }
    }
    //Text that describes the tile currently under the mouse pointer.
    private void HeadsUpDisplay() {

        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();

        if (x < WIDTH && y < HEIGHT) {
            if (worldObjects.getWorld()[x][y].equals(Tileset.FLOOR)) {
                ter.renderFrame(worldObjects, limit);
                StdDraw.setPenColor(Color.red);
                StdDraw.text(WIDTH - 5, 1, "Floor");
                StdDraw.show();
            } else if (worldObjects.getWorld()[x][y].equals(Tileset.WALL)) {
                ter.renderFrame(worldObjects, limit);
                StdDraw.setPenColor(Color.orange);
                StdDraw.text(WIDTH - 5, 1, "Wall");
                StdDraw.show();
            } else if (worldObjects.getWorld()[x][y].equals((Tileset.NOTHING))) {
                ter.renderFrame(worldObjects, limit);
                StdDraw.setPenColor(Color.orange);
                StdDraw.text(WIDTH - 5, 1, "Emptiness");
                StdDraw.show();
            } else if (worldObjects.getWorld()[x][y].equals(Tileset.AVATAR)) {
                ter.renderFrame(worldObjects, limit);
                StdDraw.setPenColor(Color.orange);
                StdDraw.text(WIDTH - 5, 1, "You");
                StdDraw.show();
            } else if (worldObjects.getWorld()[x][y].equals(Tileset.LOCKED_DOOR)) {
                ter.renderFrame(worldObjects, limit);
                StdDraw.setPenColor(Color.orange);
                StdDraw.text(WIDTH - 5, 1, "Locked Door");
                StdDraw.show();
            }
        }


    }



    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        //TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        run(input);
        return worldObjects.getWorld();
    }
    private void startGame(long seed, boolean isKeyboard) {
        TETile[][] world = worldInitialize();
        worldObjects = new WorldObjects(world);
        setSeed(seed);
        generateWorld();
        if (isKeyboard) {
            ter.renderFrame(worldObjects, limit);
        }

    }

    private void generateWorld() {
        //create rooms

        ArrayList<Hallway> halls;
        worldObjects.setRooms(Room.roomGenerator(worldObjects.getR(), worldObjects.getWorld()));


        //create hallways
        //halls = Hallway.hallGenerator(r, world, rooms);

        //connect them
        for (int i = 0; i < worldObjects.getRooms().size() - 1; i++) {
            Room curr = worldObjects.getRooms().get(i);
            Room next = worldObjects.getRooms().get(i + 1);
            generatePath(worldObjects.getR(), curr, next, worldObjects.getWorld());
        }
        //draw walls
        drawWalls(worldObjects.getWorld());
        //randomly place @
        int avatarX = worldObjects.getR().nextInt(WIDTH);
        int avatarY = worldObjects.getR().nextInt(HEIGHT);
        while (worldObjects.getWorld()[avatarX][avatarY] != Tileset.FLOOR) {
            avatarX = worldObjects.getR().nextInt(WIDTH);
            avatarY = worldObjects.getR().nextInt(HEIGHT);
        }
        worldObjects.getWorld()[avatarX][avatarY] = Tileset.AVATAR;
        worldObjects.setAvatar(new Point(avatarX, avatarY), 'A');
    }

    private static void drawWalls(TETile[][] world) {
        ArrayList<Point> mask = new ArrayList<>();
        mask.add(new Point(-1, -1));
        mask.add(new Point(0, -1));
        mask.add(new Point(1, -1));
        mask.add(new Point(-1, 0));
        mask.add(new Point(1, 0));
        mask.add(new Point(-1, 1));
        mask.add(new Point(0, 1));
        mask.add(new Point(1, 1));
        int[][] visited = new int[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (visited[x][y] == 0 && world[x][y] != Tileset.NOTHING) {
                    visited[x][y] = 1;
                    for (int i = 0; i < mask.size(); i++) {
                        int maskedX = x + mask.get(i).getX();
                        int maskedY = y + mask.get(i).getY();
                        if (maskedX < 0 || maskedX > WIDTH || maskedY < 0 || maskedY > HEIGHT) {
                            continue;
                        }
                        if (visited[maskedX][maskedY] == 0) {
                            if (world[maskedX][maskedY] == Tileset.NOTHING) {
                                visited[maskedX][maskedY] = 1;
                                world[maskedX][maskedY] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }

    private void generatePath(Random r, Room room1, Room room2, TETile[][] world) {
        int room1X = worldObjects.getR().nextInt(room1.getWidth()) + room1.getxLeft();
        int room1Y = worldObjects.getR().nextInt(room1.getLength()) + room1.getyTop();

        int room2X = worldObjects.getR().nextInt(room2.getWidth()) + room2.getxLeft();
        int room2Y = worldObjects.getR().nextInt(room2.getLength()) + room2.getyTop();

        while (worldObjects.getDoors().size() > 0 &&
                room1X == worldObjects.getDoors().get(worldObjects.getDoors().size() - 1).getX() &&
                room1Y == worldObjects.getDoors().get(worldObjects.getDoors().size() - 1).getY()) {
            room1X = worldObjects.getR().nextInt(room1.getWidth()) + room1.getxLeft();
            room1Y = worldObjects.getR().nextInt(room1.getLength()) + room1.getyTop();
        }
        if (room1X >= room2X) { // room1 on the right of room2

        } else { // room1 on the left of room2
            if (room1Y >= room2Y) { // room1 on the top left of room2
                if (worldObjects.getR().nextInt(10) > 4) { // x first
                    for (int x = room1X; x <= room2X; x++) {
                        world[x][room1Y] = Tileset.FLOOR;
                    }
                    for (int y = room1Y; y >= room2Y; y--) {
                        world[room2X][y] = Tileset.FLOOR;
                    }
                } else { // y first
                    for (int y = room1Y; y >= room2Y; y--) {
                        world[room1X][y] = Tileset.FLOOR;
                    }
                    for (int x = room1X; x <= room2X; x++) {
                        world[x][room2Y] = Tileset.FLOOR;
                    }
                }
            } else { // room1 on the bottom left of room2
                if (worldObjects.getR().nextInt(10) > 4) { // x first
                    for (int x = room1X; x <= room2X; x++) {
                        world[x][room1Y] = Tileset.FLOOR;
                    }
                    for (int y = room1Y; y <= room2Y; y++) {
                        world[room2X][y] = Tileset.FLOOR;
                    }
                } else { // y first
                    for (int y = room1Y; y <= room2Y; y++) {
                        world[room1X][y] = Tileset.FLOOR;
                    }
                    for (int x = room1X; x <= room2X; x++) {
                        world[x][room2Y] = Tileset.FLOOR;
                    }
                }
            }
        }
        world[room1X][room1Y] = Tileset.LOCKED_DOOR;
        world[room2X][room2Y] = Tileset.LOCKED_DOOR;
        Point room1Point = new Point(room1X, room1Y);
        Point room2Point = new Point(room2X, room2Y);
        worldObjects.getDoors().add(room1Point);
        worldObjects.getDoors().add(room2Point);
    }
    
    public String toString() {
        if (worldObjects != null) {
            return TETile.toString(worldObjects.getWorld());
        } else {
            return "Engine";
        }
    }

    private void setSeed(long seed) {
        worldObjects.setR(new Random(seed));
    }
}



