package obstacles;

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

public class Trampoline extends Obstacle {
    
    private BufferedImage[] normal = new BufferedImage[1], jumping = new BufferedImage[3];
    private long currentTime;
    private int time = 0;
    
    public Trampoline(AreaZone area, int x, int y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image1, image2;
        try {
            image1 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Tremplin1.png"));
            image2 = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Tremplin2.png"));
            normal[0] = image1;
            jumping[0] = image1;
            jumping[1] = image2;
            jumping[2] = image1;
        } catch (IOException ex) {
            Logger.getLogger(Trampoline.class.getName()).log(Level.SEVERE, null, ex);
        }
        animation.setDelay(-1);
        animation.setFrames(normal);
        State s = new State(x, y, -11, 0.6, 4.2, 0.64, 7, 0, 0, 0);
        initState(s);
        setWidth(30);
        setHeight(25);
        setSolid(true);
    }
    
    public void update() {
        super.update();
        if(System.currentTimeMillis() - currentTime > 1000 && time >= 3) {
            animation.setFrames(normal);
            animation.setDelay(-1);
            time = 0;
        }
        if(System.currentTimeMillis() - currentTime > 200 && time < 3) {
            animation.setFrames(normal);
            animation.setDelay(-1);
        }
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), null);
    }
    
    public void collides(MapObject object) {
        if(object.intersectsOnTop(this) && object.dy > 0) {
            time++;
            if(time < 3) {
                object.setVector(object.getDX(), object.getJumpStart());
                currentTime = System.currentTimeMillis();
                animation.setFrames(jumping);
                animation.setDelay(50);
            }
            if(time >= 3) {
                object.setVector(0, object.getJumpStart() * 2);
                currentTime = System.currentTimeMillis();
                animation.setFrames(jumping);
                animation.setDelay(50);
            }
        }
    }
}