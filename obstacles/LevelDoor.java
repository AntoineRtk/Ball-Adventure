package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import states.GameSave;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class LevelDoor extends Obstacle {
    
    private int index, flowers;
    private BufferedImage[][] animations = new BufferedImage[2][];
    private BufferedImage[] images = new BufferedImage[3];
    private long currentTime = -1;
    
    public LevelDoor(AreaZone area, int x, int y, int index, int flowers, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        this.index = index;
        this.flowers = flowers;
        this.flowers = 0;
        try {
            BufferedImage img = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Door.png"));
            for(int w = 0; w < img.getWidth(); w += 34) {
                BufferedImage sub = img.getSubimage(w, 0, 34, 60);
                images[w / 34] = sub;
            }
            animations[0] = new BufferedImage[1];
            animations[0][0] = images[0];
            animations[1] = new BufferedImage[images.length + 2];
            for(int i = 0; i < images.length; i++) {
                animations[1][i] = images[i];
                System.out.println(i+" "+animations[1].length);
            }
            animations[1][3] = images[1];
            animations[1][4] = images[0];
            animation.setFrames(animations[0]);
            initState(new State(x, y, 0, 0, 0, 2, 10, 0, dx, 2));
            setWidth(34);
            setHeight(60);
        } catch (IOException ex) {
            Logger.getLogger(Door.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void update() {
        super.update();
        if(animation.getFrames() == animations[1]) {
            if(animation.getFrameIndex() == animation.getFrames().length / 2 + 1) getArea().getHero().setVisible(false);
            getArea().getHero().setY((tileMap.getRowTile((int) (getArea().getHero().getY() + getArea().getHero().getHeight() - Math.round(getArea().getHero().dy) - 8)) + 1) * tileMap.getTileSize() - getArea().getHero().getHeight());
        }
        if(animation.hasPlayedOnce() && animation.getFrames() == animations[1]) {
            animation.setFrames(animations[0]);
            LevelChoice level = (LevelChoice) gsm.getState();
            getArea().getHero().setCanMove(true);
            getArea().getHero().setVisible(true);
            level.setLevel(index + 2);
        }
        if(currentTime != -1) {
            if(System.currentTimeMillis() - currentTime > 3000) {
                currentTime = -1;
            }
        }
    }
    
    public void paint(Graphics2D g) {
        g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), getWidth(), getHeight(), null);
        if(currentTime != -1) {
            g.setFont(new Font("Lucida Console", Font.BOLD, 15));
            TextLayout txt = new TextLayout("Vous n'avez pas assez de fleurs pour ouvrir cette porte.", g.getFont(), g.getFontRenderContext());
            g.setColor(Color.red);
            g.drawString("Vous n'avez pas assez de fleurs pour ouvrir cette porte.", (gsm.getWidth() - (int) txt.getBounds().getWidth()) / 2, 250);
        }
    }
    
    public void collides(MapObject object) {
        if(object.getClass().getName().equals(Hero.class.getName())) {
            Hero hero = (Hero) object;
            if(GameSave.getFlowers() >= flowers) {
                if(hero.up && animation.getFrames() != animations[1]) {
                    hero.up = false;
                    hero.setCanMove(false);
                    animation.setDelay(200);
                    animation.setFrames(animations[1]);
                }
            }
            else {
                currentTime = System.currentTimeMillis();
            }
        }
    }
}