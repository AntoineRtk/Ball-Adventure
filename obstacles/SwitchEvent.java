
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

public class SwitchEvent extends Obstacle {
    
    private int id;
    
    public SwitchEvent(AreaZone area, double x, double y, int id, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        try {
            BufferedImage i = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/SwitchEvent.png"));
            BufferedImage[] b = {i};
            animation.setFrames(b);
        } catch (IOException ex) {}
        setSolid(true);
        setWidth(32);
        setHeight(32);
        initState(new State(x, y, 0, 0, 0, 2, 8, 0, 0, 8));
        this.id = id;
    }
    
    public void collides(MapObject object) {
        if(object instanceof Hero) {
            if(object.intersectsOnTop(this) && object.dy > 0) {
                getArea().event(id);
                resize();
            }
        }
    }
    
    private void resize() {
        BufferedImage newImage = resize(animation.getImage());
        Image i = filtre(newImage, new Color(0, 0, 0));
        newImage = convertToBufferedImage(i);
        BufferedImage[] ni = {newImage};
        animation.setFrames(ni);
        setHeight(5);
        setSolid(false);
    }
    
    private BufferedImage resize(BufferedImage b) {
        BufferedImage newImage = new BufferedImage(32, 5, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 32, 5);
        g.drawImage(b, 0, 0, 32, 5, null);
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
}