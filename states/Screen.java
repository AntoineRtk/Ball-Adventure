package states;

import jukebox.JukeBox;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class Screen extends JPanel implements Runnable {
    
    private GameStateManager game;
    private boolean running = false;
    
    public Screen() {
        game = new GameStateManager(this);
        game.setState(GameStateManager.INTRO);
        start();
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Controler.setLeft(true);
                        break;
                    case KeyEvent.VK_RIGHT:
                        Controler.setRight(true);
                        break;
                    case KeyEvent.VK_UP:
                        Controler.setUp(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        Controler.setDown(true);
                        break;
                    case KeyEvent.VK_SPACE:
                        Controler.setSpace(true);
                        break;
                    case KeyEvent.VK_CONTROL:
                        Controler.setControl(true);
                        break;
                    case KeyEvent.VK_ENTER:
                        Controler.setEnter(true);
                        break;
                }
                if(e.getKeyCode() == KeyEvent.VK_F1) {
                    JukeBox.setMute(!JukeBox.mute);
                }
            }
            
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        Controler.setLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        Controler.setRight(false);
                        break;
                    case KeyEvent.VK_UP:
                        Controler.setUp(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        Controler.setDown(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        Controler.setSpace(false);
                        break;
                    case KeyEvent.VK_CONTROL:
                        Controler.setControl(false);
                        break;
                    case KeyEvent.VK_ENTER:
                        Controler.setEnter(false);
                        break;
                }
            }
        });
        setFocusable(true);
    }
    
    public void start() {
        running = true;
        new Thread(this).start();
    }
    
    public void stop() {
        running = false;
        new Thread(this).stop();
    }
    
    public void run() {
        while(running) {
            long currentTime = System.currentTimeMillis();
            game.update();
            long elapsed = currentTime - System.currentTimeMillis();
            try {
                Thread.sleep(20 + -elapsed);
            } catch (InterruptedException ex) {
                Logger.getLogger(Screen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        game.paint(g2d);
        repaint();
    }
}