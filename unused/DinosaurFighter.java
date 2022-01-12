package unused;


import unused.Fighter;
import unused.NormalAttack;
import unused.Attack;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DinosaurFighter extends Fighter {
    
    public DinosaurFighter() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.red);
        g.fillRect(0, 0, 30, 30);
        g.dispose();
        animation.setDelay(-1);
        BufferedImage[] b = {image};
        animation.setFrames(b);
    }
    
    public Attack[] getAttacks() {
        return new Attack[]{new NormalAttack()};
    }
    
    public void updateTurn() {
        
    }
}