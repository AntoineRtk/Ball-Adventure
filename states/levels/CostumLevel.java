package states.levels;

import entity.Hero;
import states.GameLevelState;
import states.GameStateManager;

public class CostumLevel extends GameLevelState {
    
    private String path;
    
    public CostumLevel(GameStateManager gsm) {
        super(gsm);
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public void init() {
        setLoading(true);
        readMap(path, false, "tiles.png", true);
        hero = new Hero(gsm, areas[0], 32, 32, 0);
        areas[0].setHero(hero);
        setArea(getCheckPoint());
        initMusic();
        setMapPositions();
    }
}