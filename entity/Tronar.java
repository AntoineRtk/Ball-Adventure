
package entity;

import obstacles.Branch;
import animation.BossExplosion;
import animation.DustExplosion;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jukebox.JukeBox;
import obstacles.Apple;
import states.GameStateManager;
import states.LevelChoice;
import states.State;
import tilemap.AreaZone;

public class Tronar extends Enemy {
    
    private boolean shown = false, rockPeakSpawned = false;
    private int index, dieCount = 0, rockPeakCount = 0, attackState = 0, attackCount = 0, dashCount = 0;
    private long currentTimeD = -1;
    private Branch branch;
    private BufferedImage[][] animations = new BufferedImage[4][];
    private BossExplosion bossExplosion;
    private double xd, yd;
    
    public Tronar(AreaZone area, double x, double y, int index, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID, gsm);
        BufferedImage image = null;
        this.index = index;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResourceAsStream("images/Tronar.png"));
        } catch (IOException ex) {
            Logger.getLogger(Tronar.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedImage[] w = {image.getSubimage(0, 0, 60, 60), image.getSubimage(180, 0, 60, 60)};
        BufferedImage[] d = {image.getSubimage(0, 0, 60, 60), image.getSubimage(120, 0, 60, 60), image.getSubimage(60, 0, 60, 60)};
        BufferedImage[] b = {image.getSubimage(240, 0, 60, 60), image.getSubimage(300, 0, 60, 60)};
        BufferedImage[] c = {image.getSubimage(360, 0, 60, 60)};
        animations[0] = w;
        animations[1] = d;
        animations[2] = b;
        animations[3] = c;
        State s = new State(x, y, -11, 0.7, 4, 0.32, 6, 0.2, 0.2, 0.2);
        initState(s);
        setWidth(60);
        setHeight(60);
        left = true;
        setDashSpeed(3.45);
        setDamage(2);
        setLife(20);
        this.index = index;
        branch = new Branch(area, 0, 0, 0, gsm);
        branch.setVisible(false);
        animation.setFrames(animations[0]);
        animation.setDelay(200);
        getArea().objects.add(branch);
    }
    
    public void update() {
        super.update();
        if(!isAlive()) {
            tileMap.deblockCamera();
            JukeBox.play("bossbeaten");
            JukeBox.setLoopPoints(0, -1);
            ((LevelChoice) gsm.getState()).getLevel().informBossBeatenTheme();
        }
        if(getLife() <= 0) {
            for(int i = 0; i < getArea().enemies.size(); i++) {
                if(getArea().enemies.get(i) instanceof RockPeak) {
                    getArea().enemies.remove(i);
                }
            }
            branch.setVisible(false);
            if(!tileMap.getTile(tileMap.getRowTile((int) getX() - getArea().getHero().getWidth()), tileMap.getColTile((int) getY())).isSolid()) {
                getArea().getHero().setPosition(getX() - getArea().getHero().getWidth() - dx, (int) getArea().getHero().getY());
            }
            else {
                getArea().getHero().setPosition(getX() + getWidth() + dx, (int) getArea().getHero().getY());
            }
            animation.setFrames(animations[1]);
            animation.setDelay(150);
            setPosition(xd, yd);
            dieCount++;
            if(dieCount % 2 == 0 && dieCount < 100) {
                for(int i = 0; i < 150; i++) {
                    getArea().getHero().dusts.add(new DustExplosion(getX() + getWidth() / 2, getY() + getHeight() / 2, new java.util.Random().nextInt(4), tileMap));
                }
            }
            if(dieCount == 100) {
                bossExplosion = new BossExplosion(getX(), getY(), getWidth(), getHeight(), tileMap);
            }
        }
        if(bossExplosion != null) {
            bossExplosion.update();
            if(bossExplosion.isFinished()) {
                setAlive(false);
                JukeBox.play("bossbeaten");
                JukeBox.setLoopPoints(0, -1);
                ((LevelChoice) gsm.getState()).getLevel().informBossBeatenTheme();
                getArea().getHero().setCanMove(true);
                tileMap.deblockCamera();
                getArea().event(index);
            }
        }
        if(getLife() <= 0) return;
        if(left) {
            setFlipped(true);
        }
        else {
            setFlipped(false);
        }
        getNextJumpPosition();
        if(attackState == 0) {
            animation.setFrames(animations[0]);
            animation.setDelay(200);
            getNextPosition();
            if(intersectsAnythingAtSides()) {
                attackCount++;
            }
            if(attackCount == 3) {
                attackState = 1 + new java.util.Random().nextInt(2 - 1 + 1);
                attackCount = 0;
                System.out.println("ATTACK : "+attackState);
                if(getLife() < 8 && !rockPeakSpawned) {
                    attackState = 3;
                }
                left = false;
                right = false;
            }
        }
        else if(attackState == 1) {
            if(!isDashing()) {
                if(getX() > getArea().getHero().getX()) {
                    setFlipped(true);
                }
                else {
                    setFlipped(false);
                }
            }
            if(currentTimeD == -1) {
                currentTimeD = System.currentTimeMillis();
            }
            if(System.currentTimeMillis() - currentTimeD > 1000) {
                setDashing(true);
            }
            if(isDashing()) {
                getNextPosition();
                if(intersectsAnythingAtSides()) {
                    dashCount++;
                    if(dashCount == 2) {
                        dashCount = 0;
                        currentTimeD = -1;
                        setDashing(false);
                        attackState = 0;
                    }
                }
            }
        }
        else if(attackState == 2) {
            if(getX() > getArea().getHero().getX()) {
                setFlipped(true);
            }
            else {
                setFlipped(false);
            }
            if(animation.getFrames() != animations[2]) {
                animation.setFrames(animations[2]);
                animation.setDelay(350);
                branch.grows();
                branch.setVisible(true);
            }
            if(animation.getImage() == animations[2][1]) {
                animation.setDelay(-1);
            }
            if(branch.hasFinishedOperation()) {
                attackState = 0;
                branch.setVisible(false);
            }
        }
        else if(attackState == 3) {
            if(getLife() > 7) {
                attackState = new java.util.Random().nextInt(3);
            }
            else {
                if(!rockPeakSpawned && rockPeakCount < 201) {
                    setLife(7);
                    animation.setFrames(animations[3]);
                    animation.setDelay(-1);
                    tileMap.setShaking(true, 50);
                    rockPeakCount++;
                    if(rockPeakCount % 50 == 0) {
                        getArea().enemies.add(new RockPeak(getArea(), getArea().getHero().getX(), getArea().getHero().getY() - 200, 0, gsm));
                    }
                    if(rockPeakCount == 200) {
                        rockPeakSpawned = true;
                        attackState = 0;
                        tileMap.setShaking(false, 0);
                        getArea().objects.add(new Apple(getArea(), getArea().getHero().getX(), getArea().getHero().getY() - 200, 0, gsm));
                    }
                }
                else {
                    attackState = 0;
                }
            }
        }
    }
    
    public void paint(Graphics2D g) {
        if(!isFlipped()) {
            g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY() + 5, null);
        }
        else {
            g.drawImage(getImage(), tileMap.getX() + (int) getX() + getWidth(), tileMap.getY() + (int) getY() + 5, -getWidth(), getHeight(), null);
        }
        if(bossExplosion != null) {
            bossExplosion.paint(g);
        }
    }
    
    public String getName() {
        return "Tronar";
    }
    
    public void hit(int damage) {
        if(getLife() < 1) return;
        deal(damage);
        if(getLife() <= 0) {
            xd = getX();
            yd = getY();
            getArea().getHero().setCanMove(false);
            getArea().getHero().setInvincible(true);
        }
    }
    
    public boolean onScreen() {
        if(shown) {
            tileMap.deblockCamera();
            tileMap.setPosition((int) -getX() + getTotalWidth() / 2, (int) -getY() + getTotalHeight() / 2);
            tileMap.blockCamera();
            return true;
        }
        if(new Rectangle(tileMap.getX() - getWidth(), tileMap.getY() - getHeight(), GameStateManager.getWidth() + getWidth() * 2, GameStateManager.getHeight() + getHeight() * 2).contains(new Rectangle((int) -getX() + GameStateManager.getWidth(), (int) -getY() + GameStateManager.getHeight(), getWidth(), getHeight()))) {
            shown = true;
            tileMap.deblockCamera();
            tileMap.setPosition((int) -getX() + getTotalWidth() / 2, (int) -getY() + getTotalHeight() / 2);
            tileMap.blockCamera();
            return true;
        }
        else {
            return false;
        }
    }
}