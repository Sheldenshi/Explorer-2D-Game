package Core;

import TileEngine.TERenderer;
import TileEngine.TETile;
import TileEngine.Tileset;

import java.io.Serializable;
import java.util.*;


public class Room implements Serializable {
    private static final long serialVersionUID = 652968509826775790L;
    private static final int MAXROOM = 10;
    // length and width of the room
    private int w;
    private int l;
    // coordinate of the room
    private int xLeft;
    private int xRight;
    private int yTop;
    private int yBottom;


    public Room(int x, int y, int l, int w) {
        this.xLeft = x;
        this.xRight = x + w;
        this.yTop = y;
        this.yBottom = y + l;
        this.w = w;
        this.l = l;
    }
    public Room(Random r) {
        this.w = r.nextInt(10) +5;
        this.l = r.nextInt(5) + 3;
        this.xLeft = r.nextInt(Engine.WIDTH - w - 2) + 1;
        this.xRight = xLeft + w;
        this.yTop = r.nextInt(Engine.HEIGHT - l - 4) + 1;
        this.yBottom = yTop + l;

    }


    public int getxLeft() {
        return xLeft;
    }

    public int getxRight() {
        return xRight;
    }

    public int getyTop() {
        return yTop;
    }

    public int getyBottom() {
        return yBottom;
    }

    public int getWidth() {
        return w;
    }

    public int getLength() {
        return l;
    }




    // generate a new room and draw it
    public static Room drawRoom(Random r, TETile[][] world, ArrayList<Room> existingRooms) {
        Room room = new Room(r);
        while (room.overlap(existingRooms)){
            room = new Room(r);
        }

        for (int x = room.xLeft; x < room.xRight; x++) {
            for (int y = room.yTop; y < room.yBottom; y++) {
                world[x][y] = Tileset.FLOOR;
            }
        }


        return room;
    }





    /*
        (x0,y1) ------------ (x1,y1)
           |                    |
           |                    |
           |                    |
           |                    |
        (x0,y0) ------------ (x1,y0)

        x is column, y is row
     */

    public boolean overlap(ArrayList<Room> existingRooms) {
        if (existingRooms.size() == 0) {
            return false;
        }

        for (int i = 0; i < existingRooms.size(); i++) {
        /*
            Room curr = existingRooms.get(i);
            // Left x
            int leftX = Math.max(this.getxLeft(), curr.getxLeft());
            // Right x
            int rightX = Math.min(this.getxRight(), curr.getxRight());
            // Bottom y
            int botY = Math.max(this.getyBottom(), curr.getyBottom());
            // TopY
            int topY = Math.min(this.getyTop(), curr.getyTop());

            if (rightX >= leftX && topY >= botY)
                return true;
            }
        return false;

         */
            Room curr = existingRooms.get(i);
            if ((this.getxRight() < curr.getxLeft() || this.getxLeft() > curr.getxRight()) ||
                    (this.getyBottom() > curr.getyTop() || this.getyTop() < curr.getyBottom())) {
                continue;
            }

            return true;
        }
        return false;

    }



    public static ArrayList<Room> roomGenerator(Random r, TETile[][] world) {
        int numOfRooms = r.nextInt(MAXROOM - 2) + 2;
        ArrayList<Room> rooms = new ArrayList<Room>();

        for (int i = 0; i < numOfRooms; i++) {
            Room room = drawRoom(r, world, rooms);
            rooms.add(room);
        }
        return rooms;
    }

}