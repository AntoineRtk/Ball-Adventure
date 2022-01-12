
package obstacles;

import entity.Hero;
import entity.MapObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import javax.imageio.ImageIO;
import states.GameStateManager;
import states.State;
import tilemap.AreaZone;

public class Branch extends Obstacle {
    
    private BufferedImage[] images, animations;
    private boolean hasFinished = false, up = false, down = false;
    private double h = 0;
    private long currentTime;
    
    public Branch(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setWidth(20);
        try {
            BufferedImage image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Branch.png"));
            BufferedImage[] b = {image.getSubimage(0, 0, 20, 50), image.getSubimage(20, 0, 20, 50), image.getSubimage(40, 0, 20, 50)};
            BufferedImage[] b1 = {image.getSubimage(0, 0, 20, 50), image.getSubimage(20, 0, 20, 50), image.getSubimage(40, 0, 20, 50)};
            images = b1;
            animations = b;
        } catch (IOException e) {}
        animation.setDelay(200);
        animation.setFrames(animations);
        initState(new State(x, y, 0, 0, 0, 5, 10, 0, 0, 5));
    }
    
    public void paint(Graphics2D g) {
        if(!isVisible()) return;
        g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (tileMap.getColTile((int) getY() + getHeight() + 5) * tileMap.getTileSize()) - getHeight(), null);
    }
    
    public void update() {
        if(up) {
            if(getHeight() < 65) {
                h += 0.675;
                setHeight((int) h);
                setY(getY() - 0.675);
            }
            else {
                if(getHeight() != 65) {
                    setHeight(65);
                    setY(tileMap.getColTile((int) getY() + getHeight() + 1) * 32 - getHeight());
                }
                if(currentTime == -1) {
                    currentTime = System.currentTimeMillis();
                    animation.setFrame(1);
                    animation.setDelay(-1);
                }
                if(System.currentTimeMillis() - currentTime > 3000) {
                    up = false;
                    down = true;
                    animation.setDelay(200);
                }
            }
        }
        if(down) {
            h -= 0.675;
            if(h > 1) {
                setHeight((int) h);
                setY(getY() + 0.675);
            }
            else {
                setHeight(1);
                hasFinished = true;
                down = false;
            }
        }
        super.update();
    }
    
    public void setHeight(int h) {
        super.setHeight(h);
        if(animations == null) return;
        BufferedImage b = resize(images[animation.getFrameIndex()], h);
        Image im = filtre(b, new Color(0, 0, 0));
        BufferedImage c = convertToBufferedImage(im);
        animations[animation.getFrameIndex()] = c;
    }
    
    public void collides(MapObject object) {
        if(object instanceof Hero && isVisible()) {
            if(getX() >= object.getX()) {
                ((Hero) object).hit(4, 0);
            }
            else {
                ((Hero) object).hit(4, 0);
            }
        }
    }
    
    private BufferedImage resize(BufferedImage b, int h) {
        BufferedImage newImage = new BufferedImage(getWidth(), h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), h);
        g.drawImage(b, 0, 0, getWidth(), h, null);
        g.dispose();
        return newImage;
    }
    
    private static BufferedImage convertToBufferedImage(final Image image)  
   {  
      final BufferedImage bufferedImage =  
         new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);  
      final Graphics2D g2 = bufferedImage.createGraphics();  
      g2.drawImage(image, 0, 0, null);  
      g2.dispose();  
      return bufferedImage;  
    }
    
    private Image filtre(BufferedImage b, final Color color) {
        final ImageFilter filter = new RGBImageFilter()
        {
         // the color we are looking for (white)... Alpha bits are set to opaque
         public int markerRGB = color.getRGB() | 0xFFFFFFFF;
         public final int filterRGB(final int x, final int y, final int rgb)
         {
            if ((rgb | 0xFF000000) == markerRGB)
            {
               // Mark the alpha bits as zero - transparent
               return 0x00FFFFFF & rgb;
            }
            else
            {
               // nothing to do  
               return rgb;
            }
         }
      };
      final ImageProducer ip = new FilteredImageSource(b.getSource(), filter);
      return Toolkit.getDefaultToolkit().createImage(ip);
    }
    
    public void grows() {
        hasFinished = false;
        up = true;
        down = false;
        h = 1;
        currentTime = -1;
        setHeight(1);
        double ypos = getArea().getHero().getY();
        while(true) {
            ypos += 32;
            if(tileMap.getTile(tileMap.getRowTile((int) getArea().getHero().getX()), tileMap.getColTile((int) ypos)).isSolid()) {
                ypos /= 32;
                ypos *= 32;
                ypos -= 32;
                break;
            }
        }
        setPosition(getArea().getHero().getX(), ypos);
    }

    public boolean hasFinishedOperation() {
        return hasFinished;
    }
}