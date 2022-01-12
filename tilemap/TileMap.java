package tilemap;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import states.Main;

public class TileMap {
    
    private Tile[][] tile;
    private int[][] map;
    private BufferedImage[] tiles;
    private int mapWidth, mapHeight, size, x, y, intensity, rotation;
    private boolean shaking = false, cameraBlocked = false;
    
    public TileMap(int size) {
        this.size = size;
    }
    
    public void loadTiles(String path, boolean in) {
        try {
            BufferedImage image;
            if(in) {
                image = ImageIO.read(TileMap.class.getClassLoader().getResource("images/"+path));
            }
            else {
                image = ImageIO.read(new File(path));
            }
            tiles = new BufferedImage[image.getWidth() / 32 * image.getHeight() / 32];
            int t = 0;
            for(int col = 0; col < image.getWidth() / 32; col++) {
                for(int row = 0; row < image.getHeight() / 32; row++) {
                    tiles[t] = resize(image.getSubimage(row * 32, col * 32, 32, 32), getTileSize());
                    Image i = filtre(tiles[t], new Color(0, 0, 0));
                    tiles[t] = convertToBufferedImage(i);
                    t++;
                }
            }
            System.out.println(t+" "+tiles.length);
            initMap();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
    
    public void initMap() {
        if(map == null) {
            return;
        }
        for(int col = 0; col < map.length; col++) {
            for(int row = 0; row < map[col].length; row++) {
                try {
                    tile[col][row].setImage(tiles[tile[col][row].getID()]);
                } catch (ArrayIndexOutOfBoundsException ex) {}
            }
        }
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
    
    public BufferedImage resize(BufferedImage image, int size) {
        BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, size, size, null);
        g.dispose();
        return resizedImage;
    }
    
    public int getTileSize() {
        return size;
    }
    
    public void loadMap(String t) {
        try {
            BufferedReader br = new BufferedReader(new StringReader(t));
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            map = new int[mapHeight][mapWidth];
            tile = new Tile[mapHeight][mapWidth];
            for(int col = 0; col < mapHeight; col++) {
                String line = br.readLine();
                String[] tokens = line.split(" ");
                System.out.println(line);
                for(int row = 0; row < mapWidth; row++) {
                    int id = Integer.parseInt(tokens[row]);
                    map[col][row] = id;
                    if(tiles == null) {
                        if(id == 0) {
                            tile[col][row] = new Tile(null, row * getTileSize(), col * getTileSize(), 0);
                        }
                        else {
                            tile[col][row] = new Tile(null, row * getTileSize(), col * getTileSize(), id);
                        }
                    }
                    else {
                        if(id == 0) {
                            tile[col][row] = new Tile(tiles[id], row * getTileSize(), col * getTileSize(), id);
                        }
                        else {
                            tile[col][row] = new Tile(tiles[id], row * getTileSize(), col * getTileSize(), id);
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } catch (IOException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Le fichier carte n'est pas valide.", "Ball Adventure", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }
    
    public void update() {
        if(shaking) {
            this.x += Math.random() * intensity - intensity / 2;
            this.y += Math.random() * intensity - intensity / 2;
        }
    }
    
    public void setTileY(int x, int y, int toY) {
        try {
            System.out.println(x+" : "+y+" : "+toY);
            if(tile[toY][x].getID() == 0 && map[toY][x] == 0 && tile[y][x].getID() != 0 && map[y][x] != 0) {
                tile[toY][x] = tile[y][x];
                map[toY][x] = map[y][x];
                tile[y][x].setID(0);
                map[y][x] = 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    public boolean isShaking() {
        return shaking;
    }
    
    public int getIntensity() {
        return intensity;
    }
    
    public boolean isSolid(int x, int y) {
        return getTile(x, y).isSolid();
    }
    
    public boolean isWater(int x, int y) {
        return getTile(x, y).isWater();
    }
    
    public void setShaking(boolean shaking, int intensity) {
        this.shaking = shaking;
        this.intensity = intensity;
    }
    
    public int getRowTile(int x) {
        return x / getTileSize();
    }
    
    public int getColTile(int y) {
        return y / getTileSize();
    }
    
    public Tile getTile(int x, int y) {
        try {
            return tile[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return new Tile(tiles[0], 0, 0, 0);
        }
    }
    
    public void setX(int x) {
        this.x = x;
        if(x > 0) {
            this.x = 0;
        }
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getMapWidth() {
        return mapWidth;
    }
    
    public int getMapHeight() {
        return mapHeight;
    }
    
    public void paint(Graphics2D g) {
        g.setColor(new Color(128, 255, 255));http://higherorderfun.com/blog/2012/05/20/the-guide-to-implementing-2d-platformers/
        g.fillRect(0, 0, Main.frame.getWidth(), Main.frame.getHeight());
        if(map == null) {
            return;
        }
        for(int col = 0; col < map.length; col++) {
            for(int row = 0; row < map[col].length; row++) {
                if(map[col][row] != 0) {
                    if(map[col][row] > 0 && tiles == null) {
                        g.setColor(Color.green);
                        g.fillRect(row * getTileSize() + getX(), col * getTileSize() + getY(), getTileSize(), getTileSize());
                    }
                    if(tiles != null) {
                        if(map[col][row] != 0 && tile[col][row].getID() != 0) {
                            BufferedImage image = tile[col][row].getImage();
                            g.drawImage(image, tile[col][row].getX() + getX(), tile[col][row].getY() + getY(), null);
                            if(1 == 2) {
                                double rotationRequired = Math.toRadians(rotation);
                                double locationX = image.getWidth();
                                double locationY = image.getHeight();
                                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX / 2, locationY / 2);
                                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                            }
                        }
                    }
                }
                if(1 == 2) {
                    //Draw the grid
                    g.setColor(Color.blue);
                    g.drawLine(getX() + row * getTileSize(), 0, getX() + row * getTileSize(), getY() + mapHeight * getTileSize());
                    g.drawLine(0, getY() + col * getTileSize(), mapWidth * getTileSize() + getX(), getY() + col * getTileSize());
                }
            }
        }
    }

    public void setPosition(int x, int y) {
        if(cameraBlocked) return;
        setX(x);
        setY(y);
    }
    
    public boolean isBlocked(int x, int y) {
        return getTile(x, y).isSolid();
    }
    
    public Point getPosition() {
        return new Point(x, y);
    }
    
    public void blockCamera() {
        cameraBlocked = true;
    }
    
    public void deblockCamera() {
        cameraBlocked = false;
    }
}