package unused;


import states.GameStateManager;
import animation.Animation;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class FightStartAnimation extends Animation {
    
    private BufferedImage image, background, star;
    private int x = 0, y = 0, width, height;
    private int starsPoint[][] = new int[8][2];
    private BufferedImage[] starsImage = new BufferedImage[8];
    
    public FightStartAnimation() {
        star = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = star.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Path2D starPath = new Path2D.Double(); 
        starPath.moveTo(star.getWidth()/5, star.getHeight()-1); 
        starPath.lineTo(star.getWidth()/2, 0); 
        starPath.lineTo(star.getWidth()*4/5, star.getHeight()-1); 
        starPath.lineTo(0, 2*star.getHeight()/5); 
        starPath.lineTo(star.getWidth()-1, 2*star.getHeight()/5); 
        starPath.closePath();
        g.setColor(Color.white);
        g.fillRect(0, 0, 100, 100);
        g.setColor(Color.blue);
        g.fill(starPath);
        g.dispose();
        star = transparent(star, new Color(0, 0, 0));
    }
    
    public void updateImage() {
        Graphics2D g = image.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        for(int i = 0; i < starsImage.length; i++) {
            g.drawImage(starsImage[i], starsPoint[i][0], starsPoint[i][1], null);
        }
        Shape s = new Ellipse2D.Double(x, y, width, height);
        g.setClip(s);
        g.drawImage(background, 0, 0, null);
        g.dispose();
        x += 2;
        y += 2;
        width -= 4;
        height -= 4;
        if(x >= GameStateManager.getWidth() / 2 || y >= GameStateManager.getHeight() / 2) {
            setHasPlayedOnce(true);
        }
    }
    
    public void setImage(BufferedImage background) {
        this.background = background;
        image = new BufferedImage(background.getWidth(), background.getHeight(), background.getType());
        width = image.getWidth();
        height = image.getHeight();
        for(int i = 0; i < starsPoint.length; i++) {
            int xx, yy;
            while(true) {
                xx = new java.util.Random().nextInt(image.getWidth());
                yy = new java.util.Random().nextInt(image.getHeight());
                Rectangle r = new Rectangle(xx, yy, 100, 100);
                boolean t = false;
                for(int s = 0; s < starsPoint.length; s++) {
                    Rectangle r1 = new Rectangle(starsPoint[s][0], starsPoint[s][1], 100, 100);
                    if(r.intersects(r1)) {
                        t = true;
                    }
                }
                if(!t) {
                    break;
                }
            }
            starsPoint[i][0] = xx;
            starsPoint[i][1] = yy;
            double rotationRequired = Math.toRadians(new java.util.Random().nextInt(361));
            double locationX = star.getWidth();
            double locationY = star.getHeight();
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX / 2, locationY / 2);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            starsImage[i] = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = starsImage[i].createGraphics();
            g.drawImage(op.filter(star, null), 0, 0, null);
            g.dispose();
        }
        updateImage();
    }
    
    private BufferedImage transparent(BufferedImage b, final Color color) {
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
      return convertToBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    private BufferedImage convertToBufferedImage(Image image) {
        BufferedImage b = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = b.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return b;
    }
}