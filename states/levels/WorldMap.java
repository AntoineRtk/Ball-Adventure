package states.levels;

import entity.Hero;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import javax.swing.JOptionPane;
import states.GameLevelState;
import states.GameStateManager;

public class WorldMap extends GameLevelState {
    
    public WorldMap(GameStateManager gsm) {
        super(gsm);
    }
    
    public void paint(Graphics2D g) {
        super.paint(g);
        if(!isLoading()) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            TextLayout layout = new TextLayout("Monde 1", g.getFont(), g.getFontRenderContext());
            g.setPaint(new GradientPaint(0, 0, Color.green, 20, (int) layout.getBounds().getHeight(), Color.red, true));
            g.drawString("Monde 1", (getWidth() - (int) layout.getBounds().getWidth()) / 2, 100);
        }
    }
    
    public void init() {
        setLoading(true);
        readMap("WorldMap.txt", true, "tiles.png", true);
        JOptionPane.showMessageDialog(null, 1);
        hero = new Hero(gsm, areas[0], 32, 500, 0);
        areas[0].setHero(hero);
        setMapPositions();
    }
}