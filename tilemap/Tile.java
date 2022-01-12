package tilemap;


import java.awt.image.BufferedImage;

public class Tile {
    
    private BufferedImage image;
    private boolean isSolid = false, isWater = false;
    private int x, y, id;
    public int[] notSolid = {0, 5, 11, 12, 15, 16, 20, 21, 33, 34, 35, 37, 38, 39, 40, 41, 52, 53};
    public int[] rotatedBlock = {4, -90};
    
    public Tile(BufferedImage image, int x, int y, int id) {
        this.image = image;
        this.x = x;
        this.y = y;
        setID(id);
    }
    
    public void setID(int id) {
        this.id = id;
        if(id > 0) {
            isSolid = true;
        }
        if(notSolidContains(id)) {
            isSolid = false;
        }
        if(id == 33 || id == 34) {
            isWater = true;
        }
    }
    
    public boolean notSolidContains(int id) {
        for(int i = 0; i < notSolid.length; i++) {
            if(id == notSolid[i]) {
                return true;
            }
        }
        return false;
    }
    
    public int getRotation() {
        for(int i = 0; i < rotatedBlock.length; i++) {
            System.out.println(i+" % 2 = "+i%2);
            if(i % 2 == 0) {
                System.out.println(rotatedBlock[i]+" "+id);
                if(rotatedBlock[i] == getID()) {
                    System.out.println(rotatedBlock[i + 1]);
                    return rotatedBlock[i + 1];
                }
            }
        }
        return 0;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getID() {
        return id;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setBlocked(boolean blocked) {
        this.isSolid = blocked;
    }
    
    public void setWater(boolean water) {
        this.isWater = water;
    }
    
    public boolean isSolid() {
        return isSolid;
    }
    
    public boolean isWater() {
        return isWater;
    }
    
    public void setImage(BufferedImage bufferedImage) {
        this.image = bufferedImage;
    }
}