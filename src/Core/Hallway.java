package Core;

import TileEngine.TETile;
import TileEngine.Tileset;
import java.lang.*;
import java.util.*;

//create a hallway class to connect rooms and hallway
public class Hallway {
    private static final int MAXHALL = 12;
    private int w;
    private int l;
    private int xLeft;
    private int xRight;
    private int yTop;
    private int yBottom;
    private boolean vertical;


    public Hallway(Random r, Boundaries b) {
        if (r.nextInt(10) > 4) {
            this.vertical = true;
        } else {
            this.vertical = false;
        }
        if (vertical) {
            this.w = 1;
            this.l = r.nextInt(b.getxMax() / 4) + 5;
        } else {
            this.w = r.nextInt(b.getyMax() / 4) + 2;
            this.l = 1;
        }
        this.xLeft = r.nextInt(b.getxMax() - b.getxMin() - w) + b.getxMin();
        this.xRight = xLeft + w;
        this.yTop = r.nextInt(b.getyMax() - b.getyMin() - l) + b.getyMin();
        this.yBottom = yTop + l;
    }

    public static ArrayList<Hallway> hallGenerator(Random r, TETile[][] world, ArrayList<Room> rooms) {
        int numOfHalls = r.nextInt(MAXHALL - 2) + 2;
        ArrayList<Hallway> halls = new ArrayList<Hallway>();
        Boundaries boundaries = new Boundaries(rooms);
        for (int i = 0; i < numOfHalls; i++) {
            Hallway hallway = drawHallway(r, world, halls, boundaries);
            halls.add(hallway);
        }
        return halls;
    }

    public static boolean overlap(Hallway hall, ArrayList<Hallway> existingHalls) {
        for (int i = 0; i < existingHalls.size(); i++) {
            Hallway curr = existingHalls.get(i);
            Boolean isVertical = curr.vertical;
            if (hall.vertical && isVertical) {
                if (curr.xLeft == hall.xLeft) {
                    return true;
                }
            } else if (!hall.vertical && ! isVertical) {
                if (curr.yTop == hall.yTop) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Hallway drawHallway(Random r, TETile[][] world,
                                   ArrayList<Hallway> halls, Boundaries boundaries) {
        Hallway hall = new Hallway(r, boundaries);
        while (overlap(hall, halls)) {
            hall = new Hallway(r, boundaries);
        }
        for (int x = hall.xLeft; x < hall.xRight; x++) {
            for (int y = hall.yTop; y < hall.yBottom; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }
        return hall;
    }
}

