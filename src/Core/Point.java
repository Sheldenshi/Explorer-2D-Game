package Core;

import java.io.Serializable;

public class Point implements Serializable {
    private static final long serialVersionUID = 652965098267757690L;
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
