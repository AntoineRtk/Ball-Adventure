package states.levels;

import entity.Hero;
import states.GameLevelState;
import states.GameStateManager;

public class Level4 extends GameLevelState {
    
    public Level4(GameStateManager gsm) {
        super(gsm);
    }
    
    public void init() {
        setLoading(true);
        clearAll();
        readMap("map4.txt", true, "tiles.png", true);
        hero = new Hero(gsm, areas[0], 400, 500, 0);
        areas[0].setHero(hero);
        setArea(getCheckPoint());
        setMapPositions();
    }
}