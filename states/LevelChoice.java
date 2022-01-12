package states;

import entity.Hero;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import states.levels.CostumLevel;
import states.levels.Level1;
import states.levels.Level2;
import states.levels.Level3;
import states.levels.Level4;
import states.levels.WorldMap;

public class LevelChoice extends GameState {
    
    public final static int COSTUM = 0, WORLD_MAP = 1, LEVEL1 = 2, LEVEL2 = 3, LEVEL3 = 4, LEVEL4 = 5;
    private int index = -1;
    private double xP, yP;
    private boolean endAnimation = false;
    private ArrayList<GameLevelState> levels = new ArrayList<>();
    private CostumLevel level = new CostumLevel(gsm);
    private BufferedImage screen;
    
    public LevelChoice(GameStateManager gsm) {
        super(gsm);
        levels.add(level);
        levels.add(new WorldMap(LevelChoice.this.gsm));
        levels.add(new Level1(LevelChoice.this.gsm));
        levels.add(new Level2(LevelChoice.this.gsm));
        levels.add(new Level3(LevelChoice.this.gsm));
        levels.add(new Level4(LevelChoice.this.gsm));
    }
    
    public void update() {
        if(index != -1) {
            levels.get(index).update();
        }
        if(endAnimation) {
            xP += 1.75;
            yP += 1.75;
        }
    }
    
    public void paint(Graphics2D g) {
        if(index != -1) {
            levels.get(index).paint(g);
            if(endAnimation) {
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setClip(new Ellipse2D.Double(xP, yP, getWidth() - (int) xP * 2, getHeight() - (int) yP * 2));
                g.drawImage(screen, 0, 0, null);
            }
        }
    }
    
    public void init() {
        if(index != -1) {
            levels.get(index).init();
        }
    }
    
    public void setLevel(int index) {
        this.levels.get(index).clearAll();
        this.index = index;
        this.setEndAnimation(false, null);
        this.levels.get(index).clearAll();
        init();
    }
    
    public GameLevelState getLevel() {
        return levels.get(index);
    }
    
    public void restart() {
        levels.get(index).init();
    }
    
    public Hero getHero() {
        return levels.get(index).hero;
    }
    
    public void setCostumLevelPath(String path) {
        level.setPath(path);
    }

    public void setBackground(Color cb, float fb) {
        this.levels.get(index).setBackground(cb, fb);
    }
    
    public void setEndAnimation(boolean b, BufferedImage screen) {
        this.endAnimation = b;
        if(b) {
            this.levels.get(index).setCheckPoint(0);
            xP = 0;
            yP = 0;
            this.screen = screen;
        }
    }
}