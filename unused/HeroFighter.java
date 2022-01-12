package unused;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class HeroFighter extends Fighter {
    
    public HeroFighter() {
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 20, 20);
        g.dispose();
        animation.setDelay(-1);
        BufferedImage[] b = {image};
        animation.setFrames(b);
    }
    
    public String getName() {
        return "Vous";
    }
    
    public Attack[] getAttacks() {
        return new Attack[]{new NormalAttack()};
    }
    
    public void updateTurn() {
        
    }
}