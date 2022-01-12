package obstacles;

import entity.Hero;
import states.State;
import entity.MapObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import states.GameStateManager;
import tilemap.AreaZone;

public class Scale extends Obstacle {
    
    private BufferedImage image;
    
    public Scale(AreaZone area, int x, int y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setWidth(32);
        setHeight(128);
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Scale.png"));
        } catch (IOException ex) {
            Logger.getLogger(Scale.class.getName()).log(Level.SEVERE, null, ex);
        }
        initState(new State(x, y, 0, 0, 0, 2, 4, 0, 0, 0));
    }
    
    public void collides(MapObject object) {
        if(object.up) {
            object.setDY(-4);
        }
        if(object.getDY() > 0) {
            object.setDY(2);
        }
        if(getArea().getHero().getClass().getCanonicalName().equals(object.getClass().getCanonicalName())) {
            if(object.getY() + object.getHeight() / 2 < getY() + 10 && object.up && !((Hero) object).jumping) {
                getArea().getHero().rejump();
            }
        }
    }
    
    public boolean isSolid() {
        return false;
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), getWidth(), getHeight(), null);
    }
    
    public BufferedImage getImage() {
        return image;
    }
}