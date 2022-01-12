package entity;

import states.GameStateManager;
import states.State;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import tilemap.AreaZone;

public class Dinosaur extends Enemy {
    
    public Dinosaur(AreaZone area, int x, int y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Dinosaure.png"));
        } catch (IOException ex) {}
        animation.setDelay(150);
        BufferedImage[] b = {image.getSubimage(0, 0, 30, 30), image.getSubimage(30, 0, 30, 30)};
        animation.setFrames(b);
        State s = new State(x, y, -11, 0.4, 3, 0.64, 7, 0.2, 0.2, 0.2);
        initState(s);
        right = true;
        setDamage(4);
        setWidth(30);
        setHeight(30);
        setLife(5);
    }
    
    public void update() {
        super.update();
        getNextPosition();
        if(left) {
            setFlipped(true);
        }
        else {
            setFlipped(false);
        }
    }
    
    public String getName() {
        return "Dinosaure";
    }
}