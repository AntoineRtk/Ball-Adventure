
package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.State;
import tilemap.AreaZone;

public class RockPeak extends Enemy {
    
    private BufferedImage[][] animations = new BufferedImage[3][2];
    private boolean inAttack = false;
    private long inAttackTime = 0;
    
    public RockPeak(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        initState(new State(x, y, 0, 0, 0, 5, 15, 0, 0, 10));
        setWidth(35);
        setHeight(35);
        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/RockPeak.png"));
            BufferedImage[] normal = {image.getSubimage(0, 0, 35, 35), image.getSubimage(35, 0, 35, 35)};
            BufferedImage[] readyForAttack = {image.getSubimage(70, 0, 35, 35), image.getSubimage(105, 0, 35, 35)};
            BufferedImage[] peak = {image.getSubimage(140, 0, 35, 35), image.getSubimage(175, 0, 35, 35)};
            animations[0] = normal;
            animations[1] = readyForAttack;
            animations[2] = peak;
        } catch (IOException ex) {}
        animation.setFrames(animations[0]);
        setLife(1);
        setDamage(1);
    }
    
    public void setLookAtTop(boolean b) {
        if(b) {
            animation.setFrame(1);
        }
        else {
            animation.setFrame(0);
        }
    }
    
    public void update() {
        super.update();
        Rectangle r = new Rectangle((int) getX() - 48, (int) getY() - 48, getWidth() + 48 * 2, 48 * 2 + getWidth());
        if(r.intersects(getArea().getHero().getBounds()) && !inAttack && System.currentTimeMillis() - inAttackTime > 8500) {
            inAttack = true;
            inAttackTime = System.currentTimeMillis();
        }
        if(inAttack) {
            if(System.currentTimeMillis() - inAttackTime < 500) {
                animation.setFrames(animations[1]);
            }
            if(System.currentTimeMillis() - inAttackTime > 500 && System.currentTimeMillis() - inAttackTime < 3500) {
                animation.setFrames(animations[2]);
            }
            if(System.currentTimeMillis() - inAttackTime > 5000) {
                inAttack = false;
                animation.setFrames(animations[0]);
            }
        }
        if(getArea().getHero().getX() < getX()) {
            setFlipped(true);
        }
        else {
            setFlipped(false);
        }
        if(getArea().getHero().getY() + getArea().getHero().getHeight() < getY()) {
            setLookAtTop(true);
        }
        else {
            setLookAtTop(false);
        }
        if(getArea().getHero().intersects(this) && inAttack) {
            if(getArea().getHero().getX() >= getX()) {
                getArea().getHero().hit(2, 2);
            }
            else {
                getArea().getHero().hit(2, -2);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        Rectangle r = new Rectangle(tileMap.getX() + (int) getX() - 48, tileMap.getY()+ (int) getY() - 48, getWidth() + 48 * 2, 48 * 2 + getWidth());
        //g.draw(r);
    }
    
    public void hit(int damage) {
        if(getArea().getHero().intersectsOnTop(this) && !inAttack) {
            super.hit(damage);
        }
        else {
            if(getArea().getHero().intersects(this)) {
                if(getArea().getHero().getX() >= getX()) {
                    getArea().getHero().hit(3, 2);
                }
                else {
                    getArea().getHero().hit(3, -2);
                }
            }
            else {
                super.hit(damage);
            }
        }
    }
    
    public String getName() {
        return "Pierre pique";
    }
}