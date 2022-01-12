
package animation;

import java.awt.image.BufferedImage;

public class Animation {
    
    private BufferedImage[] images;
    private int index;
    private long currentTime = System.currentTimeMillis(), delay = -1;
    private boolean hasPlayedOnce = false;
    
    public Animation() {}
    
    public void update() {
        if(delay == -1) {
            return;
        }
        long time = System.currentTimeMillis() - currentTime;
        if(time > delay) {
            currentTime = System.currentTimeMillis();
            updateImage();
        }
    }
    
    private void updateImage() {
        if(index + 1 == images.length) {
            index = 0;
            hasPlayedOnce = true;
        }
        else {
            index++;
        }
    }
    
    public void setDelay(long time) {
        delay = time;
    }
    
    public BufferedImage getImage() {
        return images[index];
    }
    
    public long getDelay() {
        return delay;
    }
    
    public void setFrames(BufferedImage[] frames) {
        if(frames != images) {
            this.images = frames;
            index = 0;
            currentTime = System.currentTimeMillis();
            hasPlayedOnce = false;
        }
    }
    
    public void setHasPlayedOnce(boolean hasPlayedOnce) {
        this.hasPlayedOnce = true;
    }

    public boolean hasPlayedOnce() {
        return hasPlayedOnce;
    }
    
    public BufferedImage[] getFrames() {
        return images;
    }
    
    public int getFrameIndex() {
        return index;
    }

    public void setFrame(int i) {
        this.index = i;
    }
}