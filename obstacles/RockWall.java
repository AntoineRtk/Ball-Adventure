
package obstacles;

import entity.Enemy;
import entity.MapObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import obstacles.Obstacle;
import states.GameStateManager;
import tilemap.AreaZone;

public class RockWall extends Obstacle {
    
    private int pixelRGB;
    private GeneralPath path;
    private boolean used;
    
    public RockWall(AreaZone area, int x, int y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        setWidth(30);
        setHeight(70);
        path = new GeneralPath();
        path.moveTo(15, 0);
        path.lineTo(30, 70);
        path.lineTo(0, 70);
        path.closePath();
    }
    
    public void setPixelRGB(int rgb) {
        this.pixelRGB = rgb;
        BufferedImage b = new BufferedImage(30, 70, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = b.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 30, 70);
        g.setClip(path);
        g.setColor(new Color(pixelRGB));
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fill(path);
        g.dispose();
        Image i = filtre(b, new Color(0, 0, 0));
        b = convertToBufferedImage(i);
        BufferedImage[] frames = new BufferedImage[1];
        frames[0] = b;
        animation.setFrames(frames);
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
    
    private void setUsed(boolean used) {
        this.used = used;
    }
    
    public boolean isUsed() {
        return used;
    }
    
    public void collides(MapObject object) {
        if(object instanceof Enemy) {
            ((Enemy) object).hit(3);
            setUsed(true);
        }
    }
}