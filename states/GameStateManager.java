package states;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GameStateManager {
    
    public ArrayList<GameState> states = new ArrayList<>();
    public final static int INTRO = 0, MENU = 1, LEVEL = 2;
    private int index = -1;
    private static JPanel panel;
    
    public GameStateManager(JPanel panel) {
        this.panel = panel;
        states.add(new IntroScreen(this));
        states.add(new MenuState(this));
        states.add(new LevelChoice(this));
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setState(int index) {
        this.index = index;
        init();
    }
    
    public void init() {
        if(index != -1) {
            states.get(index).init();
        }
    }
    
    public void update() {
        if(index != -1) {
            states.get(index).update();
        }
    }
    
    public void paint(Graphics2D g2d) {
        if(index != -1) {
            states.get(index).paint(g2d);
        }
    }
    
    public static int getWidth() {
        return panel.getWidth();
    }
    
    public static int getHeight() {
        return panel.getHeight();
    }

    public GameState getState() {
        return states.get(index);
    }
    
    public GameState getState(int index) {
        return states.get(index);
    }
    
    public void initState(int index) {
        states.get(index).init();
    }

    public BufferedImage getScreen() {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        panel.paint(g);
        g.dispose();
        return image;
    }
    
    public void addKeyListener(KeyAdapter keyAdapter) {
        panel.addKeyListener(keyAdapter);
    }
    
    public void removeKeyListener(KeyAdapter keyAdapter) {
        panel.removeKeyListener(keyAdapter);
    }
}