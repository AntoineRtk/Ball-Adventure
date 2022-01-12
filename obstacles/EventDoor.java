
package obstacles;

import entity.MapObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameStateManager;
import tilemap.AreaZone;

public class EventDoor extends Obstacle {
    
    private boolean gettingOn = false;
    private double yG = 0;
    private BufferedImage i;
    
    public EventDoor(AreaZone area, double x, double y, int eventID, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setIndexEvent(eventID);
        try {
            i = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/EventWall.png"));
            BufferedImage[] b = {i};
            animation.setFrames(b);
        } catch (IOException ex) {}
        setSolid(true);
        setWidth(32);
        setHeight(96);
    }
    
    public void event() {
        gettingOn = true;
        yG = getHeight();
    }
    
    public void update() {
        if(gettingOn) {
            if(getHeight() > 1) {
                yG -= 0.4;
                setHeight((int) (yG));
                resize();
            }
            else {
                gettingOn = false;
                setSolid(false);
            }
        }
    }
    
    private void resize() {
        BufferedImage newImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.drawImage(i, 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        BufferedImage[] ni = {newImage};
        animation.setFrames(ni);
    }
    
    public void collides(MapObject object) {}
}