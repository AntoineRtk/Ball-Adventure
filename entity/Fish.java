
package entity;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.State;
import tilemap.AreaZone;

public class Fish extends Enemy {
    
    public Fish(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        try {
            BufferedImage i = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Fish.png"));
            BufferedImage[] b = {i.getSubimage(0, 0, 21, 21), i.getSubimage(21, 0, 21, 21)};
            animation.setFrames(b);
            animation.setDelay(200);
        } catch (IOException ex) {}
        setWidth(21);
        setHeight(21);
        setLife(3);
        setDamage(2);
        initState(new State(x, y, 0, 0, 0, 2, 5, 1.75, 0, 0));
        right = true;
    }
    
    public void update() {
        super.update();
        getNextPosition();
        if(right) setFlipped(true);
        else setFlipped(false);
    }
    
    public void hit(int damage) {
        if(getArea().getHero().intersectsOnTop(this)) {
        }
        else {
            super.hit(damage);
        }
    }
    
    public String getName() {
        return "Poisson";
    }
}