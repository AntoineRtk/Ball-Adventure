
package animation;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import states.GameStateManager;
import tilemap.TileMap;

public class BossExplosion {
    
    private Ellipse2D ellipse;
    private TileMap tileMap;
    private double speed = 2.5;
    private double bx, by, bw, bh;
    private int count = 0, rad = 0;
    private boolean finished = false;
    
    public BossExplosion(double x, double y, int w, int h, TileMap tileMap) {
        this.bx = x;
        this.by = y;
        this.bw = w;
        this.bh = h;
        this.tileMap = tileMap;
        ellipse = new Ellipse2D.Double(x, y, w, h);
    }
    
    public void update() {
        ellipse.setFrame(new Rectangle2D.Double(ellipse.getX() - speed, ellipse.getY() - speed, ellipse.getWidth() + speed * 2, ellipse.getHeight() + speed * 2));
        if(ellipse.getX() < bx - GameStateManager.getWidth() / 2 && ellipse.getX() + ellipse.getWidth() > bx + GameStateManager.getWidth() / 2 * 2) {
            speed *= 1.5;
            ellipse = new Ellipse2D.Double(bx, by, bw, bh);
            count++;
            rad = new java.util.Random().nextInt(31);
            if(count > 7) {
                speed /= 1.5;
            }
        }
        if(count == 20) {
            finished = true;
        }
        rad += new java.util.Random().nextInt(5);
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public void paint(Graphics2D g) {
        Composite c = g.getComposite();
        g.setPaint(new GradientPaint(rad, rad / 2, Color.pink, rad / 2, rad, Color.magenta, true));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        AffineTransform t = g.getTransform();
        g.translate(tileMap.getX(), tileMap.getY());
        g.fill(ellipse);
        g.setTransform(t);
        g.setPaint(null);
        g.setComposite(c);
    }
}