package Core;

import TileEngine.TETile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WorldObjects implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Room> rooms;
    private ArrayList<Point> doors = new ArrayList<>();
    private Point avatar;
    private TETile[][] world;
    private Random r;
    private char avatarDir;

    public WorldObjects(TETile[][] world) {
        this.world = world;
    }
    public TETile[][] getWorld() {
        return world;
    }

    public ArrayList<Point> getDoors() {
        return doors;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }
    public void setAvatar(Point point, char dir) {
        avatar = point;
        avatarDir = dir;
    }

    public char getAvatarDir() {
        return avatarDir;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public Point getAvatar() {
        return avatar;
    }
}
