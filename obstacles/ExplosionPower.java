
package obstacles;

import animation.Explosion;
import entity.MapObject;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jukebox.JukeBox;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class ExplosionPower extends Obstacle {
    
    private long currentTime = 0, ultimateTime, startTime;
    private boolean active = false, explode = false;
    private double xRadius = 0, yRadius;
    private float backgroundBlack;
    private Explosion e1, e2, e3, e4, e5, e6, e7, e8, e9, e10;
    private BufferedImage[] images;
    
    public ExplosionPower(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(new Color(128, 0, 128));
        g.fillRect(0, 0, 32, 32);
        g.dispose();
        BufferedImage[] b = {image};
        animation.setFrames(b);
        setWidth(32);
        setHeight(32);
        initState(new State(getX(), getY(), 0, 0, 0, 1, 10, 0, 0, 4));
        images = new BufferedImage[12];
        BufferedImage imageAnim = null;
        try {
            imageAnim = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/explosion_anim.png"));
        } catch (IOException ex) {
            Logger.getLogger(Explosion.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < images.length; i++) {
            images[i] = imageAnim.getSubimage(134 * i, 0, 134, 134);
        }
    }
    
    public void update() {
        super.update();
        if(active) {
            if(System.currentTimeMillis() - startTime > 3000 && startTime != -1) {
                tileMap.setShaking(false, 0);
                getArea().getHero().setCanMove(true);
                startTime = -1;
            }
            if(System.currentTimeMillis() - ultimateTime > 350) {
                explode = true;
                ultimateTime = System.currentTimeMillis();
            }
            backgroundBlack += 0.0025;
            if(backgroundBlack > 0.7) {
                backgroundBlack = 0.7f;
            }
            ((LevelChoice) ExplosionPower.this.gsm.getState()).setBackground(Color.black, backgroundBlack);
            if(System.currentTimeMillis() - currentTime > 90000) {
                active = false;
                setVisible(true);
                ((LevelChoice) ExplosionPower.this.gsm.getState()).setBackground(null, backgroundBlack);
                ((LevelChoice) gsm.getState()).getLevel().initMusic();
            }
        }
        if(explode) {
            int xh = (int) getArea().getHero().getX(), yh = (int) getArea().getHero().getY();
            e1 = new Explosion(getArea(), (int) getArea().getHero().getX() - 134, (int) getArea().getHero().getY() - 144, 0, images, gsm);
            e2 = new Explosion(getArea(), (int) getArea().getHero().getX() + getArea().getHero().getWidth(), (int) getArea().getHero().getY() - 144, 0, images, gsm);
            e3 = new Explosion(getArea(), (int) getArea().getHero().getX() - 134, (int) getArea().getHero().getY() + getArea().getHero().getHeight(), 0, images, gsm);
            e4 = new Explosion(getArea(), (int) getArea().getHero().getX() + getArea().getHero().getWidth(), (int) getArea().getHero().getY() + getArea().getHero().getHeight(), 0, images, gsm);
            e5 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            e6 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            e7 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            e8 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            e9 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            e10 = new Explosion(getArea(), (int) xh - gsm.getWidth() / 2 + new java.util.Random().nextInt(gsm.getWidth() + xh - xh - getWidth() / 2 + 1), (int) yh - gsm.getHeight() / 2 + new java.util.Random().nextInt(gsm.getHeight() + yh - yh - getHeight() / 2 + 1), 0, images, gsm);
            Obstacle[] objs = {e1, e2, e3, e4, e5, e6, e7, e8, e9, e10};
            getArea().addObjects(objs);
            JukeBox.playAction("explosion");
            explode = false;
        }
        for(int i = 0; i < getArea().objects.size(); i++) {
            if(getArea().objects.get(i) instanceof Explosion) {
                if(((Explosion) getArea().objects.get(i)).canBeRemove()) {
                    getArea().objects.remove(i);
                }
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
        g.setPaint(new GradientPaint(0, 0, new Color(128, 0, 128), (int) xRadius, (int) yRadius, Color.pink, true));
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
            startTime = System.currentTimeMillis();
            backgroundBlack = 0;
            setVisible(false);
            active = true;
            currentTime = System.currentTimeMillis();
            tileMap.setShaking(true, 60);
            getArea().getHero().setCanMove(false);
            ((LevelChoice) gsm.getState()).getLevel().informSpecialTheme();
            JukeBox.play("special");
            JukeBox.setLoopPoints(107785, -1);
        }
    }
}