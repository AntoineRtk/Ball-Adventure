package states;


import java.awt.Graphics2D;

public abstract class GameState {
    
    public GameStateManager gsm;
    
    public GameState(GameStateManager gsm) {
        this.gsm = gsm;
    }
    
    public abstract void update();
    
    public abstract void paint(Graphics2D g);
    
    public int getWidth() {
        return gsm.getWidth();
    }
    
    public int getHeight() {
        return gsm.getHeight();
    }

    public abstract void init();
}