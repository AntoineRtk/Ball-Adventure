package obstacles;

import states.State;
import entity.Hero;
import entity.MapObject;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import tilemap.AreaZone;

public class Arrow extends MapObject {
    
    private double xb, yb, speedX, speedY;
    private boolean canBeRemove = false;
    private Hero hero;
    private double rotation;
    
    public Arrow(AreaZone area, double x, double y, int instantID, Hero hero) {
        super(area, x, y, instantID);
        xb = x + 15;
        yb = y + 15;
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Arrow1.png"));
        } catch (IOException ex) {
            Logger.getLogger(Arrow.class.getName()).log(Level.SEVERE, null, ex);
        }
        animation.setDelay(-1);
        BufferedImage[] b = {image};
        animation.setFrames(b);
        setWidth(30);
        setHeight(30);
        this.hero = hero;
        setDirectionAndSpeed();
        setCanFly(true);
    }
    
    public void update() {
        super.checkCollisions();
        if(getDX() == 0 || getDY() == 0) {
            canBeRemove = true;
        }
        if(intersects(hero)) {
            hero.hit(2, dx / 4);
            canBeRemove = true;
        }
    }
    
    private void setDirectionAndSpeed()
        {
            // Unit direction vector of the bullet.
            double directionVx = hero.getX() - getX();
            double directionVy = hero.getY() - getY();
            double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
            directionVx = directionVx / lengthOfVector; // Unit vector
            directionVy = directionVy / lengthOfVector; // Unit vector
            // Set speed.
            this.speedX = 4 * directionVx;
            this.speedY = 4 * directionVy;
            initState(new State(getX(), getY(), 0, speedX, speedX * 2, 0, 0, 0, 0, 0));
            setLeft(false);
            setRight(true);
            setDY(speedY * 2);
            double adj, hyp, opp;
            adj = hero.getX() + hero.getWidth() / 2 - getX() + getWidth() / 2;
            opp = getY() + getHeight() / 2 - hero.getY() + hero.getHeight() / 2;
            hyp = Math.sqrt(adj * adj + opp * opp);
            System.out.println("Le côté adjacent de l'angle est de "+adj+" pixels,\n"
                    + "le côté opposé à l'angle est de "+opp+" pixels\n"
                    + "et l'hypothénuse est de "+hyp+"\n"
                    + "et d'après le ... nous faisons Math.acos(adj / hyp)\n"
                    + "qui nous donne : "+Math.toDegrees(Math.acos(adj / hyp))+"\n"
                    + "ou Math.asin(opp / hyp) : "+Math.toDegrees(Math.asin(opp / hyp))+".");
            rotation = Math.toDegrees(Math.acos(adj / hyp));
            System.out.println("Rotation = "+rotation);
            System.out.println(arccos(adj/hyp));
            System.out.println(Math.toDegrees(arccos(adj/hyp)));
            System.out.println(Math.toRadians(Math.toDegrees(arccos(adj/hyp))));
            if(hero.getY() < getY()) {
                rotation = 90 - rotation;
            }
            else if(hero.getY() > getY()) {
                rotation = 180 - rotation;
            }
        }
    
    public void paint(Graphics2D g) {
        AffineTransform transform = new AffineTransform();
        transform.translate(tileMap.getX(), tileMap.getY());
        AffineTransform original = g.getTransform();
        g.setTransform(transform);
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(rotation), 30 / 2, 30 / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        g.drawImage(op.filter(getImage(), null), (int) getX(), (int) getY(), null);
        GeneralPath shape = new GeneralPath();
        shape.moveTo(xb, yb);
        shape.lineTo(hero.getX() + hero.getWidth() / 2, hero.getY() + hero.getHeight() / 2);
        shape.lineTo(hero.getX() + hero.getWidth() / 2, yb);
        shape.closePath();
        //g.draw(shape);
        g.setTransform(original);
    }
    
    public double arccos(double a) {
        return Math.abs(a)==1.0?(1-a)*Math.PI/2.0:Math.atan(-a/Math.sqrt(1-a*a))+2*Math.atan(1);
    }
    
    public boolean canBeRemove() {
        return canBeRemove;
    }
}