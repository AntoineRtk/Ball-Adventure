package unused;


import animation.Animation;
import unused.Attack;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class Fighter {
    
    private String name;
    private int x, y, dx, dy, life;
    private boolean bot;
    private ArrayList<Attack> attacks;
    public Animation animation = new Animation();
    private int attackIndex = 0;
    
    public void update() {
        animation.update();
    }
    
    public BufferedImage getImage() {
        return animation.getImage();
    }
    
    public int getWidth() {
        return animation.getImage().getWidth();
    }
    
    public int getHeight() {
        return animation.getImage().getHeight();
    }
    
    public void keyPressed(int k) {
        if(bot) return;
    }

    public void keyReleased(int k) {
        if(bot) return;
    }
    
    public abstract Attack[] getAttacks();
    
    private void attack(int i) {
        attacks.get(i).attack();
    }
    
    public void paint(Graphics2D g) {
        if(bot) return;
        g.setColor(Color.black);
        g.drawOval(getX() - (getWidth() / 2 + 35), getY() - getHeight() - 20, getWidth() + getWidth() / 2 + 70, getHeight());
    }
    
    public void setBot(boolean bot) {
        this.bot = bot;
    }
    
    public void updateTurn() {
        
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public void setY(int y) {
        this.y = y;
    }
}