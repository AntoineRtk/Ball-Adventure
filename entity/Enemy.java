package entity;

import states.GameStateManager;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import obstacles.Obstacle;
import tilemap.AreaZone;

public abstract class Enemy extends MapObject {
    
    private double life, maxLife, damage;
    private boolean alive = true;
    public GameStateManager gsm;
    
    public Enemy(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID);
        this.gsm = gsm;
    }
    
    public void update() {
        super.checkCollisions();
        if(getY() > tileMap.getMapHeight() * tileMap.getTileSize()) {
            setAlive(false);
        }
        if(tileMap.getTile(tileMap.getRowTile((int) getX() + getWidth() / 2), tileMap.getColTile((int) getY() + getHeight() / 2)).getID() == 53) {
            setAlive(false);
        }
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
                }
            }
        }
        animation.update();
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    public void paint(Graphics2D g) {
        if(isFlipped()) {
            g.drawImage(getImage(), tileMap.getX() + (int) getX() + getWidth(), tileMap.getY() + (int) getY(), -getWidth(), getHeight(), null);
        }
        else {
            g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), null);
        }
        g.setColor(Color.white);
        g.fillRect(tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY() - 10, (int) getWidth(), 6);
        g.setColor(Color.red);
        g.fillRect(tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY() - 10, (int) (life / maxLife * getWidth()), 6);
    }
    
    public boolean onScreen() {
        return new Rectangle((int) getArea().getHero().getX() - gsm.getWidth() / 2, (int) getArea().getHero().getY() - gsm.getHeight() / 2, gsm.getWidth() / 2 * 3 + getArea().getHero().getWidth(), gsm.getHeight() / 2 * 3 + getArea().getHero().getHeight()).contains(getX(), getY());
    }
    
    public void getNextPosition() {
        if(dx == 0) {
            if(right) {
                left = true;
                right = false;
            }
            else {
                left = false;
                right = true;
            }
        }
    }
    
    public void getNextJumpPosition() {
        if(right) {
            if(tileMap.getTile(tileMap.getRowTile((int) (getX() + getWidth() + getDX())) + 1, tileMap.getColTile((int) getY() + getHeight() / 2)).isSolid()) {
                if(!tileMap.getTile((int) tileMap.getRowTile((int) (getX() + getWidth() + getDX())) + 1, tileMap.getColTile((int) getY() - getHeight() / 2)).isSolid()) {
                    setJumping(true);
                }
            }
        }
        else if(left) {
            if(tileMap.getTile(tileMap.getRowTile((int) (getX() + getDX())) - 1, tileMap.getColTile((int) getY() + getHeight() / 2)).isSolid()) {
                if(!tileMap.getTile((int) tileMap.getRowTile((int) (getX() + getDX())) - 1, tileMap.getColTile((int) getY() - getHeight() / 2)).isSolid()) {
                    setJumping(true);
                }
            }
        }
    }
    
    public void setLife(int life) {
        this.life = life;
        this.maxLife = life;
    }
    
    public void hit(int damage) {
        this.life -= damage;
        if(life <= 0) {
            setAlive(false);
        }
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public int getDamage() {
        return (int) damage;
    }
    
    public abstract String getName();
    
    public double getLife() {
        return life;
    }
    
    public void deal(int damage) {
        this.life -= damage;
    }
    
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    
    public boolean isAlive() {
        return alive;
    }
}