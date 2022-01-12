
package animation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import tilemap.TileMap;

public class StarAnimation {
    
    private TileMap tileMap;
    private double x, y, dx = 0, dy = 0;
    private BufferedImage star;
    private int count = 0;
    private boolean canBeRemoved = false;
    
    public StarAnimation(TileMap t, double x, double y, int dir) {
        this.tileMap = t;
        this.x = x;
        this.y = y;
        switch(dir) {
            case 0:
                dy = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 1:
                dx = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                dy = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 2:
                dx = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 3:
                dx = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                dy = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 4:
                dy = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 5:
                dx = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                dy = (5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 6:
                dx = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
            case 7:
                dx = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                dy = -(5 + new java.util.Random().nextInt(20 - 5 + 1));
                break;
        }
        dx = dx / 10;
        dy = dy / 10;
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Path2D starPath = new Path2D.Double(); 
        starPath.moveTo(image.getWidth()/5, image.getHeight()-1); 
        starPath.lineTo(image.getWidth()/2, 0); 
        starPath.lineTo(image.getWidth()*4/5, image.getHeight()-1); 
        starPath.lineTo(0, 2*image.getHeight()/5); 
        starPath.lineTo(image.getWidth()-1, 2*image.getHeight()/5); 
        starPath.closePath();
        g.setColor(Color.white);
        g.fillRect(0, 0, 100, 100);
        g.setColor(new Color(new java.util.Random().nextInt(256), new java.util.Random().nextInt(256), new java.util.Random().nextInt(256)));
        g.fill(starPath);
        g.dispose();
        BufferedImage b = resize(image);
        Image i = filtre(b, new Color(0, 0, 0));
        star = convertToBufferedImage(i);
    }
    
    public void update() {
        this.x += dx;
        this.y += dy;
        count++;
        if(count > 40) {
            canBeRemoved = true;
        }
    }
    
    public boolean canBeRemoved() {
        return canBeRemoved;
    }
    
    private BufferedImage resize(BufferedImage b) {
        BufferedImage newImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 10, 10);
        g.drawImage(b, 0, 0, 10, 10, null);
        g.dispose();
        return newImage;
    }
    
    private static BufferedImage convertToBufferedImage(final Image image)  
   {  
      final BufferedImage bufferedImage =  
         new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);  
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
    
    public void paint(Graphics2D g) {
        g.drawImage(star, tileMap.getX() + (int) x, tileMap.getY() + (int) y, null);
    }
}