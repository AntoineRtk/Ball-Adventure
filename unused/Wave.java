package unused;


import states.State;
import entity.MapObject;
import java.awt.Color;
import java.awt.Graphics2D;
import tilemap.AreaZone;

public class Wave extends MapObject {
    
    private boolean isToRemove = false;
    
    public Wave(AreaZone area, int x, int y, boolean left, int instantID) {
        super(area, x, y, instantID);
        this.left = left;
        this.right = !left;
        setWidth(30);
        setHeight(35);
        initState(new State(x, (tileMap.getRowTile(y) + 2) * tileMap.getTileSize() - 35, 0, 0.9, 2, 0, 0, 0, 0, 0));
    }
    
    public void update() {
        super.checkCollisions();
    }
    
    public void paint(Graphics2D g) {
        g.setColor(Color.magenta);
        g.fillArc(tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), 30, 35, 0, 180);
    }

    public void setToRemove(boolean b) {
        this.isToRemove = b;
    }
    
    public boolean isToRemove() {
        return isToRemove;
    }
}