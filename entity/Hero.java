package entity;

import obstacles.RockWall;
import animation.DustExplosion;
import jukebox.JukeBox;
import states.LevelChoice;
import states.GameStateManager;
import states.State;
import obstacles.Obstacle;
import unused.FightStartAnimation;
import unused.Wave;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import states.Controler;
import tilemap.AreaZone;

public class Hero extends MapObject {
    
    private GameStateManager gsm;
    private State s;
    public boolean flinching = false, straight = false, vibration = false, groundAttack = false, canMove = true, invincible;
    private long flinchTime = System.currentTimeMillis(), lifeTime, vibrationTime, groundAttackTime = 0;
    private double life, maxLife;
    private double basicRotation, basicX, basicY, turn, pxl, pxr;
    private FightStartAnimation fightAnimation = new FightStartAnimation();
    private ArrayList<Wave> waves = new ArrayList();
    public ArrayList<DustExplosion> dusts;
    private RockWall leftW, rightW;
    public BufferedImage[][] animations;
    private BufferedImage imageNormal, imageWater, imageHappy;
    
    public Hero(GameStateManager gsm, AreaZone area, int x, int y, int instantID) {
        super(area, x, y, instantID);
        this.dusts = new ArrayList<>();
        this.gsm = gsm;
        init();
    }
    
    public void init() {
        animations = new BufferedImage[3][];
        try {
            imageNormal = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Hero.png"));
            BufferedImage flipped1 = new BufferedImage(
                imageNormal.getWidth(),
                imageNormal.getHeight(),
                imageNormal.getType());
            AffineTransform tran1 = AffineTransform.getTranslateInstance(imageNormal.getWidth(), 0);
            AffineTransform flip1 = AffineTransform.getScaleInstance(-1d, 1d);
            tran1.concatenate(flip1);
            Graphics2D g1 = flipped1.createGraphics();
            g1.setTransform(tran1);
            g1.drawImage(imageNormal, 0, 0, null);
            g1.dispose();
            imageNormal = flipped1;
            imageWater = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/HeroWater.png"));
            BufferedImage flipped2 = new BufferedImage(
                imageWater.getWidth(),
                imageWater.getHeight(),
                imageWater.getType());
            AffineTransform tran2 = AffineTransform.getTranslateInstance(imageWater.getWidth(), 0);
            AffineTransform flip2 = AffineTransform.getScaleInstance(-1d, 1d);
            tran2.concatenate(flip2);
            Graphics2D g2 = flipped2.createGraphics();
            g2.setTransform(tran2);
            g2.drawImage(imageWater, 0, 0, null);
            g2.dispose();
            imageWater = flipped2;
            imageHappy = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/HeroHappy.png"));
            BufferedImage flipped3 = new BufferedImage(
                imageHappy.getWidth(),
                imageHappy.getHeight(),
                imageHappy.getType());
            AffineTransform tran3 = AffineTransform.getTranslateInstance(imageHappy.getWidth(), 0);
            AffineTransform flip3 = AffineTransform.getScaleInstance(-1d, 1d);
            tran3.concatenate(flip3);
            Graphics2D g3 = flipped3.createGraphics();
            g3.setTransform(tran3);
            g3.drawImage(imageHappy, 0, 0, null);
            g3.dispose();
            imageHappy = flipped3;
        } catch (IOException ex) {
            Logger.getLogger(Hero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        animations[0] = new BufferedImage[1];
        animations[1] = new BufferedImage[1];
        animations[2] = new BufferedImage[2];
        setWidth(40);
        setHeight(40);
        BufferedImage[] n = {imageNormal}, w = {imageWater}, h = {imageHappy};
        animations[0] = n;
        animations[1] = w;
        animations[2] = h;
        animation.setFrames(animations[0]);
        s = new State(getX(), getY(), -11, 0.6, 4.2, 0.4, 14.5, 1.5, -2.85, 0.75);
        initState(s);
        life = 15;
        maxLife = 15;
        rotation = 0;
        setDashSpeed(2.75);
    }
    
    public void setArea(AreaZone area) {
        area.setHero(this);
        setInvincible(false);
        super.setArea(area);
    }
    
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
        if(!canMove) {
            left = false;
            right = false;
            jumping = false;
            setDashing(false);
        }
    }
    
    public void update() {
        readKeys();
        super.checkCollisions();
        if(inWater && canMove) {
            animation.setFrames(animations[1]);
            animation.setDelay(-1);
        }
        else if(canMove) {
            animation.setFrames(animations[0]);
            animation.setDelay(-1);
        }
        if(dx > 0) {
            rotation += 4;
        }
        else if(dx < 0) {
            rotation -= 4;
        }
        if(rotation > 355 && rotation < 365 || rotation < -355 && rotation > -365 || rotation % 360 == 0 || rotation % -360 == 0) {
            rotation = 0;
        }
        if(straight && dx == 0 && dy == 0 && rotation != 0) {
            if(rotation > -3 && rotation < 3) {
                rotation = 0;
            }
            if(rotation > 0 && rotation < 180) {
                rotation -= 2.5;
            }
            if(rotation > 180) {
                rotation += 2.5;
            }
            if(rotation < 0 && rotation > -180) {
                rotation += 2.5;
            }
            if(rotation < -180) {
                rotation -= 2.5;
            }
        }
        if(falling && groundAttack == false && straight && System.currentTimeMillis() - vibrationTime > 3000) {
            vibration = true;
            basicRotation = getRotation();
            basicX = getX();
            basicY = getY();
            turn = 0;
            vibrationTime = System.currentTimeMillis();
        }
        if(vibration) {
            rotate();
        }
        if(groundAttack) {
            if(onTheGround() && groundAttackTime == 0) {
                groundAttackTime = System.currentTimeMillis();
                pxl = tileMap.getTile(tileMap.getRowTile((int) getX() - getWidth() / 2), tileMap.getColTile((int) getY() + getHeight() - 1) + 1).getImage().getRGB(16, 16);
                pxr = tileMap.getTile(tileMap.getRowTile((int) getX() + (getWidth() / 2) * 3), tileMap.getColTile((int) getY() + getHeight() - 1) + 1).getImage().getRGB(16, 16);
                int idl = tileMap.getTile(tileMap.getRowTile((int) getX() - getWidth() / 2), tileMap.getColTile((int) getY() + getHeight() - 1) + 1).getID(), idr = tileMap.getTile(tileMap.getRowTile((int) getX() + (getWidth() / 2) * 3), tileMap.getColTile((int) getY() + getHeight() - 1) + 1).getID();
                leftW = new RockWall(getArea(), (int) getX() - 30, tileMap.getColTile((int) getY() + getHeight() + 1) * 32 - 70, 0, gsm);
                rightW = new RockWall(getArea(), (int) getX() + getWidth(), tileMap.getColTile((int) getY() + getHeight() + 1) * 32 - 70, 0, gsm);
                leftW.setPixelRGB((int) pxl);
                rightW.setPixelRGB((int) pxr);
                if(idl != 0) {
                    getArea().objects.add(leftW);
                }
                if(idr != 0) {
                    getArea().objects.add(rightW);
                }
            }
            if(groundAttackTime != 0) {
                if(System.currentTimeMillis() - groundAttackTime > 750) {
                    groundAttackTime = 0;
                    groundAttack = false;
                    if(getArea().objects.contains(leftW)) {
                        getArea().objects.remove(leftW);
                    }
                    if(getArea().objects.contains(rightW)) {
                        getArea().objects.remove(rightW);
                    }
                }
                if(leftW != null && rightW != null) {
                    if(leftW.isUsed()) {
                        getArea().objects.remove(leftW);
                    }
                    if(rightW.isUsed()) {
                        getArea().objects.remove(rightW);
                    }
                }
            }
        }
        checkAttacks();
        for(int i = 0; i < getArea().objects.size(); i++) {
            Obstacle o = getArea().objects.get(i);
            if(intersects(o)) {
                o.collides(this);
                if(o.isSolid()) {
                    if(getX() + getWidth() > o.getX() && getX() + getWidth() < o.getX() + o.getWidth() / 2) {
                        setX(getX() - (getX() + getWidth() - o.getX()));
                        dx = 0;
                    }
                    if(getX() > o.getX() + o.getWidth() / 2 && getX() < o.getX() + o.getWidth()) {
                        setX(o.getX() + o.getWidth() + 1);
                        dx = 0;
                    }
                    for(int i1 = 0; i1 < waves.size(); i1++) {
                        if(waves.get(i).getX() + waves.get(i).getWidth() > o.getX() && waves.get(i).getX() + waves.get(i).getWidth() < o.getX() + o.getWidth() / 2) {
                            setX(getX() - (getX() + getWidth() - o.getX()));
                        }
                        if(waves.get(i).getX() > o.getX() + o.getWidth() / 2 && waves.get(i).getX() < o.getX() + o.getWidth()) {
                            setX(o.getX() + o.getWidth() + 1);
                        }
                    }
                }
            }
        }
        if(System.currentTimeMillis() - flinchTime > 2000) {
            flinching = false;
        }
        if(tileMap.getTile(tileMap.getRowTile((int) getX() + getWidth() / 2), tileMap.getColTile((int) getY() + getHeight() / 2)).getID() == 53) {
            ((LevelChoice) gsm.getState()).restart();
        }
        animation.update();
        tileMap.setPosition(getTotalWidth() / 2 - (int) getX() - getWidth() / 2, getTotalHeight() / 2 - (int) getY() - getHeight() / 2);
    }
    
    private void checkAttacks() {
        for(int i = 0; i < waves.size(); i++) {
            waves.get(i).update();
            if(waves.get(i).getDX() == 0) {
                waves.remove(i);
            }
        }
        for(int i = 0; i < getArea().enemies.size(); i++) {
            Enemy en = getArea().enemies.get(i);
            if(!inWater) {
                if(intersects(en) && en.isAlive()) {
                    if(intersectsOnTop(en) && dy > 0) {
                        en.hit(1);
                        rejump();
                    }
                    if(!flinching && !jumping) hit(en.getDamage(), en.getDX());
                }
            }
            else {
                if(intersects(en)) {
                    hit(en.getDamage(), en.getDX());
                }
            }
            for(int i1 = 0; i1 < waves.size(); i1++) {
                waves.get(i1).update();
                if(waves.get(i1).intersects(en)) {
                    en.hit(2);
                    en.setDY(-7);
                    waves.get(i).setToRemove(true);
                }
                if(waves.get(i1).getDX() == 0 || waves.get(i1).isToRemove()) {
                    waves.remove(i1);
                }
            }
            if(!en.isAlive()) {
                for(int j = 0; j < 3 + new java.util.Random().nextInt(9 - 3 + 1); j++) {
                    dusts.add(new DustExplosion(getArea().enemies.get(i).getX(), getArea().enemies.get(i).getY(), 0, tileMap));
                }
                for(int k = 0; k < 3 + new java.util.Random().nextInt(9 - 3 + 1); k++) {
                    dusts.add(new DustExplosion(getArea().enemies.get(i).getX(), getArea().enemies.get(i).getY(), 1, tileMap));
                }
                for(int l = 0; l < 3 + new java.util.Random().nextInt(9 - 3 + 1); l++) {
                    dusts.add(new DustExplosion(getArea().enemies.get(i).getX(), getArea().enemies.get(i).getY(), 2, tileMap));
                }
                for(int m = 0; m < 3 + new java.util.Random().nextInt(9 - 3 + 1); m++) {
                    dusts.add(new DustExplosion(getArea().enemies.get(i).getX(), getArea().enemies.get(i).getY(), 3, tileMap));
                }
                getArea().removeEnemy(i);
                JukeBox.playAction("explode");
            }
        }
        if(getY() > tileMap.getMapHeight() * tileMap.getTileSize() || getY() < 0) {
            LevelChoice level = (LevelChoice) gsm.getState();
            level.restart();
        }
        for(int i = 0; i < dusts.size(); i++) {
            dusts.get(i).update();
            if(dusts.get(i).canBeRemoved()) {
                dusts.remove(i);
            }
        }
    }
    
    private void rotate() {
        turn++;
        rotation += 30;
        setX(basicX);
        setY(basicY);
        if(turn > 360 / 30 * 3) {
            rotation = basicRotation;
            vibration = false;
            groundAttack = true;
        }
    }
    
    public void readKeys() {
        if(!canMove) return;
        if(Controler.isLeft()) {
            setLeft(true);
        }
        else {
            setLeft(false);
        }
        if(Controler.isRight()) {
            setRight(true);
        }
        else {
            setRight(false);
        }
        if(Controler.isUp()) {
            setJumping(true);
        }
        else {
            setJumping(false);
        }
        if(Controler.isDown()) {
            setDown(true);
        }
        else {
            setDown(false);
        }
        straight = Controler.isSpace();
        setDashing(Controler.isControl());
    }
    
    public void paint(Graphics2D g) {
        for(int i = 0; i < waves.size(); i++) {
            waves.get(i).paint(g);
        }
        for(int i = 0; i < dusts.size(); i++) {
            dusts.get(i).paint(g);
        }
        g.setColor(Color.yellow);
        double rotationRequired = Math.toRadians(rotation);
        double locationX = animation.getImage().getWidth();
        double locationY = animation.getImage().getHeight();
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX / 2, locationY / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        if((!flinching || (flinching && (System.currentTimeMillis() - flinchTime) % 2 == 0)) && isVisible()) {
            g.drawImage(op.filter(animation.getImage(), null), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), null);
        }
        if(fightAnimation.getDelay() != -1) {
            g.drawImage(fightAnimation.getImage(), 0, 0, null);
        }
        //g.setColor(Color.yellow);
        //g.fillRect(tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), 96, 96);
        g.setColor(Color.red);
        for(int i = 0; i <= getWidth() / 32; i++) {
            //g.drawLine(tileMap.getX() + (int) getX() + i * 32, tileMap.getY() + (int) getY(), tileMap.getX() + (int) getX() + i * 32, tileMap.getY() + (int) getY() + getHeight());
        }
        g.setColor(Color.white);
        g.drawOval(50, 50, 100, 70);
        g.setColor(Color.green);
        if(life < maxLife / 2 + maxLife / 3) {
            g.setColor(new Color(Color.red.getRed() + Color.green.getRed(), Color.red.getGreen() + Color.green.getGreen(), Color.red.getBlue() + Color.green.getBlue()));
        }
        if(life < maxLife / 2) {
            g.setColor(Color.red);
        }
        if(life == 1) {
            if(System.currentTimeMillis() - lifeTime > 500) {
                g.setColor(new Color(255, 128, 0));
            }
            if(System.currentTimeMillis() - lifeTime > 1000) {
                lifeTime = System.currentTimeMillis();
            }
            if(lifeTime == 0) {
                lifeTime = System.currentTimeMillis();
            }
        }
        g.setClip(50, 50, (int) (life / maxLife * 100), 70);
        g.fillOval(51, 51, 100 - 1, 70 - 1);
        g.setClip(null);
        Font font = new Font("Arial", Font.PLAIN, 20);
        FontMetrics f = g.getFontMetrics(font);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString((int) life+"/"+(int) maxLife, 50 + (100 - f.stringWidth((int) life+"/"+(int) maxLife)) / 2, (50 + 70 * 2) / 2);
    }
    
    public void setWidth(int width) {
        super.setWidth(width);
        if(getHeight() == 0 || animations[0][0] == null) return;
        animations[0][0] = resize(imageNormal, width, getHeight());
        animations[1][0] = resize(imageWater, width, getHeight());
        if(width == 40) {
            animations[0][0] = imageNormal;
            animations[1][0] = imageWater;
        }
    }
    
    public void setHeight(int height) {
        super.setHeight(height);
        if(getWidth() == 0 || animations[0] == null) return;
        animations[0][0] = resize(imageNormal, getWidth(), height);
        animations[1][0] = resize(imageWater, getWidth(), height);
        if(height == 40) {
            animations[0][0] = imageNormal;
            animations[1][0] = imageWater;
        }
    }
    
    public BufferedImage resize(BufferedImage image, int w, int h) {
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = newImage.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        g.drawImage(image, 0, 0, w, h, null);
        g.dispose();
        Image i = filtre(newImage, new Color(0, 0, 0));
        newImage = convertToBufferedImage(i);
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
    
    public void hit(int damage, double dx) {
        if(invincible) return;
        if(!flinching) {
            flinchTime = System.currentTimeMillis();
            flinching = true;
            life -= damage;
            setVector(dx * 5, getDY());
            if(life <= 0) {
                LevelChoice level = (LevelChoice) gsm.getState();
                level.getLevel().lastLife = 15;
                level.restart();
            }
        }
    }
    
    public void setLeft(boolean left) {
        this.left = left;
    }
    
    public void setRight(boolean right) {
        this.right = right;
    }
    
    public void setJumping(boolean jumping) {
        super.setJumping(jumping);
        if(!falling && !inWater && onTheGround() && jumping) {
            JukeBox.playAction("playerjump");
        }
    }
    
    public void setDown(boolean down) {
        this.down = down;
    }
    
    public double getLife() {
        return life;
    }
    
    public double getMaxLife() {
        return maxLife;
    }
    
    public void setLife(int life) {
        this.life = life;
    }
    
    public void setInvincible(boolean b) {
        this.invincible = b;
    }
}