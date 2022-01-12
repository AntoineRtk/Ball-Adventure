package obstacles;

import entity.MapObject;
import states.GameStateManager;
import tilemap.AreaZone;

public abstract class Obstacle extends MapObject {
    
    public GameStateManager gsm;
    private boolean solid = false;
    private int indexEvent = -1;
    
    public Obstacle(AreaZone area, double x, double y, int instantID, GameStateManager gsm) {
        super(area, x, y, instantID);
        this.gsm = gsm;
    }
    
    public void setIndexEvent(int index) {
        this.indexEvent = index;
    }
    
    public int getIndexEvent() {
        return indexEvent;
    }
    
    public void update() {
        super.checkCollisions();
        if(animation.getFrames() != null) {
            animation.update();
        }
    }
    
    public void event() {}
    
    public abstract void collides(MapObject object);
    
    public void setSolid(boolean isSolid) {
        this.solid = isSolid;
    }
    
    public boolean isSolid() {
        return solid;
    }
}