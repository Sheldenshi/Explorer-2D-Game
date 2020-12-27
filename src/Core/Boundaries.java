package Core;

import java.io.Serializable;
import java.util.ArrayList;

public class Boundaries implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;
    private int xMax;
    private int xMin;
    private int yMax;
    private int yMin;

    public Boundaries(ArrayList<byow.Core.Room> rooms) {
        this.xMin = Engine.WIDTH;
        this.xMax = 0;
        this.yMin = Engine.HEIGHT;
        this.yMax = 0;

        for (int i = 0; i < rooms.size(); i++) {
            xMin = Math.min(xMin, rooms.get(i).getxLeft());
            xMax = Math.max(xMax, rooms.get(i).getxRight());
            yMin = Math.min(yMin, rooms.get(i).getyTop());
            yMax = Math.max(yMax, rooms.get(i).getyBottom());
        }
        xMin = Math.max(1, xMin - 5);
        xMax = Math.min(Engine.WIDTH - 1, xMax + 5);
        yMin = Math.max(1, yMin - 3);
        yMax = Math.min(Engine.HEIGHT - 1, yMax + 3);
    }

    public int getxMax() {
        return xMax;
    }

    public int getxMin() {
        return xMin;
    }

    public int getyMax() {
        return yMax;
    }

    public int getyMin() {
        return yMin;
    }
}
