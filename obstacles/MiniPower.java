
package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import jukebox.JukeBox;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class MiniPower extends Obstacle {
    
    private long currentTime = 0;
    private boolean active = false;
    private double xRadius = 0, yRadius;
    
    public MiniPower(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.blue);
        g.fillRect(0, 0, 5, 5);
        g.dispose();
        BufferedImage[] b = {image};
        animation.setFrames(b);
        setWidth(5);
        setHeight(5);
        initState(new State(getX(), getY(), 0, 0, 0, 1, 10, 0, 0, 4));
    }
    
    public void update() {
        super.update();
        if(active) {
            if(System.currentTimeMillis() - currentTime > 50000) {
                if(!canGoUp()) return;
                getArea().getHero().rejump();
                getArea().getHero().setWidth(40);
                getArea().getHero().setHeight(40);
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
    
    public boolean canGoUp() {
        Hero h = getArea().getHero();
        return !h.tileMap.getTile(tileMap.getRowTile((int) h.getX() + h.getWidth() / 2), tileMap.getColTile((int) h.getY()) - 1).isSolid() &&
                !h.tileMap.getTile(tileMap.getRowTile((int) h.getX() + h.getWidth()) + 1, tileMap.getColTile((int) h.getY() + h.getHeight() / 2)).isSolid() &&
                !h.tileMap.getTile(tileMap.getRowTile((int) h.getX()) - 1, tileMap.getColTile((int) h.getY() + h.getHeight() / 2)).isSolid();
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(!active) return;
        g.setPaint(new GradientPaint(0, 0, Color.blue, (int) xRadius, (int) yRadius, Color.cyan, true));
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
            getArea().getHero().setWidth(10);
            getArea().getHero().setHeight(10);
            ((LevelChoice) gsm.getState()).getLevel().informSpecialTheme();
            JukeBox.play("special");
            JukeBox.setLoopPoints(107785, -1);
        }
    }
}