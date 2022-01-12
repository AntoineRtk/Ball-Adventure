package entity;

import states.LevelChoice;
import states.GameStateManager;
import states.State;
import obstacles.Arrow;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import tilemap.AreaZone;

public class Gobelin extends Enemy {
    
    private ArrayList<Arrow> arrows = new ArrayList<>();
    private long currentTime = System.currentTimeMillis();
    private BufferedImage[][] animations = new BufferedImage[3][];
    
    public Gobelin(AreaZone area, int x, int y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Gobelin.png"));
        } catch (IOException ex) {}
        BufferedImage[] normal = {image.getSubimage(0, 0, 35, 35), image.getSubimage(35, 0, 35, 35)};
        BufferedImage[] shooting = {image.getSubimage(70, 0, 35, 35)};
        BufferedImage[] jumping = {image.getSubimage(105, 0, 35, 35)};
        animations[0] = normal;
        animations[1] = shooting;
        animations[2] = jumping;
        animation.setFrames(normal);
        State s = new State(x, y, -6.75, 0.2, 2, 0.5, 7, 1, -1, 0.5);
        initState(s);
        right = true;
        setDamage(2);
        setWidth(35);
        setHeight(35);
        setLife(3);
   }
    
    public void update() {
        super.update();
        if(dy != 0 && animation.getFrames() != animations[2]) {
            animation.setFrames(animations[2]);
            animation.setDelay(-1);
        }
        if(dy == 0 && dx != 0 && animation.getFrames() != animations[0] && (right || left)) {
            animation.setFrames(animations[0]);
            animation.setDelay(200);
        }
        if(left) {
            setFlipped(true);
        }
        else {
            setFlipped(false);
        }
        getNextJumpPosition();
        LevelChoice level = (LevelChoice) gsm.getState();
        if(System.currentTimeMillis() - currentTime > 4000) {
            left = false;
            right = false;
            animation.setFrames(animations[1]);
            arrows.add(new Arrow(getArea(), getX(), getY(), 0, level.getHero()));
            currentTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - currentTime < 1000 && !right && !left) {
            if(getArea().getHero().getX() < getX()) {
                setFlipped(true);
                animation.update();
            }
        }
        if(System.currentTimeMillis() - currentTime > 1000 && !right && !left) {
            if(getArea().getHero().getX() < getX()) {
                left = true;
            }
            else {
                right = true;
            }
        }
        for(int i = 0; i < arrows.size(); i++) {
            arrows.get(i).update();
            if(arrows.get(i).canBeRemove()) {
                arrows.remove(i);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        LevelChoice level = (LevelChoice) gsm.getState();
        try {
        for(int i = 0; i < arrows.size(); i++) {
            arrows.get(i).paint(g);
        }
        } catch (IndexOutOfBoundsException e) {}
        GeneralPath shape = new GeneralPath();
        //shape.moveTo(getX(), getY());
        //shape.lineTo(level.getHero().getX(), level.getHero().getY());
        //shape.lineTo(level.getHero().getX(), getY());
        //shape.closePath();
        AffineTransform rotation = g.getTransform();
        g.translate(tileMap.getX(), tileMap.getY());
        g.setTransform(rotation);
    }
    
    public String getName() {
        return "Gobelin";
    
    }
}