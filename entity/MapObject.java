package entity;

import animation.Animation;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import jukebox.JukeBox;
import states.Main;
import states.State;
import tilemap.AreaZone;
import tilemap.TileMap;

public abstract class MapObject {
    
    private double x, y;
    public double dx, dy, rotation;
    private double jumpStart, moveSpeed, maxSpeed, dash, fallingSpeed, maxFallingSpeed, waterMoveSpeed, waterJumpSpeed, waterFallingSpeed;
    public boolean dashing, left, right, up, down, jumping, falling = true, canFly = false, inWater = false, isSliding = false, visible = true, projecting = false, flipped = false;
    private boolean botLeft, botRight, topLeft, topRight, midTop, midBot, midLeft, midRight;
    public Animation animation = new Animation();
    private AreaZone area;
    public TileMap tileMap;
    private int instantID = 0, width, height;
    
    public MapObject(AreaZone area, double x, double y, int instantID) {
        this.area = area;
        this.tileMap = area.tileMap;
        this.x = x;
        this.y = y;
        this.instantID = instantID;
    }
    
    public AreaZone getArea() {
        return area;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void setProjecting(boolean projecting) {
        this.projecting = projecting;
    }
    
    public void setArea(AreaZone area) {
        this.area = area;
        this.tileMap = area.tileMap;
    }
    
    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }
    
    public boolean intersectsOnTop(MapObject o) {
        return new Rectangle((int) getX(), (int) getY() + (int) getHeight() - (int) getHeight() / 3, (int) getWidth(), (int) getHeight() / 3).intersects(new Rectangle((int) o.getX(), (int) o.getY(), (int) o.getWidth(), (int) o.getHeight() / 3));
    }
    
    public boolean intersects(MapObject o) {
        return getBounds().intersects(o.getBounds()) || o.getBounds().intersects(getBounds());
    }
    
    public abstract void update();
    
    public void checkCollisions() {
        if(!inWater) {
            if(left) {
                dx -= moveSpeed;
                if(dx < -maxSpeed) {
                    dx = -maxSpeed;
                    if(dashing) {
                        dx -= dash;
                    }
                }
            }
            else if(right) {
                dx += moveSpeed;
                if(dx > maxSpeed) {
                    dx = maxSpeed;
                    if(dashing) {
                        dx += dash;
                    }
                }
            }
            else {
                if(!projecting) {
                    if(dx > 0) {
                        dx -= moveSpeed;
                        if(dx < 0) {
                            dx = 0;
                        }
                    }
                    else if(dx < 0) {
                        dx += moveSpeed;
                        if(dx > 0) {
                            dx = 0;
                        }
                    }
                }
            }
            if(jumping) {
                dy = jumpStart;
                jumping = false;
                falling = true;
            }
            if(falling) {
                if(!projecting) {
                    dy += fallingSpeed;
                    if(dy > maxFallingSpeed) {
                        dy = maxFallingSpeed;
                    }
                }
            }
        }
        else {
            if(left && !projecting) {
                dx = -waterMoveSpeed;
            }
            else if(right && !projecting) {
                dx = waterMoveSpeed;
            }
            else {
                if(!projecting) {
                    dx = 0;
                }
            }
            if(up && !projecting) {
                dy = waterJumpSpeed;
            }
            if(falling && !projecting) {
                dy += waterFallingSpeed;
                if(dy > waterFallingSpeed * 3) {
                    dy = waterFallingSpeed * 3;
                }
            }
        }
        // Collisions
        int toX = (int) (x + dx);
        int toY = (int) (y + dy);
        double tempx = x;
        double tempy = y;
        if(isSliding) {
            int rotation = tileMap.getTile(tileMap.getRowTile((int) x + getWidth() / 2), tileMap.getColTile((int) y + getHeight() + 1)).getRotation();
            if(rotation < 0) {
                tempx += -moveSpeed;
            }
            else {
                tempx += moveSpeed;
            }
        }
        calculateCorners(toX, (int) y);
        if(dx > 0 && !projecting) {
            if(botRight || topRight || midRight) {
                dx = 0;
            }
            else {
                tempx += dx;
            }
        }
        else if(dx < 0 && !projecting) {
            if(botLeft || topLeft || midLeft) {
                dx = 0;
            }
            else {
                tempx += dx;
            }
        }
        calculateCorners((int) x, toY);
        if(dy < 0 && !projecting) {
            if(topLeft || topRight || midTop) {
                tempy = tileMap.getColTile((int) (y)) * tileMap.getTileSize();
                dy = 0;
            }
            else {
                tempy += dy;
            }
        }
        else if(dy > 0 && !projecting) {
            if(botLeft || botRight || midBot) {
                falling = false;
                tempy = tileMap.getColTile((int) (y + height + dy)) * tileMap.getTileSize() - height;
                dy = 0;
            }
            else {
                tempy += dy;
            }
        }
        if(!falling) {
            if(!projecting) {
                calculateCorners((int) x, (int) y + 1);
                if(!botLeft && !botRight && !midBot) {
                    falling = true;
                }
            }
        }
        if(up && canFly) {
            dy = jumpStart;
        }
        if(down && canFly) {
            dy += fallingSpeed;
            if(dy > maxFallingSpeed) {
                dy = maxFallingSpeed;
            }
        }
        if(1 == 2) {
            int rotation = tileMap.getTile(tileMap.getRowTile((int) x + getWidth() / 2), tileMap.getColTile((int) y + getHeight() + 1)).getRotation();
            isSliding = rotation != 0;
            isSliding = false;
        }
        if(projecting) {
            tempx += dx;
            tempy += dy;
        }
        x = tempx;
        y = tempy;
        int midx = (int) (x + getWidth() / 2), midy = (int) (y + getHeight() / 2);
        inWater = tileMap.isWater(tileMap.getRowTile(midx), tileMap.getColTile(midy));
        if(!tileMap.isWater(tileMap.getRowTile((int) midx), tileMap.getColTile((int) y) - 1) && inWater && up) {
            calculateCorners(midx, toY);
            if(!midTop && up) {
                jumping = true;
            }
        }
    }
    
    public boolean intersectsAnything() {
        calculateCorners((int) (x + dx), (int) y);
        if(dx > 0) {
            if(botRight || topRight || midRight) {
                return true;
            }
        }
        else if(dx < 0) {
            if(botLeft || topLeft || midLeft) {
                return true;
            }
        }
        calculateCorners((int) x, (int) (y + dy));
        if(dy < 0) {
            if(topLeft || topRight || midTop) {
                return true;
            }
        }
        else if(dy > 0) {
            if(botLeft || botRight || midBot) {
                return true;
            }
        }
        calculateCorners((int) x, (int) y + 1);
        return botLeft || botRight || midBot;
    }
    
    public boolean intersectsAnythingAtSides() {
        calculateCorners((int) (x + dx), (int) y);
        if(dx > 0) {
            if(botRight || topRight || midRight) {
                return true;
            }
        }
        else if(dx < 0) {
            if(botLeft || topLeft || midLeft) {
                return true;
            }
        }
        for(int i = 0; i < getArea().objects.size(); i++) {
            if(new Rectangle2D.Double(getX() + getDX(), getY() + getHeight() / 2, getWidth(), getHeight()).intersects(getArea().objects.get(i).getBounds())) {
                return true;
            }
        }
        return false;
    }
    
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    
    public double getRotation() {
        return rotation;
    }
    
    private void calculateCorners(int x, int y) {
        int leftTile = tileMap.getRowTile(x);
        int rightTile = tileMap.getRowTile(x + getWidth() - 1);
        int topTile = tileMap.getColTile(y);
        int botTile = tileMap.getColTile(y + getHeight() - 1);
        int midWidth = tileMap.getColTile(y + getHeight() / 2);
        int midHeight = tileMap.getRowTile(x + getWidth() / 2);
        topLeft = tileMap.isSolid(leftTile, topTile);
        topRight = tileMap.isSolid(rightTile, topTile);
        botLeft = tileMap.isSolid(leftTile, botTile);
        botRight = tileMap.isSolid(rightTile, botTile);
        midLeft = tileMap.isSolid(leftTile, midWidth);
        midRight = tileMap.isSolid(rightTile, midWidth);
        midTop = tileMap.isSolid(midHeight, topTile);
        midBot = tileMap.isSolid(midHeight, botTile);
    }
    
    public boolean onTheGround() {
        calculateCorners((int) x, (int) y + 1);
        return botLeft || botRight || midBot;
    }
    
    public void setDashSpeed(double dash) {
        this.dash = dash;
    }
    
    public void setDashing(boolean dashing) {
        this.dashing = dashing;
    }
    
    public boolean isDashing() {
        return this.dashing;
    }
    
    public void setLeft(boolean left) {
        this.left = left;
    }
    
    public void setRight(boolean right) {
        this.right = right;
    }
    
    public void setJumping(boolean jumping) {
        up = jumping;
        if(!falling) {
            this.jumping = jumping;
        }
    }
    
    public void setDown(boolean down) {
        this.down = down;
    }
    
    public void rejump() {
        jumping = true;
        if(this instanceof Hero) {
            JukeBox.playAction("playerjump");
        }
    }
    
    public BufferedImage getImage() {
        return animation.getImage();
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public double getDX() {
        return dx;
    }
    
    public double getDY() {
        return dy;
    }
    
    public double getJumpStart() {
        return jumpStart;
    }
    
    public void paint(Graphics2D g) {
        if(!isVisible()) {
            return;
        }
        if(getRotation() != 0) {
            AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(getRotation()), getWidth() / 2, getHeight() / 2);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            g.drawImage(op.filter(getImage(), null), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), null);
            return;
        }
        g.drawImage(getImage(), tileMap.getX() + (int) getX(), tileMap.getY() + (int) getY(), null);
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public void setWidth(int width) {
        this.width = width;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, (int) getWidth(), (int) getHeight());
    }
    
    public void setPosition(double x, double y) {
        setX(x);
        setY(y);
    }
    
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    
    public String getVector() {
        return dx+" "+dy;
    }
    
    public void setDX(double dx) {
        this.dx = dx;
    }
    
    public void setDY(double dy) {
        this.dy = dy;
    }
    
    public void initState(State s) {
        this.x = s.getX();
        this.y = s.getY();
        this.jumpStart = s.getJumpStart();
        this.moveSpeed = s.getMoveSpeed();
        this.maxSpeed = s.getMaxSpeed();
        this.fallingSpeed = s.getFallingSpeed();
        this.maxFallingSpeed = s.getMaxFallingSpeed();
        this.waterMoveSpeed = s.getWaterMoveSpeed();
        this.waterJumpSpeed = s.getWaterJumpSpeed();
        this.waterFallingSpeed = s.getWaterFallingSpeed();
    }
    
    public void keyPressed(int k) {}
    
    public void keyReleased(int k) {}
    
    public int getTotalWidth() {
        return Main.frame.getWidth();
    }
    
    public int getTotalHeight() {
        return Main.frame.getHeight();
    }
    
    public int getInstantID() {
        return instantID;
    }
}