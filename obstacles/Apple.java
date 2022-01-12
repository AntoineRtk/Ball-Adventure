
package obstacles;

import animation.StarAnimation;
import entity.Hero;
import entity.MapObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.State;
import tilemap.AreaZone;

public class Apple extends Obstacle {
    
    private boolean regenerating = false;
    private long regenerateTime = 0;
    private int regenerated = 0;
    private ArrayList<StarAnimation> stars = new ArrayList<StarAnimation>();
    
    public Apple(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        try {
            BufferedImage[] b = {ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Apple.png"))};
            animation.setFrames(b);
        } catch (IOException ex) {}
        setWidth(16);
        setHeight(16);
        initState(new State(x, y, 0, 0, 0, 2, 20, 0, 0, 30));
    }
    
    public void update() {
        super.update();
        if(regenerating) {
            if(System.currentTimeMillis() - regenerateTime > 350) {
                if(getArea().getHero().getLife() != getArea().getHero().getMaxLife()) {
                    getArea().getHero().setLife((int) (getArea().getHero().getLife() + 1));
                }
                regenerated++;
                regenerateTime = System.currentTimeMillis();
                for(int i = 0; i < 5; i++) {
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 0));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 1));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 2));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 3));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 4));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 5));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 6));
                    stars.add(new StarAnimation(tileMap, getArea().getHero().getX(), getArea().getHero().getY(), 7));
                }
                if(regenerated == 5) {
                    regenerating = false;
                }
            }
        }
        for(int i = 0; i < stars.size(); i++) {
            stars.get(i).update();
            if(stars.get(i).canBeRemoved()) {
                stars.remove(i);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        for(int i = 0; i < stars.size(); i++) {
            stars.get(i).paint(g);
        }
    }
    
    public void collides(MapObject object) {
        if(object instanceof Hero) {
            if(isVisible()) {
                setVisible(false);
                regenerating = true;
            }
        }
    }
}