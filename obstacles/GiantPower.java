
package obstacles;

import entity.MapObject;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import jukebox.JukeBox;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class GiantPower extends Obstacle {
    
    private long currentTime = 0;
    private boolean active = false, increasing;
    private double xRadius = 0, yRadius, size = 40, xB, yB;
    
    public GiantPower(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.red);
        g.fillRect(0, 0, 50, 50);
        g.dispose();
        BufferedImage[] b = {image};
        animation.setFrames(b);
        setWidth(50);
        setHeight(50);
        initState(new State(getX(), getY(), 0, 0, 0, 1, 10, 0, 0, 4));
    }
    
    public void update() {
        super.update();
        if(active) {
            if(increasing) {
                size += 0.85;
                if(size > 320) {
                    size = 320;
                    increasing = false;
                    tileMap.setShaking(false, 0);
                    getArea().getHero().setCanMove(true);
                }
                getArea().getHero().setWidth((int) size);
                getArea().getHero().setHeight((int) size);
                getArea().getHero().setX(xB - (int) size);
                getArea().getHero().setY(yB - (int) size);
            }
            for(int i = 0; i < getArea().enemies.size(); i++) {
                if(getArea().getHero().getBounds().intersects(getArea().enemies.get(i).getBounds())) {
                    getArea().enemies.get(i).hit(3);
                }
            }
            Rectangle r = new Rectangle((int) getX(), (int) getY(), getWidth() - 32, getHeight() - 32);
            if(System.currentTimeMillis() - currentTime > 45000) {
                getArea().getHero().rejump();
                getArea().getHero().setWidth(40);
                getArea().getHero().setHeight(40);
                getArea().getHero().setInvincible(false);
                active = false;
                setVisible(true);
                ((LevelChoice) gsm.getState()).getLevel().initMusic();
            }
        }
        xRadius += 0.35;
        if(xRadius > 80) {
            xRadius = 0;
        }
        yRadius += 0.35;
        if(yRadius > 30) {
            yRadius = 0;
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(!active) return;
        g.setPaint(new GradientPaint(0, 0, Color.red, (int) xRadius, (int) yRadius, Color.orange, true));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g.fillRect(0, 0, 30, gsm.getHeight());
        g.fillRect(0, 0, gsm.getWidth(), 30);
        g.fillRect(gsm.getWidth() - 30, 0, 30, gsm.getHeight());
        g.fillRect(0, gsm.getHeight() - 30, gsm.getWidth(), 30);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g.setColor(Color.black);
        g.fillRect(0, 0, 30, 30);
        g.fillRect(0, gsm.getHeight() - 30, 30, 30);
        g.fillRect(gsm.getWidth() - 30, 0, 30, 30);
        g.fillRect(gsm.getWidth() - 30, gsm.getHeight() - 30, 30, 30);
    }
    
    public void collides(MapObject object) {
        if(isVisible()) {
            setVisible(false);
            currentTime = System.currentTimeMillis();
            active = true;
            increasing = true;
            xB = getArea().getHero().getX();
            yB = getArea().getHero().getY();
            size = 40;
            getArea().getHero().setInvincible(true);
            tileMap.setShaking(true, 30);
            getArea().getHero().setCanMove(false);
            ((LevelChoice) gsm.getState()).getLevel().informSpecialTheme();
            JukeBox.play("special");
            JukeBox.setLoopPoints(107785, -1);
        }
    }
}