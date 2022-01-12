
package animation;

import entity.Enemy;
import entity.MapObject;
import java.awt.image.BufferedImage;
import obstacles.Obstacle;
import states.GameStateManager;
import tilemap.AreaZone;

public class Explosion extends Obstacle {
    
    private boolean canBeRemove = false;
    
    public Explosion(AreaZone area, int x, int y, int instantID, BufferedImage[] images, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setWidth(134);
        setHeight(134);
        animation.setFrames(images);
        animation.setDelay(20);
    }
    
    public void update() {
        super.update();
        if(animation.hasPlayedOnce()) {
            canBeRemove = true;
        }
    }
    
    public boolean canBeRemove() {
        return canBeRemove;
    }
    
    public void collides(MapObject object) {
        if(object instanceof Enemy) {
            ((Enemy) object).hit(10);
        }
    }
    
    public void setPlacement(int x, int y) {
        animation.setFrame(0);
        animation.setDelay(20);
        animation.setHasPlayedOnce(false);
    }
}