
package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class UpGenerator extends Obstacle {
    
    private boolean spawned = false;
    private Obstacle o;
    private int count = 0;
    
    public UpGenerator(AreaZone area, double x, double y, int id, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        try {
            BufferedImage i = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/StraightBall64.png"));
            BufferedImage[] b = {i};
            animation.setFrames(b);
        } catch (IOException ex) {}
        setWidth(64);
        setHeight(64);
        initState(new State(x, y, 0, 0, 0, 31, 31, 0, 0, 2));
        switch(id) {
            case 0:
                o = new MiniPower(area, getX() + getWidth() / 2, getY(), 0, gsm);
                break;
            case 1:
                o = new GiantPower(area, getX() + getWidth() / 2, getY(), 0, gsm);
                break;
            case 2:
                o = new ExplosionPower(area, getX() + getWidth() / 2, getY(), 0, gsm);
                break;
        }
    }
    
    public void update() {
        super.update();
        if(spawned) {
            o.setProjecting(true);
            o.setDY(-3.45555555);
            if(o.intersectsAnything()) {
                o.setProjecting(false);
                count = 50;
            }
            count++;
            if(count > 50) {
                o.setProjecting(false);
                spawned = false;
            }
        }
    }
    
    public void collides(MapObject object) {
        if(object instanceof Hero && isVisible()) {
            Hero h = (Hero) object;
            if(h.getRotation() == 0) {
                setVisible(false);
                spawned = true;
                o.setX(getX() + (getWidth() - o.getWidth()));
                o.setY(getY() - object.getHeight());
                o.setProjecting(true);
                ((LevelChoice) gsm.getState()).getLevel().addObstacle(o, getArea().getID());
            }
        }
    }
}