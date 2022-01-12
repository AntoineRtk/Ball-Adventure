package states;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class IntroScreen extends GameState {
    
    private long currentTimeMillis = System.currentTimeMillis();
    private float opacity = 1;
    private boolean skip = false;
    private BufferedImage barunson, nintendo;
    
    public IntroScreen(GameStateManager gsm) {
        super(gsm);
    }
    
    public void update() {
        if(System.currentTimeMillis() - currentTimeMillis > 5000 || skip) {
            if((opacity - 0.007) > 0) {
                opacity -= 0.007;
            }
            else {
                opacity = 0;
                gsm.setState(GameStateManager.MENU);
            }
        }
    }
    
    public void paint(Graphics2D g) {
        g.setColor(Color.white);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.yellow);
        g.setFont(new Font("Arial", Font.BOLD, 90));
        TextLayout ba = new TextLayout("BallAdventure", g.getFont(), g.getFontRenderContext());
        g.drawString("Ball Adventure", 10, 100);
        g.setColor(Color.cyan);
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        g.drawString("Osoroshi", (int) ba.getBounds().getWidth() - 50, (int) ba.getBounds().getHeight() + 80);
        g.setColor(Color.green);
        g.drawString("Benkrafter", (int) ba.getBounds().getWidth() - 20, (int) ba.getBounds().getHeight() + 100);
        g.translate(0, 100);
        g.drawImage(barunson, 100, 100, 200, 200, null);
        g.drawImage(nintendo, 400, 100, 200, 200, null);
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        String s1 = "(C) Barunson Interactive 2008-2011. Aucune violation du droit d'auteur prévue. L'allocation est faite pour «utilisation équitable» à des fins";
        String s2 = "telles que la critique, le commentaire, les rapports de nouvelles, d'enseignement, d'érudition et de la recherche. L'utilisation équitable";
        String s3 = "est une utilisation permise par la loi du droit d'auteur qui pourraient autrement être atteinte. À but non lucratif, d'enseignement ou de";
        String s4 = "personnel inscrit la marque en faveur de l'auteur. Certaines musiques du jeu sont prises du jeu \"Dragonica\" edité par Gala Networks";
        String s5 = "Europe en Europe et publié par GRAVITY GAMES. Le jeu était ouvert aux personnes possédantes d'un compte GPotato sur le site";
        String s6 = "européen de Gala Networks Europe.";
        TextLayout txt1 = new TextLayout(s1, g.getFont(), g.getFontRenderContext());
        TextLayout txt2 = new TextLayout(s2, g.getFont(), g.getFontRenderContext());
        TextLayout txt3 = new TextLayout(s3, g.getFont(), g.getFontRenderContext());
        TextLayout txt4 = new TextLayout(s4, g.getFont(), g.getFontRenderContext());
        TextLayout txt5 = new TextLayout(s5, g.getFont(), g.getFontRenderContext());
        g.drawString(s1, 10, 350);
        g.drawString(s2, 10, 350 + (int) txt1.getBounds().getHeight());
        g.drawString(s3, 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight()));
        g.drawString(s4, 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight() + txt3.getBounds().getHeight()));
        g.drawString(s5, 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight() + txt3.getBounds().getHeight() + txt4.getBounds().getHeight()));
        g.drawString(s6, 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight() + txt3.getBounds().getHeight() + txt4.getBounds().getHeight() + txt5.getBounds().getHeight()));
        g.setColor(Color.blue);
        g.drawString("Aucune violation aux regards des droits d'auteur de Nintendo et de Barunson Interactive n'a été enfreinte.", 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight() + txt3.getBounds().getHeight() + txt4.getBounds().getHeight() + txt5.getBounds().getHeight()) + 25);
        g.drawString("A chaque changement de musique, le nom ainsi que son apparition dans le jeu orginal sera affiché en bas à droite.", 10, 350 + (int) (txt1.getBounds().getHeight() + txt2.getBounds().getHeight() + txt3.getBounds().getHeight() + txt4.getBounds().getHeight() + txt5.getBounds().getHeight()) + 50);
        g.setColor(Color.black);
        g.drawString("Ce jeu dispose d'une sauvegarde automatique.", 5, 550);
    }
    
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE) {
            skip = true;
        }
    }
    
    public void init() {
        try {
            barunson = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Barunson.png"));
            nintendo = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Nintendo.png"));
        } catch (IOException ex) {}
    }
}