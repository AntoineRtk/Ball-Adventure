
package animation;

import java.awt.Color;
import java.awt.Graphics2D;
import tilemap.TileMap;

public class DustExplosion {
    
    private double x, y;
    private boolean canBeRemoved = false;
    private int dir, count = 0;
    private Color c;
    private TileMap t;
    
    public DustExplosion(double x, double y, int dir, TileMap t) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        if(new java.util.Random().nextInt(2) == 0) {
            c = Color.black;
        }
        else {
            c = Color.white;
        }
        this.t = t;
    }
    
    public void paint(Graphics2D g) {
        g.setColor(c);
        g.fillOval(t.getX() + (int) x, t.getY() + (int) y, 8, 8);
    }
    
    public void update() {
        switch(dir) {
            case 0:
                x -= ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                y -= ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                break;
            case 1:
                x += ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                y -= ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                break;
            case 2:
                x -= ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                y += ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2;
                break;
            case 3:
                x += ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                y += ((7 + new java.util.Random().nextInt(10 - 7 + 1)) / 10) * 2.5;
                break;
        }
        if(count > 40) {
            canBeRemoved = true;
        }
        count++;
    }
    
    public boolean canBeRemoved() {
        return canBeRemoved;
    }
}