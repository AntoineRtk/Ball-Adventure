
package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.State;
import tilemap.AreaZone;

public class Canon extends Obstacle {
    
    private double toX, toY, speedX, speedY, rotation;
    private boolean inCanon = false, projecting = false;
    private long time = 0;
    
    public Canon(AreaZone area, int x, int y, double toX, double toY, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setWidth(30);
        setHeight(40);
        setSolid(true);
        this.toX = toX;
        this.toY = toY;
        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Canon.png"));
            BufferedImage images[] = {image};
            animation.setFrames(images);
        } catch (IOException ex) {
            Logger.getLogger(Canon.class.getName()).log(Level.SEVERE, null, ex);
        }
        initState(new State(x, y, 0, 0, 0, 5, 10, 0, 0, 5));
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        g.setColor(Color.red);
        GeneralPath shape = new GeneralPath();
        AffineTransform af = new AffineTransform();
        af.translate(tileMap.getX(), tileMap.getY());
        AffineTransform last = g.getTransform();
        g.setTransform(af);
        shape.moveTo(getX(), getY());
        shape.lineTo(toX, toY);
        shape.lineTo(toX, getY());
        shape.closePath();
        g.setTransform(last);
        //g.draw(shape);
        g.setColor(Color.yellow);
        //g.fillRect(tileMap.getX() + (int) toX, tileMap.getY() + (int) toY, 10, 10);
    }
    
    public void initSpeed() {
        // Unit direction vector of the bullet.
        double directionVx = toX - getX();
        double directionVy = toY - getY();
        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector; // Unit vector
        directionVy = directionVy / lengthOfVector; // Unit vector
        // Set speed.
        this.speedX = 10 * directionVx;
        this.speedY = 10 * directionVy;
        double adj, hyp, opp;
        adj = toX - getX();
        opp = getY() - toY;
        hyp = Math.sqrt(adj * adj + opp * opp);
        rotation = 90 - Math.toDegrees(Math.acos(adj / hyp));
        setRotation(rotation);
        System.out.println(adj+" "+opp+" "+hyp+" "+Math.hypot(adj, opp)+" "+rotation);
    }
    
    public void collides(MapObject object) {
        if(!object.getClass().getName().equals(Hero.class.getName())) return;
        if(object.intersects(this) && !inCanon && object.dy > 0) {
            initSpeed();
            time = System.currentTimeMillis();
            inCanon = true;
            Hero hero = (Hero) object;
            hero.setPosition(getX(), getY());
            getArea().getHero().setCanMove(false);
            hero.setVisible(false);
        }
    }
    
    public void update() {
        super.update();
        if(inCanon && System.currentTimeMillis() - time > 2000) {
            if(projecting && !getArea().getHero().intersects(this)) {
                inCanon = false;
                getArea().getHero().setProjecting(projecting);
            }
            projecting = true;
            getArea().getHero().setCanMove(false);
            getArea().getHero().setVisible(true);
        }
        if(projecting) {
            getArea().getHero().setVector(speedX, speedY);
            System.out.println(speedX+" "+speedY);
            if(getArea().getHero().intersectsAnything()) {
                projecting = false;
                getArea().getHero().setCanMove(true);
                getArea().getHero().setProjecting(false);
            }
            setRotation(0);
        }
    }
}