package states;


import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MenuState extends GameState {
    
    private String[] choices = {"Commencer", "Charger une carte", "Quitter"};
    private int currentChoice = 0;
    private boolean rectangleInit = false, select;
    private Font f = new Font("Tahoma", Font.ITALIC, 30), t = new Font("Brush Script Std", Font.BOLD, 70);
    private ArrayList<GameRectangle> rectangles = new ArrayList<>();
    private Color color1, color2;
    private long currentTime = System.currentTimeMillis(), currentChoiceTime;
    private float rad = 1;
    
    public MenuState(GameStateManager gsm) {
        super(gsm);
    }
    
    public void update() {
        if(System.currentTimeMillis() - currentChoiceTime > 150) {
            if(Controler.isUp()) {
                currentChoice--;
                if(currentChoice == -1) {
                    currentChoice = choices.length - 1;
                }
                currentChoiceTime = System.currentTimeMillis();
            }
            if(Controler.isDown()) {
                currentChoice++;
                if(currentChoice >= choices.length) {
                    currentChoice = 0;
                }
                currentChoiceTime = System.currentTimeMillis();
            }
            if(Controler.isEnter()) {
                select = true;
                currentChoiceTime = System.currentTimeMillis();
            }
            else if(!Controler.isEnter()) {
                select = false;
            }
        }
        if(System.currentTimeMillis() - currentTime > 100) {
            rad += 0.65;
            if(rad > 300) {
                rad = 50;
            }
        }
        if(select) {
            if(currentChoice == 0) {
                gsm.setState(GameStateManager.LEVEL);
                LevelChoice l = (LevelChoice) gsm.getState();
                l.setLevel(LevelChoice.WORLD_MAP);
            }
            else if(currentChoice == 1) {
                JFileChooser fc = new JFileChooser();
                fc.setAcceptAllFileFilterUsed(false);
                fc.setFileFilter(new FileNameExtensionFilter("Cartes", "txt"));
                if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    gsm.setState(GameStateManager.LEVEL);
                    LevelChoice l = (LevelChoice) gsm.getState();
                    l.setCostumLevelPath(fc.getSelectedFile().toString());
                    l.setLevel(LevelChoice.COSTUM);
                }
            }
            else if(currentChoice == 2) {
                System.exit(0);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        g.setPaint(new GradientPaint(0, Main.frame.getY(), color1, 0, Main.frame.getY() + 350, color2, true));
        g.fillRect(0, 0, Main.frame.getWidth(), Main.frame.getHeight());
	float[] dist = { 0.1f, 0.2f, 0.3f, 1.0f };
	Color[] colors = {new Color(0, 0, 255), new Color(0, 255, 0), new Color(255, 0, 0), Color.yellow};
        g.setPaint(new RadialGradientPaint(new Point2D.Float(0, 0), rad, new Point2D.Float(0, 0), dist, colors, MultipleGradientPaint.CycleMethod.REPEAT));
        g.setFont(t);
        TextLayout tx = new TextLayout("BallAdventure", g.getFont(), g.getFontRenderContext());
        g.drawString("Ball Adventure", (getWidth()) / 2 - (int) tx.getBounds().getWidth() / 2, 100);
        g.setFont(f);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, Main.frame.getY(), color1, 0, Main.frame.getY() + 350, color2, true));
        FontMetrics font = g.getFontMetrics(f);
        for(int i = 0; i < rectangles.size(); i++) {
            g.fillRect((int) rectangles.get(i).getBounds().getX(), (int) rectangles.get(i).getBounds().getY(), (int) rectangles.get(i).getBounds().getWidth(), (int) rectangles.get(i).getBounds().getHeight());
        }
        for(int i = 0; i < choices.length; i++) {
            g.setColor(Color.red);
            if(currentChoice == i) {
                g.setColor(Color.blue);
            }
            g.drawString(choices[i], (getWidth() - font.stringWidth(choices[i])) / 2, i * 50 + getHeight() / 3);
            if(!rectangleInit) {
                rectangles.add(new GameRectangle(choices[i], (getWidth() - font.stringWidth(choices[i])) / 2, i * 55 + getHeight() / 3 - (int) font.getStringBounds(choices[i], g).getHeight(), f));
            }
        }
        rectangleInit = true;
    }
    
    public void mouseReleased(MouseEvent e) {
        for(int i = 0; i < rectangles.size(); i++) {
            if(rectangles.get(i).isInRectangle()) {
                select = true;
            }
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        for(int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i).update(e);
        }
        for(int i = 0; i < rectangles.size(); i++) {
            if(rectangles.get(i).isInRectangle()) {
                currentChoice = i;
            }
        }
    }
    
    public void init() {
        color1 = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
        color2 = new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256));
    }
    
    public class GameRectangle {

        private String text;
        private boolean inRectangle = false;
        private int x, y, width, height;

        public GameRectangle(String text, int x, int y, Font font) {
            this.text = text;
            this.x = x;
            this.y = y;
            width = (int) font.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth();
            height = (int) font.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getHeight();
        }

        public void update(MouseEvent e) {
            if(getBounds().contains(e.getPoint())) {
                inRectangle = true;
            }
            else {
                inRectangle = false;
            }
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }

        public String getText() {
            return text;
        }

        public boolean isInRectangle() {
            return inRectangle;
        }
    }
}