
package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import jukebox.JukeBox;
import states.GameStateManager;
import states.LevelChoice;
import states.Main;
import states.levels.CostumLevel;
import tilemap.AreaZone;

public class Flower extends Obstacle {
    
    private boolean ended = false, eL = false, s = true, advancing = true;
    private long currentTime = 0;
    private Ellipse2D ellipse;
    
    public Flower(AreaZone areaZone, int x, int y, int i, GameStateManager gsm) {
        super(areaZone, x, y, i, gsm);
        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Flower.png"));
            BufferedImage[] b = {image};
            animation.setFrames(b);
            setWidth(50);
            setHeight(64);
            ellipse = new Ellipse2D.Double(getX() - 40, getY() - 30, 80, 60);
        } catch (IOException ex) {}
    }
    
    public void update() {
        super.update();
        if(ended) {
            ((LevelChoice) gsm.getState()).getLevel().setCheckPoint(0);
            Main.frame.setSize(Main.frame.getMinimumSize());
            getArea().getHero().setPosition(getX(), getY());
            getArea().getHero().setRotation(0);
            if(System.currentTimeMillis() - currentTime < 6500 && s) {
                setRotation(rotation + 5);
            }
            if(System.currentTimeMillis() - currentTime > 7875) {
                s = false;
                setRotation(0);
                advancing = false;
            }
            if(System.currentTimeMillis() - currentTime > 8000) {
                if(!eL) {
                    BufferedImage image = gsm.getScreen();
                    eL = true;
                    ((LevelChoice) gsm.getState()).setEndAnimation(true, image);
                }
            }
            if(System.currentTimeMillis() - currentTime > 12000) {
                if(((LevelChoice) gsm.getState()).getLevel() instanceof CostumLevel) {
                    gsm.setState(GameStateManager.MENU);
                }
                else {
                    ((LevelChoice) gsm.getState()).setLevel(LevelChoice.WORLD_MAP);
                }
            }
            if(advancing) {
                if(right) {
                    setX(getX() + 3.25);
                }
                if(getX() > ellipse.getX() + 60) {
                    right = false;
                }
                if(!right && getX() < ellipse.getX() - 1) {
                    right = true;
                }
                if(!right) {
                    setX(getX() - 3.25);
                }
                if(up) {
                    setY(getY() + 2);
                }
                if(getY() > ellipse.getY() + 60) {
                    up = false;
                }
                if(!up && getY() < ellipse.getY() - 1) {
                    up = true;
                }
                if(!up) {
                    setY(getY() - 2);
                }
            }
            else {
                setPosition(ellipse.getX() + 30, ellipse.getY() + 30);
                setRotation(0);
            }
        }
    }
    
    private boolean right = true, up;
    
    public void paint(Graphics2D g) {
        super.paint(g);
    }
    
    public void collides(MapObject object) {
        if(object instanceof Hero) {
            if(!ended) {
                getArea().getHero().animation.setFrames(getArea().getHero().animations[2]);
                getArea().getHero().setCanMove(false);
                getArea().getHero().setPosition(getX(), getY());
                getArea().getHero().setRotation(0);
                ended = true;
                currentTime = System.currentTimeMillis();
                JukeBox.play("end");
                ((LevelChoice) gsm.getState()).getLevel().informEndTheme();
            }
        }
    }
}